package com.bobobo.plugins.conf;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
public class ConfigManager {
    private final JavaPlugin plugin;
    private String apiUrl;
    private int updateInterval;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfigValues();
    }
    public void loadConfigValues() {
        FileConfiguration config = plugin.getConfig();
        apiUrl = config.getString("api-url", "https://ttt.ru/api.php");
        updateInterval = config.getInt("update-interval-ticks", 600);
    }
    public String getApiUrl() {
        return apiUrl;
    }
    public int getUpdateInterval() {
        return updateInterval;
    }
    public void reload() {
        plugin.reloadConfig();
        loadConfigValues();
    }
}
