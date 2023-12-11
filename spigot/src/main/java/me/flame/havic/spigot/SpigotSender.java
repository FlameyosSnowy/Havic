package me.flame.havic.spigot;

import me.flame.havic.common.sender.Sender;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotSender implements Sender<CommandSender> {
    private final CommandSender sender;

    SpigotSender(CommandSender sender) {
        this.sender = sender;
    }

    @NotNull
    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    @Nullable
    public Player getPlayer() {
        return sender instanceof Player ? (Player) sender : null;
    }

    @Override
    public ConsoleCommandSender getConsole() {
        return sender instanceof ConsoleCommandSender ? (ConsoleCommandSender) sender : null;
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }
}
