package me.flame.havic.common.manager;

import lombok.NonNull;
import me.flame.havic.common.command.ContextCommand;
import me.flame.havic.common.command.PlayerCooldownData;
import me.flame.havic.common.sender.Sender;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class CooldownManager<S extends Sender, C extends ContextCommand<S>> {
    private final Map<C, PlayerCooldownData> cooldownMap = new HashMap<>(10);

    public boolean cooldownActivated(@NonNull UUID uuid, @NonNull C command) {
        PlayerCooldownData info = cooldownMap.get(command);
        if (info == null) {
            cooldownMap.put(command, new PlayerCooldownData());
            return false;
        }
        return info.isExpired(uuid);
    }

    @SuppressWarnings("UnusedReturnValue")
    public void cooldownUser(@NonNull UUID uuid, @NonNull C command, Duration expiryTime) {
        PlayerCooldownData info = cooldownMap.get(command);
        if (info == null) {
           cooldownMap.put(command, new PlayerCooldownData());
           info = cooldownMap.get(command);
        }
        info.add(uuid, expiryTime);
    }

    public void commandInfo(C command) {
        cooldownMap.put(command, new PlayerCooldownData());
    }

    /**
     * Expires the cooldown of the given UUID for the given command only if it is already expired.
     * <p>
     * If it is not expired, nothing happens, as it needs to be expired first.
     * @param player the player's UUID
     * @param command the command
     */
    public void expire(UUID player, C command) {
        PlayerCooldownData info = cooldownMap.get(command);
        if (info != null && info.isExpired(player)) cooldownMap.remove(command);
    }

    /**
     * Expires the cooldown of the given UUID for the given command immediately even if it is not expired.
     * @param player the player's UUID
     * @param command the command
     */
    public void expireNow(UUID player, C command) {
        cooldownMap.remove(command);
    }
}
