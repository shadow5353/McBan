package com.shadow5353.Managers;

import com.shadow5353.McBan;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BanManager {
    private MessageManager message = new MessageManager();
    private OfflinePlayer targetPlayer;

    public BanManager() { }

    public void ShowMenu(Player player, final OfflinePlayer targetPlayer, String arg) {
        boolean broadcast = true;
        this.targetPlayer = targetPlayer;

        if (arg.equalsIgnoreCase("-s")) {
            broadcast = false;
        }

        ItemStack p = new ItemStack(Material.getMaterial("DIAMOND_ORE"));

        IconMenu menu = new IconMenu("Players with notes", 36, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                message.info(event.getPlayer(), "Temp banning " + targetPlayer);
                event.setWillClose(true);
            }
        }, McBan.getPlugin());

        menu.setOption(0, p, "Diamond Ore", "Temp ban in x days");

        menu.open(player);

    }

    private void TempBanPlayer(OfflinePlayer player, String amount) {

    }
}
