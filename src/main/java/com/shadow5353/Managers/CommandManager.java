package com.shadow5353.Managers;

import com.shadow5353.Commands.BanCommands;
import com.shadow5353.McBan;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class CommandManager implements CommandExecutor {
    private MessageManager message = new MessageManager();
    private FileConfiguration config = McBan.getPlugin().getConfig();

    private ArrayList<BanCommands> cmds = new ArrayList<BanCommands>();

    public CommandManager() {
        // Todo add command
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            message.error(sender, "Only players can use StaffNotes!");
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("staffnotes")) {
            if (!p.hasPermission("staffnotes.use")) {
                message.noPermission(p);
            } else {

                if (args.length == 0) {
                    p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    message.command(p, "/sn" + ChatColor.BLACK + " : " + ChatColor.YELLOW + "Show a list of commands");
                    for (BanCommands mc : cmds)
                        message.command(p, "/sn " + aliases(mc) + " " + mc.getUsage() + ChatColor.BLACK + " : " + ChatColor.YELLOW + mc.getMessage());
                    p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    return true;
                }

                BanCommands c = getCommand(args[0]);

                if (c == null) {
                    message.error(p, "That command doesn't exist!");
                    return true;
                }

                Vector<String> a = new Vector<String>(Arrays.asList(args));
                a.remove(0);
                args = a.toArray(new String[a.size()]);

                c.onCommand(p, args);

            }
            return true;
        }
        return true;
    }

    private String aliases(BanCommands cmd) {
        String fin = "";

        for (String a : cmd.getAliases()) {
            fin += a + " | ";
        }

        return fin.substring(0, fin.lastIndexOf(" | "));
    }

    private BanCommands getCommand(String name) {
        for (BanCommands cmd : cmds) {
            if (cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
            for (String alias : cmd.getAliases()) if (name.equalsIgnoreCase(alias)) return cmd;
        }
        return null;
    }
}
