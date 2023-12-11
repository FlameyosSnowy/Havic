package me.flame.havic.spigot;

import me.flame.havic.common.command.ContextArguments;
import me.flame.havic.common.utils.Option;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class SpigotContextArguments extends ContextArguments<Player> {
    SpigotContextArguments(String[] args) {
        super(args);
    }

    @Override
    public Player getPlayer(int index) {
        @Nullable
        Player player = Bukkit.getPlayer(args[index]);

        return player;
    }

    @Override
    public Option<Player> getOptionalPlayer(int index) {
        return Option.of(this.getPlayer(index));
    }

    String[] getArray() {
        return args;
    }
}
