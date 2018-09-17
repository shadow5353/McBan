package com.shadow5353;

import com.shadow5353.Managers.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jacob on 17-09-2018.
 */
public class McBan extends JavaPlugin {

    public void onEnable() {
        getCommand("tempban").setExecutor(new CommandManager());

        saveDefaultConfig();
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("McBan");
    }
}
