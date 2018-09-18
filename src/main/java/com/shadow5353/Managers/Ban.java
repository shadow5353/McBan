package com.shadow5353;

import com.shadow5353.Managers.SettingsManager;

import java.util.UUID;

public class Ban {
    private int id;
    private String reason, date, bannedTo;
    private UUID playerUUID, adminUUID;

    public Ban(int id) {
        this.id = id;

        this.reason = SettingsManager.getNotes().get("bans." + id + ".reason");
        this.date = SettingsManager.getNotes().get("bans." + id + ".date");
		this.bannedTo = SettingsManager.getNotes().get("bans." + id + ".bannedTo");
        this.playerUUID = UUID.fromString(SettingsManager.getNotes().get("bans." + id + ".playerUUID").toString());
        this.adminUUID = UUID.fromString(SettingsManager.getNotes().get("bans." + id + ".adminUUID").toString());
    }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }
	
	public String getBannedTo() {
        return bannedTo;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public UUID getAdminUUID() {
        return adminUUID;
    }
}