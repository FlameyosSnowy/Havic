package me.flame.havic.common.command.registries;

import me.flame.havic.common.sender.Provide;
import me.flame.havic.common.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class SenderProviderRegistry<S extends Sender> {

    @NotNull
    private final Map<Class<?>, Provide<S, ?>> senderProviders = new HashMap<>(5);

    @Nullable
    @SuppressWarnings("unchecked")
    private <C> Provide<S, C> getSenderProvider(Class<C> clazz) {
        return (Provide<S, C>) senderProviders.get(clazz);
    }

    public <C> void registerSender(Class<C> clazz, Provide<S, C> provider) {
        senderProviders.put(clazz, provider);
    }

    public <C> boolean isProviderPresent(Class<C> clazz) {
        return senderProviders.get(clazz) != null;
    }

    public <C> @Nullable C provideSender(@NotNull S sender, Class<C> clazz) {
        Provide<S, C> provider = getSenderProvider(clazz);
        return (provider != null) ? provider.map(sender) : null;
    }
}