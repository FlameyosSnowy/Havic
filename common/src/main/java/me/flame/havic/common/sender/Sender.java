package me.flame.havic.common.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Sender<P> {
    @NotNull
    P getSender();

    @Nullable
    P getPlayer();

    @Nullable
    P getConsole();

    boolean isPlayer();

    boolean isConsole();

}
