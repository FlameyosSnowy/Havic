package me.flame.havic.spigot;

import me.flame.havic.common.sender.Sender;
import org.bukkit.ChatColor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Lib {
    public static void alert(@NotNull Sender player, @NotNull Message message) {
        player.send(message.getMessage());
    }

    public static void alertCooldownActive(@NotNull Sender player, Duration cooldown, SpigotCommandManager manager) {
        player.send(Message.COOLDOWN_ACTIVE.getMessage().replace("%time%", asString(cooldown, manager)));
    }

    private static @NotNull String asString(@NotNull Duration duration, SpigotCommandManager manager) {
        long millis = duration.toMillis();
        if (millis < 1000) return millis + manager.getDurationName(0);
        long seconds = duration.toSeconds();
        if (seconds < 60) return seconds + manager.getDurationName(1);
        long minutes = duration.toMinutes();
        if (minutes < 60) return minutes + manager.getDurationName(2);
        long hours = duration.toHours();
        if (hours < 24) return hours + manager.getDurationName(3);
        long days = duration.toDays();
        return days + manager.getDurationName(4);
    }

    @Contract("_ -> new")
    public static @NotNull String style(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
