package me.flame.havic.common.manager;

import lombok.Setter;
import me.flame.havic.common.command.CommandExecutionType;
import me.flame.havic.common.command.ContextCommand;
import me.flame.havic.common.command.registries.SenderProviderRegistry;
import me.flame.havic.common.command.registries.TypeConversionRegistry;
import me.flame.havic.common.sender.Sender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.OverrideOnly
@SuppressWarnings("unused")
public abstract class AbstractCommandManager<P, C extends ContextCommand<S>, S extends Sender> {
    protected final P plugin;
    protected final String pluginName;
    protected final CommandExecutionType executionType;

    protected final CooldownManager<S, C> cooldownManager = new CooldownManager<>();
    protected final TypeConversionRegistry typeConversionRegistry = new TypeConversionRegistry();
    protected final SenderProviderRegistry<S> senderProviderRegistry = new SenderProviderRegistry<>();

    private final List<C> commands = new ArrayList<>(16);


    @Setter
    private String cooldownPermission;

    private List<String> sortedDurationNames = List.of("Milliseconds", "Seconds", "Minutes", "Hours", "Days");

    protected AbstractCommandManager(P plugin, String pluginName, CommandExecutionType type) {
        this.plugin = plugin;
        this.pluginName = pluginName;
        this.executionType = type;
    }

    @SafeVarargs
    protected AbstractCommandManager(P plugin, String pluginName, CommandExecutionType type, C... commands) {
        this.plugin = plugin;
        this.pluginName = pluginName;
        this.executionType = type;
        register(commands);
    }

    public void register(C command) {
        if (command == null) return;
        commands.add(command);
    }

    protected void initializeManager(P plugin, String pluginName, CommandExecutionType type) {
        Arguments.typeConversionRegistry(typeConversionRegistry);
    }

    @SafeVarargs
    public final void register(C @NotNull ... commands) {
        for (C command : commands) {
            register(command);
        }
    }

    public String getDurationName(int index) {
        return sortedDurationNames.get(index);
    }

    public void setDurationNames(@NotNull List<String> names) {
        int size = names.size();
        int namesSize = sortedDurationNames.size();

        if (size != namesSize) {
            throw new IllegalArgumentException("Size should have been " + namesSize + " but turned out to be " + size);
        }

        this.sortedDurationNames = names;
    }

    public CommandExecutionType executionType() {
        return executionType;
    }

    public CooldownManager<S, C> cooldownManager() {
        return cooldownManager;
    }

    public TypeConversionRegistry typeConversionRegistry() {
        return typeConversionRegistry;
    }

    public SenderProviderRegistry<S> senderProviderRegistry() {
        return senderProviderRegistry;
    }

    public List<C> commands() {
        return commands;
    }

    public String cooldownPermission() {
        return cooldownPermission;
    }
}
