package com.bobobo.plugins;

import com.bobobo.plugins.com.ReloadCommand;
import com.bobobo.plugins.conf.ConfigManager;
import com.bobobo.plugins.db.DatabaseManager;
import com.bobobo.plugins.web.WebsiteSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlaytimeTracker extends JavaPlugin {
    private final HashMap<UUID, Long> playtimeMap = new HashMap<>();
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this);
        Objects.requireNonNull(getCommand("ptreload")).setExecutor(new ReloadCommand(configManager));
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlaytime(player);
                    WebsiteSender.sendDataToWebsite(player, playtimeMap.get(player.getUniqueId()), configManager.getApiUrl());
                    saveDataLocally(player);
                }
            }
        }.runTaskTimer(this, 0, configManager.getUpdateInterval());
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
    }
    private void updatePlaytime(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playtimeMap.containsKey(uuid)) {
            playtimeMap.put(uuid, 0L);
        }

        long playtime = playtimeMap.get(uuid);
        playtime += 5;
        playtimeMap.put(uuid, playtime);
    }

    private void saveDataLocally(Player player) {
        UUID uuid = player.getUniqueId();
        long playtime = playtimeMap.get(uuid);
        long days = playtime / (20 * 60 * 24);
        databaseManager.savePlayerData(uuid.toString(), player.getName(), playtime, days);
    }
}
