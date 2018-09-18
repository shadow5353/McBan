package com.shadow5353.Managers;

import com.shadow5353.SBan;
import com.sun.javafx.image.IntPixelGetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

class BanInformation {
    public ItemStack item;
    public String reason;
    public String amount;
    public String perm;

    public BanInformation(ItemStack item, String reason, String amount, String perm) {
        this.item = item;
        this.reason = reason;
        this.amount = amount;
        this.perm = perm;
    }
}

public class BanManager {
    private MessageManager message = new MessageManager();
    private ArrayList<BanInformation> banInformations = new ArrayList<BanInformation>();
    private FileConfiguration config = SBan.getPlugin().getConfig();
    private int amountOfRows = 1;
    private boolean mysql;
    private ArrayList<Player> cooldown = new ArrayList<Player>();

    public BanManager() {
        getBanInformations();

        mysql = config.getString("savingType").equalsIgnoreCase("mysql");
    }

    public void showMenu(Player player, final OfflinePlayer targetPlayer, final String arg) {

        IconMenu menu = new IconMenu("Choose a ban", amountOfRows * 9, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(final IconMenu.OptionClickEvent event) {
                if (!cooldown.contains(event.getPlayer())) {
                    cooldown.add(event.getPlayer());
                    tempBanPlayer(event.getPlayer(), targetPlayer, arg, event.getName());

                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SBan.getPlugin(), new Runnable(){
                        public void run(){
                            cooldown.remove(event.getPlayer());
                        }
                    }, 5);

                }
                event.setWillClose(true);
            }
        }, SBan.getPlugin());

        int position = 0;
        for (BanInformation bi : banInformations) {
            if (player.hasPermission(bi.perm)) {
                String description = "";

                if (bi.amount.equalsIgnoreCase("BAN")) {
                    description = "Permanent banned";
                } else if (bi.amount.contains("D")) {
                    description = "Banned for " + bi.amount.replace("D", "") + " days";
                } else if (bi.amount.contains("H")) {
                    description = "Banned for " + bi.amount.replace("H", "") + " hours";
                }

                menu.setOption(position, bi.item, bi.reason, description);
                position++;
            }
        }

        menu.open(player);

    }

    private void getBanInformations() {
        int counter = 0;

        for (String key : config.getConfigurationSection("bans").getKeys(false)) {
            String reason = config.getString("bans." + key + ".reason");
            String amount = config.getString("bans." + key + ".time");
            String perm = config.getString("bans." + key + ".perm");
            int itemId = config.getInt("bans." + key + ".item");

            if (counter == 18 || counter == 27 || counter == 36 || counter == 45 || counter == 54) {
                amountOfRows++;
            }

            ItemStack item = new ItemStack(Material.getMaterial(itemId));

            banInformations.add(new BanInformation(item, reason, amount, perm));
            counter++;
        }
    }

    private String getAmount(String reason) {
        for (BanInformation bi : banInformations) {
            if (bi.reason.equals(reason)) {
                return bi.amount;
            }
        }
        return "";
    }

    private void tempBanPlayer(Player player, OfflinePlayer targetPlayer, String arg, String reason) {
        UUID targetUUID = targetPlayer.getUniqueId();
        UUID adminUUID = player.getUniqueId();

        boolean broadcast = true;

        if (arg.equalsIgnoreCase("-s")) {
            broadcast = false;
        }

        String amount = getAmount(reason);

        if (amount.equalsIgnoreCase("BAN")) {
            boolean banning = saveBan(adminUUID, targetUUID, amount, reason, true);

            if (banning) {
                message.good(player, targetPlayer.getName() + " have been perm banned!");
            } else {
                message.error(player, "Something went wrong banning this player, check console!");
            }
        } else if (amount.contains("D")) {
            int amountInt = Integer.parseInt(amount.replace("D", "")) * 24;

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, +amountInt);
            Date date = calendar.getTime();

            boolean banning = saveBan(adminUUID, targetUUID, date.toString(), reason, false);

            if (banning)
                message.good(player, targetPlayer.getName() + " have been until " + date.toString() + "!");
            else
                message.error(player, "Something went wrong banning this player, check console!");
        } else if (amount.contains("H")) {
            int amountInt = Integer.parseInt(amount.replace("H", ""));

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, +amountInt);
            Date date = calendar.getTime();

            boolean banning = saveBan(adminUUID, targetUUID, date.toString(), reason, false);

            if (banning)
                message.good(player, targetPlayer.getName() + " have been until " + date.toString() + "!");
            else
                message.error(player, "Something went wrong banning this player, check console!");
        } else {
            message.error(player, "The time need one of following reasons: [D, H, BAN]");
        }
    }

    private boolean saveBan(UUID adminPlayerUUID, UUID targetPlayerUUID, String amount, String reason, boolean perm) {
        if (mysql) {
            try {
                int permed = 0;

                if (perm)
                    permed = 1;

                MySQL.getInstance().getStatement().executeUpdate("INSERT INTO bans (fldUUID, fldReason, fldAdmin, fldPerm) " +
                        " VALUES ('" + targetPlayerUUID + "', '" + reason + "', '" + adminPlayerUUID + "', '" + permed + "')");

                return true;
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("Something went wrong saving to mysql database, error message: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            FlatSaving.getInstance().saveBan(reason, targetPlayerUUID, adminPlayerUUID, timestamp.toString(), amount, perm);
            return true;
        }
    }
}
