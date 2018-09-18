package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.SBan;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Info extends BanCommands {
    private MessageManager msg = new MessageManager();

    public void onCommand(Player p, String[] args) {
        if (!p.hasPermission("SBan.use")) {
            msg.noPermission(p);
        } else {
            PluginDescriptionFile pdf = SBan.getPlugin().getDescription();
            p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
            p.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + pdf.getName());
            p.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdf.getVersion());
            p.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "shadow5353");
            p.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.YELLOW + pdf.getDescription());
            p.sendMessage(ChatColor.GOLD + "Jenkins: " + ChatColor.YELLOW + "https://jenkins.shadow5353.com/job/SBan/");
            p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
        }
    }

    public Info() {
        super("Show information about the SBan", "info", "i");
    }
}
