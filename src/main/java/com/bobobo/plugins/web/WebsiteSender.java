package com.bobobo.plugins.web;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class WebsiteSender {
    public static void sendDataToWebsite(Player player, long playtime, String apiUrl) {
        long days = playtime / (20 * 60 * 24);
        try {
            int responseCode = sendRequest(player, playtime, days, apiUrl);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                player.getServer().getLogger().info("Данные отправлены для игрока: " + player.getName());
            } else {
                player.getServer().getLogger().warning("Ошибка при отправке данных для игрока: " + player.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static int sendRequest(Player player, long playtime, long days, String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");

        // Формируем данные через объект JSON
        String jsonInputString = String.format("{\"player\":\"%s\",\"playtime\":%d,\"days\":%d}",
                player.getName(), playtime, days);

        // Добавляем логирование, чтобы увидеть данные перед отправкой
        System.out.println("Отправляем JSON: " + jsonInputString);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return connection.getResponseCode();
    }


}
