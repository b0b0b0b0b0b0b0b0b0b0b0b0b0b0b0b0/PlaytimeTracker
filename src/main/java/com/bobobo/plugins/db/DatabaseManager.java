package com.bobobo.plugins.db;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection connection;
    private final JavaPlugin plugin;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initDatabase();
    }

    // Инициализация базы данных
    private void initDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/playtime.db");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Создание таблицы для хранения данных игроков
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS playtime (" +
                "uuid TEXT PRIMARY KEY, " +
                "player_name TEXT, " +
                "playtime INTEGER, " +
                "days INTEGER)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Сохранение данных в базу данных
    public void savePlayerData(String uuid, String playerName, long playtime, long days) {
        String sql = "INSERT OR REPLACE INTO playtime (uuid, player_name, playtime, days) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, playerName);
            pstmt.setLong(3, playtime);
            pstmt.setLong(4, days);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Закрытие соединения с базой данных
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
