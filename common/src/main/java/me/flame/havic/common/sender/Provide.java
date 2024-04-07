package me.flame.havic.common.sender;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
@SuppressWarnings("unused")
public interface Provide<S extends Sender, C> {
    static <S extends Sender> Provide<S, S> self() {
        return (s) -> s;
    }

    C map(@NotNull S sender);
}