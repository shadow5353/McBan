package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import org.bukkit.entity.Player;

public class Help extends BanCommands {
    private MessageManager msg = new MessageManager();


    public void onCommand(Player p, String[] args) {
        if (!p.hasPermission("mcban.use")) {
            msg.noPermission(p);
        } else {
            msg.good(p, "Temp ban");
        }
    }

    public Help() {
        super("Shows a list of commands", "", "");
    }
}
