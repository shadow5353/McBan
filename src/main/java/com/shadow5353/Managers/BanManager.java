package com.shadow5353.Managers;

import com.shadow5353.McBan;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

class BanInformation {
    public ItemStack item;
    public String reason;
    public String amount;

    public BanInformation(ItemStack item, String reason, String amount) {
        this.item = item;
        this.reason = reason;
        this.amount = amount;
    }
}

public class BanManager {
    private MessageManager message = new MessageManager();
    private ArrayList<BanInformation> banInformations = new ArrayList<BanInformation>();
    private FileConfiguration config = McBan.getPlugin().getConfig();
    private int amountOfRows = 1;
    private OfflinePlayer targetPlayer;

    public BanManager() {}

    public void ShowMenu(Player player, final OfflinePlayer targetPlayer, String arg) {
        GetBanInformations();
        boolean broadcast = true;
        this.targetPlayer = targetPlayer;

        if (arg.equalsIgnoreCase("-s")) {
            broadcast = false;
        }

        IconMenu menu = new IconMenu("Players with notes", amountOfRows * 9, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                message.info(event.getPlayer(), "Temp banning " + targetPlayer);
                event.setWillClose(true);
            }
        }, McBan.getPlugin());

        int position = 0;
        for (BanInformation bi : banInformations) {
            String description = "";

            if (bi.amount.equalsIgnoreCase("BAN")) {
                description = "Permanent banned";
            } else if (bi.amount.equals("D")) {
                description = "Banned for " + bi.amount.replace("D", "") + " days";
            } else if (bi.amount.equals("H")) {
                description = "Banned for " + bi.amount.replace("H", "") + " hours";
            }

            menu.setOption(position, bi.item, bi.reason, description);
            position++;
        }

        menu.open(player);

    }

    private void GetBanInformations() {
        boolean found = false;
        int counter = 1;

        do {
            if (config.contains("bans." + counter)) {
                found = true;
                counter++;

                String reason = config.getString("bans." + counter + ".reason");
                String amount = config.getString("bans." + counter + ".time");
                int itemId = config.getInt("bans." + counter + ".item");

                if (counter == 18 || counter == 27 || counter == 36 || counter == 45 || counter == 54) {
                    amountOfRows++;
                }

                ItemStack item = new ItemStack(Material.getMaterial(itemId));

                banInformations.add(new BanInformation(item, reason, amount));
            }
        } while (!found);
    }

    private void TempBanPlayer(OfflinePlayer player, String amount) {

    }
}
