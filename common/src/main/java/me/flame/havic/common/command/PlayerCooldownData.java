package me.flame.havic.common.command;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerCooldownData {
    private final Map<UUID, Long> durations = new HashMap<>(10);

    public void add(UUID uuid, long duration) {
        durations.put(uuid, System.currentTimeMillis() + duration);
    }

    public void add(UUID uuid, @NotNull Duration duration) {
        durations.put(uuid, System.currentTimeMillis() + duration.toMillis());
    }

    public boolean isExpired(UUID uuid) {
        Long cooldownExpiry = durations.get(uuid);
        return cooldownExpiry != null && System.currentTimeMillis() > cooldownExpiry;
    }
}
