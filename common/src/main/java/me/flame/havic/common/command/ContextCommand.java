package me.flame.havic.common.command;

import lombok.Getter;
import lombok.Setter;
import me.flame.havic.common.manager.CooldownManager;
import me.flame.havic.common.sender.Sender;
import me.flame.havic.common.utils.Lazy;
import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class ContextCommand<S extends Sender<?>> {
    private final String name, description, usage, permission;
    private final List<String> aliases;

    @Setter
    private Duration cooldown;

    @Setter
    private boolean playerOnly, consoleOnly, async, quoting = false;

    private final Lazy<Map<String, SubcommandHandler<S>>> container = new Lazy<>(new ConcurrentHashMap<>(5));

    public ContextCommand(CommandInfo info) {
        this.name = info.getName();
        this.description = info.getDescription();
        this.usage = info.getUsage();
        this.permission = info.getPermission();
        this.aliases = info.getAliases();
        this.playerOnly = false;
        this.consoleOnly = false;
        this.async = false;
    }

    private Map<String, SubcommandHandler<S>> getContainer() {
        return container.get();
    }

    public abstract Response execute(S sender, ContextArguments<?> args);

    protected final void registerSubCommand(String string, SubcommandHandler<S> handler) {
        getContainer().put(string, handler);
    }

    @ApiStatus.Internal
    public Map<String, SubcommandHandler<S>> getImmutableSubcommandsContainer() {
        return Collections.unmodifiableMap(getContainer());
    }


}
