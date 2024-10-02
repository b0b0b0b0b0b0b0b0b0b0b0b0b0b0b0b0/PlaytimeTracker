package com.bobobo.plugins;

import com.bobobo.plugins.data.PlaytimeDataManager;
import com.bobobo.plugins.web.WebsiteSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlaytimeTracker extends JavaPlugin {
    private WebsiteSender websiteSender;
    private PlaytimeDataManager playtimeDataManager;
    private final HashMap<UUID, Long> playerLastCheckTime = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playtimeDataManager = new PlaytimeDataManager(getDataFolder()); // Инициализируем PlaytimeDataManager
        websiteSender = new WebsiteSender(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    updatePlaytime(player);
                    long playtime = playtimeDataManager.getPlaytime(player.getUniqueId());
                    websiteSender.sendDataToWebsite(player, playtime, getConfig().getString("api-url"));
                }
                playtimeDataManager.savePlaytimeData(); // Сохраняем данные каждую итерацию
            }
        }.runTaskTimer(this, 0, getConfig().getInt("update-interval-ticks"));
    }

    @Override
    public void onDisable() {
        playtimeDataManager.savePlaytimeData(); // Сохраняем данные при выключении плагина
    }

    private void updatePlaytime(Player player) {
        UUID uuid = player.getUniqueId();
        long currentPlaytime = playtimeDataManager.getPlaytime(uuid);  // Получаем текущее накопленное время

        // Получаем текущее время и время последней проверки
        long currentTime = System.currentTimeMillis();
        long lastCheckTime = playerLastCheckTime.getOrDefault(uuid, currentTime);

        // Вычисляем игровое время, проведённое с момента последней проверки в секундах
        long playedTimeInMillis = currentTime - lastCheckTime;
        long playedTimeInSeconds = playedTimeInMillis / 1000;  // Без округления до минут

        // Обновляем игровое время, добавляем проведённые секунды
        playtimeDataManager.setPlaytime(uuid, currentPlaytime + playedTimeInSeconds);

        // Обновляем время последней проверки
        playerLastCheckTime.put(uuid, currentTime);
    }


    public void setPlayerJoinTime(Player player) {
        UUID uuid = player.getUniqueId();
        playerLastCheckTime.put(uuid, System.currentTimeMillis());
    }
}
