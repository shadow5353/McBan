package com.shadow5353.Managers;


import java.util.UUID;

public class Ban {
    private int id;
    private String reason, date, bannedTo;
    private UUID playerUUID, adminUUID;

    public Ban(int id) {
        this.id = id;

        this.reason = SettingsManager.getBans().get("bans." + id + ".reason");
        this.date = SettingsManager.getBans().get("bans." + id + ".date");
		this.bannedTo = SettingsManager.getBans().get("bans." + id + ".bannedTo");
        this.playerUUID = UUID.fromString(SettingsManager.getBans().get("bans." + id + ".playerUUID").toString());
        this.adminUUID = UUID.fromString(SettingsManager.getBans().get("bans." + id + ".adminUUID").toString());
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