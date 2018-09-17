package com.shadow5353.Managers;

import java.io.File;

import com.shadow5353.McBan;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {
    private File file;
    private FileConfiguration config;

    private static SettingsManager bans = new SettingsManager(McBan.getPlugin().getConfig().get("filename").toString());

    public static SettingsManager getBans() {
        return bans;
    }

    private SettingsManager(String fileName) {
        System.out.println(McBan.getPlugin());

        if (!McBan.getPlugin().getDataFolder().exists()) McBan.getPlugin().getDataFolder().mkdir();

        file = new File(McBan.getPlugin().getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void set(String path, Object value) {
        config.set(path, value);
        try { config.save(file); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public ConfigurationSection createConfigurationSection(String path) {
        ConfigurationSection cs = config.createSection(path);
        try { config.save(file); }
        catch (Exception e) { e.printStackTrace(); }
        return cs;
    }

    public boolean removePath(String path) {
        config.set(path, null);

        try {
            config.save(file);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }
}