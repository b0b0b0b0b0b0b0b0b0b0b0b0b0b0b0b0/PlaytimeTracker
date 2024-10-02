package com.bobobo.plugins.com;
import com.bobobo.plugins.conf.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
public class ReloadCommand implements CommandExecutor {
    private final ConfigManager configManager;

    public ReloadCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ptreload")) {
            if (sender.hasPermission("playtime.reload")) {
                configManager.reload();
                sender.sendMessage("Конфигурация плагина PlaytimeTracker перезагружена.");
            } else {
                sender.sendMessage("У вас нет прав на выполнение этой команды.");
            }
            return true;
        }
        return false;
    }
}
