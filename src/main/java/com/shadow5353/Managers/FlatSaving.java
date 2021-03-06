package com.shadow5353.Managers;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.UUID;

public class FlatSaving {

    private FlatSaving() {}
    private static FlatSaving instance = new FlatSaving();

    public static FlatSaving getInstance() {
        return instance;
    }

    private ArrayList<Ban> bans = new ArrayList<Ban>();

    public void setupBans() {
        if (SettingsManager.getBans().<ConfigurationSection>get("bans") == null) SettingsManager.getBans().createConfigurationSection("bans");

        bans.clear();

        for (String key : SettingsManager.getBans().<ConfigurationSection>get("bans").getKeys(false)) {
            bans.add(new Ban(Integer.parseInt(key)));
        }
    }

    public ArrayList<Ban> getBans() {
        return bans;
    }

    public void saveBan(String reason, UUID playerUUID, UUID adminUUID, String date, String bannedTo, boolean perm){
        int id = getBans().size() + 1;

        SettingsManager.getBans().createConfigurationSection("bans." + id);
        SettingsManager.getBans().set("bans." + id + ".reason", reason);
        SettingsManager.getBans().set("bans." + id + ".date", date);
        SettingsManager.getBans().set("bans." + id + ".playerUUID", playerUUID.toString());
        SettingsManager.getBans().set("bans." + id + ".adminUUID", adminUUID.toString());
        SettingsManager.getBans().set("bans." + id + ".bannedTo", bannedTo);
        SettingsManager.getBans().set("bans." + id + ".perm", perm);

        setupBans();
    }

    public Ban getBan(int id) {
         return new Ban(id);
    }

    public boolean removeBan(UUID playerUUID, int id) {
        boolean found = false;

        if (getBan(id).getPlayerUUID().equals(playerUUID)) {
            found = true;

            SettingsManager.getBans().removePath("bans." + id);

            setupBans();
        }

        return found;

    }

    public void reset() {
        SettingsManager.getBans().removePath("bans");
        setupBans();
    }

    public boolean hasBan(UUID playerUUID) {

        for (Ban ban : getBans()) {
            if (playerUUID.equals(ban.getPlayerUUID())) {
                return true;
            }
        }
        return false;
    }
}