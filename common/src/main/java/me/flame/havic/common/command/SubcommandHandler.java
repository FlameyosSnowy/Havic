package me.flame.havic.common.command;

import me.flame.havic.common.sender.Sender;

@FunctionalInterface
public interface SubcommandHandler<S extends Sender<?>> {
    void handle(S sender, ContextArguments<?> arguments);
}
