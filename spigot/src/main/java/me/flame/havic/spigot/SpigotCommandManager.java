package me.flame.havic.spigot;

import lombok.Getter;

import me.flame.havic.common.command.CommandExecutionType;
import me.flame.havic.common.manager.AbstractCommandManager;
import me.flame.havic.common.manager.CooldownManager;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@SuppressWarnings({ "unused", "deprecation" })
public class SpigotCommandManager extends AbstractCommandManager<Plugin, SpigotCommand, SpigotSender> {
    private static final CommandMap commandMap = getCommandMap();

    SpigotCommandManager(Plugin plugin, CommandExecutionType type) {
        super(plugin, plugin.getName(), type);
    }

    SpigotCommandManager(Plugin plugin, CommandExecutionType type, SpigotCommand... commands) {
        this(plugin, type);
        register(commands);
    }

    @Override
    public void initializeManager(Plugin plugin, String pluginName, CommandExecutionType type) {
        super.initializeManager(plugin, pluginName, type);

    }

    @NotNull
    @Contract("_ -> new")
    public static SpigotCommandManager create(Plugin plugin) {
        return new SpigotCommandManager(plugin, CommandExecutionType.SYNC);
    }

    @NotNull
    public static SpigotCommandManager create(Plugin plugin, CommandExecutionType type) {
        return new SpigotCommandManager(plugin, type);
    }

    @NotNull
    public static SpigotCommandManager create() {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SpigotCommandManager.class);
        return new SpigotCommandManager(plugin, CommandExecutionType.SYNC);
    }

    @NotNull
    @Contract("_ -> new")
    public static SpigotCommandManager create(CommandExecutionType type) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SpigotCommandManager.class);
        return new SpigotCommandManager(plugin, type);
    }

    @NotNull
    @Contract("_ -> new")
    public static SpigotCommandManager create(SpigotCommand... commands) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SpigotCommandManager.class);
        return new SpigotCommandManager(plugin, CommandExecutionType.SYNC, commands);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static SpigotCommandManager create(CommandExecutionType type, SpigotCommand... commands) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SpigotCommandManager.class);
        return new SpigotCommandManager(plugin, type, commands);
    }

    private static CommandMap getCommandMap() {
        try {
            Server server = Bukkit.getServer();
            Field mapField = server.getClass().getDeclaredField("commandMap");
            mapField.setAccessible(true);
            return (SimpleCommandMap) mapField.get(server);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void register(SpigotCommand command) {
        NativeSpigotCommand nativeCommand = new NativeSpigotCommand(command);
        commandMap.register(pluginName, nativeCommand);
        if (command.getCooldown() != null) {
            cooldownManager.commandInfo(command);
        }
    }

    private static class PlayerQuitListener implements Listener {
        private final CooldownManager<SpigotSender, SpigotCommand> cooldownManager;
        private final List<SpigotCommand> commands;

        @Contract(pure = true)
        PlayerQuitListener(@NotNull SpigotCommandManager manager) {
            this.cooldownManager = manager.cooldownManager;
            this.commands = manager.commands();
        }

        @EventHandler
        public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
            UUID uniqueId = event.getPlayer().getUniqueId();


            int size = commands.size();
            for (int commandIndex = 0; commandIndex < size; commandIndex++) {
                var command = commands.get(commandIndex);
                cooldownManager.expire(uniqueId, command);
            }
        }
    }
}
