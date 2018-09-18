package com.shadow5353.Managers;

import com.shadow5353.SBan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
    private String host, username, password, database;
    private int port;
    private Statement statement;
    private Connection connection;

    private MySQL() {
        this.host = SBan.getPlugin().getConfig().get("mysql.host").toString();
        this.username = SBan.getPlugin().getConfig().get("mysql.username").toString();
        this.password = SBan.getPlugin().getConfig().get("mysql.password").toString();
        this.port = Integer.parseInt(SBan.getPlugin().getConfig().get("mysql.port").toString());
        this.database = SBan.getPlugin().getConfig().get("mysql.database").toString();

        try {
            openConnection();
            statement = connection.createStatement();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static MySQL instance = new MySQL();

    public static MySQL getInstance() {
        return instance;
    }

    public void startUp() {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `bans` (" +
                    "`fldID` int(255) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`fldUUID` varchar(255) NOT NULL," +
                    "`fldReason` text CHARACTER SET utf16 COLLATE utf16_danish_ci NOT NULL," +
                    "`fldAdmin` varchar(255) NOT NULL," +
					"`fldBannedTo` timestamp NOT NULL," +
                    "`fldTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1\n;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }
}