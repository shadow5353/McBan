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
        if (SettingsManager.getbans().<ConfigurationSection>get("bans") == null) SettingsManager.getbans().createConfigurationSection("bans");

        bans.clear();

        for (String key : SettingsManager.getBans().<ConfigurationSection>get("bans").getKeys(false)) {
            bans.add(new Note(Integer.parseInt(key)));
        }
    }

    public ArrayList<Ban> getbans() {
        return bans;
    }

    public void saveBan(String note, UUID playerUUID, UUID adminUUID, String date){
        int id = getbans().size() + 1;

        SettingsManager.getbans().createConfigurationSection("bans." + id);
        SettingsManager.getbans().set("bans." + id + ".note", note);
        SettingsManager.getbans().set("bans." + id + ".date", date);
        SettingsManager.getbans().set("bans." + id + ".playerUUID", playerUUID.toString());
        SettingsManager.getbans().set("bans." + id + ".adminUUID", adminUUID.toString());

        setupBans();
    }

    public Note getBan(int id) {
         return new Note(id);
    }

    public boolean removeBan(UUID playerUUID, int id) {
        boolean found = false;

        if (getNote(id).getPlayerUUID().equals(playerUUID)) {
            found = true;

            SettingsManager.getbans().removePath("bans." + id);

            setupbans();
        }

        return found;

    }

    public void reset() {
        SettingsManager.getbans().removePath("bans");
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