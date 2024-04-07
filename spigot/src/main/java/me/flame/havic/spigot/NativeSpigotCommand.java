package me.flame.havic.spigot;

import me.flame.havic.common.command.*;
import me.flame.havic.common.manager.Arguments;
import me.flame.havic.common.manager.CooldownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class NativeSpigotCommand extends Command {
    private final SpigotCommand command;
    private final String permission;
    private final boolean hasPermission;

    private static SpigotCommandManager manager;

    static void manager(SpigotCommandManager spigotManager) {
        manager = spigotManager;
    }

    NativeSpigotCommand(@NotNull SpigotCommand info) {
        super(info.getName(), info.getDescription(), info.getUsage(), info.getAliases());
        this.command = info;

        this.permission = info.getPermission();
        this.hasPermission = !permission.isEmpty();

        boolean isPlayerOnly = command.isPlayerOnly();
        boolean isConsoleOnly = command.isConsoleOnly();
        if (isPlayerOnly && isConsoleOnly) {
            throw new IllegalArgumentException("Command " + getName() + " is Player only and Console only.");
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final Map<String, CommandHandler<SpigotSender>> subcommands = command.subcommands();
        final SpigotSender player = new SpigotSender(sender);
        if (notRequiredSender(player) || isCooldownActive(player, command)) return true;

        final Arguments arguments = new Arguments(args);
        return (command.isAsync() || manager.executionType().isAsync())
                ? executeAsync(subcommands, command, player, arguments)
                : checkSubCommand(player, command, subcommands, arguments);
    }

    private boolean notRequiredSender(final SpigotSender sender) {
        if (command.isPlayerOnly() && !sender.isPlayer()) {
            Lib.alert(sender, Message.NOT_PLAYER);
            return true;
        } else if (command.isConsoleOnly() && !sender.isConsole()) {
            Lib.alert(sender, Message.NOT_CONSOLE);
            return true;
        }
        if (sender.isPlayer() && hasPermission && !sender.hasPermission(permission)) {
            Lib.alert(sender, Message.NO_PERM);
            return true;
        }
        return false;
    }

    private static boolean isCooldownActive(@NotNull final SpigotSender sender,
                                            @NotNull final SpigotCommand command) {
        Duration cooldown;
        if (sender.isConsole() || (cooldown = command.getCooldown()) == null) return false;

        final String permission = manager.cooldownPermission();
        if (permission != null && sender.hasPermission(permission)) return false;

        final UUID uuid = sender.getUniqueId();
        final CooldownManager<SpigotSender, SpigotCommand> cooldownManager = manager.cooldownManager();

        if (cooldownManager.cooldownActivated(uuid, command)) {
            Lib.alertCooldownActive(sender, cooldown, manager);
            return true;
        }

        cooldownManager.cooldownUser(uuid, command, cooldown);
        return false;
    }

    private static boolean executeAsync(final Map<String, CommandHandler<SpigotSender>> subcommands,
                                        final SpigotCommand command,
                                        final SpigotSender player,
                                        final Arguments args) {
        CompletableFuture.runAsync(() -> checkSubCommand(player, command, subcommands, args));
        return true;
    }

    private static boolean executeCommand(@NotNull final SpigotSender player, @NotNull final SpigotCommand command, @NotNull final Arguments arguments) {
        command.execute(player, arguments);
        return true;
    }

    private static boolean checkSubCommand(@NotNull final SpigotSender sender, @NotNull final SpigotCommand command,
                                           @NotNull final Map<String, CommandHandler<SpigotSender>> subcommands,
                                           @NotNull final Arguments arguments) {
        if (subcommands.isEmpty() || arguments.isEmpty()) return executeCommand(sender, command, arguments);
        String firstArgument = arguments.get(0);

        CommandHandler<SpigotSender> subcommand = subcommands.get(firstArgument);
        if (subcommand == null) return executeCommand(sender, command, arguments);

        String[] subcommandArguments = new String[arguments.size() - 1];
        subcommand.execute(sender, arguments.copy(subcommandArguments, 1));
        return true;
    }
}