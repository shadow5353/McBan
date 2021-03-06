package com.shadow5353.Managers;

import com.shadow5353.Commands.BanCommands;
import com.shadow5353.Commands.Help;
import com.shadow5353.Commands.Info;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class CommandManager implements CommandExecutor {
    private MessageManager message = new MessageManager();
    private BanManager ban = new BanManager();

    private ArrayList<BanCommands> cmds = new ArrayList<BanCommands>();

    public CommandManager() {
        cmds.add(new Help());
        cmds.add(new Info());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            message.error(sender, "Only players can use SBan!");
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (!p.hasPermission("SBan.use")) {
                message.noPermission(p);
            } else {

                if (args.length == 0) {
                    p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    message.command(p, "/tempban" + ChatColor.BLACK + " : " + ChatColor.YELLOW + "Show a list of commands");
                    message.command(p, "/tempban help" + ChatColor.BLACK + " : " + ChatColor.YELLOW + "Shows more details about commands");
                    message.command(p, "/tempban info" + ChatColor.BLACK + " : " + ChatColor.YELLOW + "Show information about the SBan");
                    message.command(p, "/tempban <Player> [-s, -p]" + ChatColor.BLACK + " : " + ChatColor.YELLOW + "Tempban/ban a player");
                    p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    return true;
                }

                BanCommands c = getCommand(args[0]);

                if (c == null) {
                    if (args[0] != null) {
                        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);

                        if (args.length == 2) {
                            ban.showMenu(p, targetPlayer, args[1]);
                        } else {
                            ban.showMenu(p, targetPlayer, "-p");
                        }
                    }
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
