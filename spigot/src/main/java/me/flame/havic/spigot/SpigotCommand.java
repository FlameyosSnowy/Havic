package me.flame.havic.spigot;

import me.flame.havic.common.command.ContextCommand;
import me.flame.havic.common.command.Response;
import me.flame.havic.common.command.SubcommandHandler;
import me.flame.havic.common.manager.CooldownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpigotCommand extends Command {
    private final ContextCommand<SpigotSender> command;
    private final String permission;
    private final boolean hasPermission;

    private final SpigotCommandManager manager;

    SpigotCommand(ContextCommand<SpigotSender> info, SpigotCommandManager spigotCommandManager) {
        super(info.getName(), info.getDescription(), info.getUsage(), info.getAliases());
        this.command = info;
        this.manager = spigotCommandManager;

        this.permission = info.getPermission();
        this.hasPermission = !permission.isEmpty();

        boolean isPlayerOnly = command.isPlayerOnly(), isConsoleOnly = command.isConsoleOnly();
        if (isPlayerOnly && isConsoleOnly) {
            throw new IllegalArgumentException("Command " + getName() + " is Player only and Console only.");
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Map<String, SubcommandHandler<SpigotSender>> subcommands = command.getImmutableSubcommandsContainer();

        if (notRequiredSender(sender) || isCooldownActive(sender, manager, command)) {
            return true;
        }

        SpigotSender player = new SpigotSender(sender);
        SpigotContextArguments arguments = new SpigotContextArguments(args);
        return (command.isAsync())
                ? executeAsync(subcommands, player, arguments)
                : checkSubCommand(player, subcommands, arguments) || executeCommand(player, arguments);
    }

    private boolean notRequiredSender(CommandSender sender) {
        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            Lib.send(sender, Message.NOT_PLAYER);
            return true;
        } else if (command.isConsoleOnly() && !(sender instanceof ConsoleCommandSender)) {
            Lib.send(sender, Message.NOT_CONSOLE);
            return true;
        }
        if ((sender instanceof Player) && hasPermission && !sender.hasPermission(permission)) {
            Lib.send(sender, Message.NO_PERM);
            return true;
        }
        return false;
    }

    private static boolean isCooldownActive(CommandSender sender,
                                            SpigotCommandManager manager,
                                            ContextCommand<SpigotSender> command) {
        Duration cooldown = command.getCooldown();
        if (sender instanceof ConsoleCommandSender || cooldown == null) return false;

        UUID uuid = ((Player) sender).getUniqueId();
        String name = command.getName();
        CooldownManager cooldownManager = manager.getCooldownManager();
        if (cooldownManager.isCooldownActive(uuid, name)) {
            Lib.sendNoCooldown(sender, cooldown);
            return true;
        }

        cooldownManager.addCooldown(uuid, name, cooldown);
        return false;
    }

    private boolean executeAsync(final Map<String, SubcommandHandler<SpigotSender>> subcommands,
                                 final SpigotSender player,
                                 final SpigotContextArguments args) {
        CompletableFuture.runAsync(() -> {
            if (checkSubCommand(player, subcommands, args)) return;
            executeCommand(player, args);
        });
        return true;
    }

    private boolean executeCommand(SpigotSender player, SpigotContextArguments arguments) {
        Response response;
        do {
            response = command.execute(player, arguments);
        } while (response == Response.RETRY);
        return true;
    }

    private boolean checkSubCommand(SpigotSender sender,
                                    @NotNull Map<String, SubcommandHandler<SpigotSender>> subcommands,
                                    SpigotContextArguments arguments) {
        if (subcommands.isEmpty() || arguments.isEmpty()) return false;
        String firstArgument = arguments.get(0);

        SubcommandHandler<SpigotSender> subcommand = subcommands.get(firstArgument);
        if (subcommand == null) return false;

        String[] subcommandArguments = new String[arguments.size()];
        System.arraycopy(arguments.getArray(), 1, subcommandArguments, 0, subcommandArguments.length);

        subcommand.handle(sender, new SpigotContextArguments(subcommandArguments));
        return true;
    }
}
