package me.flame.havic.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Lib {
    public static void send(CommandSender player, Message message) {
        player.sendMessage(message.getMessage());
    }

    public static void sendNoCooldown(CommandSender player, Duration cooldown) {
        player.sendMessage(Message.COOLDOWN_ACTIVE.getMessage().replace("%time%", asString(cooldown)));
    }

    private static String asString(Duration duration) {
        long seconds = duration.getSeconds();
        if (seconds < 60) return seconds + " Seconds";
        long minutes = duration.toMinutes();
        if (minutes < 60) return minutes + " Minutes";
        long hours = duration.toHours();
        if (hours < 24) return hours + " Hours";
        long days = duration.toDays();
        if (days < 7) return days + " Days";
        long weeks = duration.toDays() / 7;
        if (weeks < 5) return weeks + " Weeks"; // months last 4.3 weeks
        return days + " Days";
    }

    @Contract("_ -> new")
    public static @NotNull String style(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
