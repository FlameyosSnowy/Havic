package me.flame.havic.common.manager;

import lombok.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({ "unused", "ResultIsIgnored" })
public class CooldownManager {
    private final Map<UUID, Map<String, Instant>> cooldownMap = new HashMap<>(100);

    public boolean isCooldownActive(@NonNull UUID uuid, @NonNull String commandName) {
        Map<String, Instant> durationMap = cooldownMap.get(uuid);
        if (durationMap == null) {
            cooldownMap.put(uuid, new HashMap<>());
            return false;
        }
        Instant duration = durationMap.get(commandName);
        return (Instant.now().isBefore(duration));
    }

    public void addCooldown(@NonNull UUID uuid, @NonNull String commandName, Duration expiryTime) {
        Map<String, Instant> durationMap = cooldownMap.get(uuid);
        if (durationMap == null) {
           cooldownMap.put(uuid, new HashMap<>());
           durationMap = cooldownMap.get(uuid);
        }
        durationMap.put(commandName, Instant.now().plus(expiryTime));
    }
}
