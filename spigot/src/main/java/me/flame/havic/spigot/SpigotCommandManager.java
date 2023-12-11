package me.flame.havic.spigot;

import lombok.Getter;
import me.flame.havic.common.command.ContextCommand;
import me.flame.havic.common.manager.CooldownManager;
import me.flame.havic.common.utils.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class SpigotCommandManager {
    private final Plugin plugin;
    private final String pluginName;

    private static final CommandMap commandMap = getCommandMap();

    private String permission;

    @Getter
    private final CooldownManager cooldownManager = new CooldownManager();

    SpigotCommandManager(Plugin plugin) {
        this.plugin = plugin;
        this.pluginName = plugin.getName();
    }

    @NotNull
    public static SpigotCommandManager create(Plugin plugin) {
        return new SpigotCommandManager(plugin);
    }

    private static CommandMap getCommandMap() {
        try {
            Class<?> serverClass = Bukkit.getServer().getClass();
            Field commandMap = serverClass.getField("commandMap");
            commandMap.setAccessible(true);
            return (CommandMap) commandMap.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public final <C extends ContextCommand<SpigotSender>> void register(C command) {
        SpigotCommand spigotCommand = new SpigotCommand(command, this);
        commandMap.register(pluginName, spigotCommand);
    }

    @SafeVarargs
    public final <C extends ContextCommand<SpigotSender>> void register(C... commands) {
        for (C command : commands) {
            register(command);
        }
    }

    public void permissionToAvoidCooldowns(String permission) {
        this.permission = permission;
    }

    public String permissionToAvoidCooldowns() {
        return this.permission;
    }
}
