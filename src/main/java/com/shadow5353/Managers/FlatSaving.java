package com.shadow5353;

import com.shadow5353.Managers.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.UUID;

public class FlatSaving {

    private FlatSaving() {}
    private static FlatSaving instance = new FlatSaving();

    public static FlatSaving getInstance() {
        return instance;
    }

    private ArrayList<Ban> bans = new ArrayList<Note>();

    public void setupBans() {
        if (SettingsManager.getBans().<ConfigurationSection>get("bans") == null) SettingsManager.getBans().createConfigurationSection("bans");

        bans.clear();

        for (String key : SettingsManager.getBans().<ConfigurationSection>get("bans").getKeys(false)) {
            bans.add(new Note(Integer.parseInt(key)));
        }
    }

    public ArrayList<Ban> getBans() {
        return bans;
    }

    public void saveBan(String note, UUID playerUUID, UUID adminUUID, String date){
        int id = getBans().size() + 1;

        SettingsManager.getBans().createConfigurationSection("bans." + id);
        SettingsManager.getBans().set("bans." + id + ".note", note);
        SettingsManager.getBans().set("bans." + id + ".date", date);
        SettingsManager.getBans().set("bans." + id + ".playerUUID", playerUUID.toString());
        SettingsManager.getBans().set("bans." + id + ".adminUUID", adminUUID.toString());

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

            setupbans();
        }

        return found;

    }

    public void reset() {
        SettingsManager.getBans().removePath("bans");
        setupbans();
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