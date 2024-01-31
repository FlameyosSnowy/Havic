package me.flame.havic.common.command;

import lombok.Getter;
import lombok.Setter;

import me.flame.havic.common.manager.Arguments;
import me.flame.havic.common.sender.Sender;
import me.flame.havic.common.utils.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Duration;
import java.util.*;

@Getter
@SuppressWarnings("unused")
public abstract class ContextCommand<S extends Sender> {
    private final String name, description, usage, permission;
    private final List<String> aliases;

    @Setter
    private Duration cooldown;

    @Setter
    private boolean playerOnly, consoleOnly, async, quoting = false;

    private final Lazy<Map<String, CommandHandler<S>>> container = new Lazy<>(new HashMap<>(5));

    public ContextCommand(String name, String desc, String usage, String perm, List<String> aliases) {
        this.name = name;
        this.description = desc;
        this.usage = usage;
        this.permission = perm;
        this.aliases = aliases;
        this.playerOnly = false;
        this.consoleOnly = false;
        this.async = false;
    }

    private Map<String, CommandHandler<S>> getContainer() {
        return container.get();
    }

    /**
     * The code to execute on command execution of this specific command
     *
     * @param  sender  The sender that executed this command
     * @param  args    the arguments of the command input
     */
    public abstract void execute(S sender, Arguments args);

    /**
     * Registers a subcommand with the given string and handler.
     *
     * @param  string  the string to register the subcommand with
     * @param  handler the handler for the subcommand
     */
    protected final <C extends CommandHandler<S>> void registerSubCommand(String string, C handler) {
        getContainer().put(string, handler);
    }

    /**
     * Get all subcommands registered with this command and their handlers
     * <p>
     * The returned map is unmodifiable and should not be modified
     *
     * @see #registerSubCommand(String, CommandHandler)
     * @return unmodifiable map of all subcommands registered with this command
     */
    @NotNull
    @Unmodifiable
    public final Map<String, CommandHandler<S>> subcommands() {
        return Collections.unmodifiableMap(getContainer());
    }
}
