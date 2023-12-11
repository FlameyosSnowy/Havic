package me.flame.havic.common.manager;

import me.flame.havic.common.command.ContextCommand;
import me.flame.havic.common.sender.Sender;

public abstract class AbstractCommandManager<S extends Sender<?>> {
    public abstract <C extends ContextCommand<S>> void register(C command);

    @SafeVarargs
    public final <C extends ContextCommand<S>> void register(C... commands) {
        for (C command : commands) {
            register(command);
        }
    }
}
