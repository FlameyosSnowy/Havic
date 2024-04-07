package me.flame.havic.spigot;

import me.flame.havic.common.command.CommandInfo;
import me.flame.havic.common.command.ContextCommand;

import java.util.List;

public abstract class SpigotCommand extends ContextCommand<SpigotSender> {
    public SpigotCommand(String name, String desc, String usage, String perm, List<String> aliases) {
        super(name, desc, usage, perm, aliases);
    }

    public SpigotCommand(CommandInfo info) {
        super(info.getName(), info.getDescription(), info.getUsage(), info.getPermission(), info.getAliases());
    }
}
