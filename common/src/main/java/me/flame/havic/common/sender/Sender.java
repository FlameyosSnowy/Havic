package me.flame.havic.common.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public interface Sender {
    @NotNull
    Object getSender();

    @Nullable
    Object getPlayer();

    @Nullable
    Object getConsole();

    <C> C as(Class<C> senderType);

    boolean isPlayer();

    boolean isConsole();

    UUID getUniqueId();

    String getName();

    void send(String message);

    @NotNull
    default Object requirePlayer() {
        return Objects.requireNonNull(getPlayer());
    }

    @NotNull
    default Object requireConsole() {
        return Objects.requireNonNull(getConsole());
    }
}
