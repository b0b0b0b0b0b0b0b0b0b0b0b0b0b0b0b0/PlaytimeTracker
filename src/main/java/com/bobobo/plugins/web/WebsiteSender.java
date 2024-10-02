package com.bobobo.plugins.web;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebsiteSender {
    private final JavaPlugin plugin;

    public WebsiteSender(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendDataToWebsite(Player player, long playtime, String apiUrl) {
        try {
            String apiToken = plugin.getConfig().getString("api-token", "default-token");
            String jsonInputString = String.format("{\"player\":\"%s\",\"playtime\":%d}", player.getName(), playtime);
            plugin.getLogger().info("Отправляем данные на сайт: " + jsonInputString);
            int responseCode = sendRequest(jsonInputString, apiUrl, apiToken);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                plugin.getLogger().info("Данные успешно отправлены для игрока: " + player.getName());
            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) { // Отловим ошибку 403
                plugin.getLogger().warning("Ошибка: неверный API-ключ для игрока: " + player.getName() +
                        ". Проверьте конфигурацию плагина, убедитесь, что API-ключ настроен правильно и синхронизирован с сервером.");
            } else {
                plugin.getLogger().warning("Ошибка при отправке данных для игрока: " + player.getName() + " (Код ответа: " + responseCode + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int sendRequest(String jsonInputString, String apiUrl, String apiToken) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Authorization", "Bearer " + apiToken);

        // Отправляем данные
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return connection.getResponseCode();
    }

}
