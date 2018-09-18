package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Help extends BanCommands {
    private MessageManager msg = new MessageManager();


    public void onCommand(Player p, String[] args) {
        if (!p.hasPermission("SBan.use")) {
            msg.noPermission(p);
        } else {
            p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
            p.sendMessage(ChatColor.YELLOW + "To ban a player: " + ChatColor.GOLD + "/tempban <Player> [-s,-p]");
            p.sendMessage(ChatColor.YELLOW + "As default it will be broadcast on the server, but if you have -s behind the name, it will not broadcast.");
            p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
        }
    }

    public Help() {
        super("Shows a list of commands", "", "");
    }
}
