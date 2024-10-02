package com.bobobo.plugins.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class PlaytimeDataManager {
    private final File playtimeFile;
    private final Gson gson = new Gson();
    private HashMap<UUID, Long> playtimeMap = new HashMap<>();

    public PlaytimeDataManager(File dataFolder) {
        this.playtimeFile = new File(dataFolder, "playtime.json");
        loadPlaytimeData();
    }

    // Загружаем данные игрового времени из JSON-файла
    public void loadPlaytimeData() {
        try {
            // Проверяем, существует ли файл. Если нет — создаём его.
            if (!playtimeFile.exists()) {
                playtimeFile.getParentFile().mkdirs(); // Создаём директорию, если её нет
                playtimeFile.createNewFile(); // Создаём сам файл
                playtimeMap = new HashMap<>(); // Инициализируем пустую карту, если файл только что создан
                return;
            }

            // Если файл существует, читаем данные
            try (FileReader reader = new FileReader(playtimeFile)) {
                Type type = new TypeToken<HashMap<UUID, Long>>(){}.getType();
                playtimeMap = gson.fromJson(reader, type);
                if (playtimeMap == null) {
                    playtimeMap = new HashMap<>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Сохраняем данные игрового времени в JSON-файл
    public void savePlaytimeData() {
        try (FileWriter writer = new FileWriter(playtimeFile)) {
            gson.toJson(playtimeMap, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Получаем игровое время для игрока
    public long getPlaytime(UUID uuid) {
        return playtimeMap.getOrDefault(uuid, 0L);
    }

    // Обновляем игровое время для игрока
    public void setPlaytime(UUID uuid, long playtime) {
        playtimeMap.put(uuid, playtime);
    }
}
