package me.flame.havic.common.command;

import lombok.Getter;

import java.util.List;

@Getter
public class CommandInfo {
    private final List<String> aliases;
    private final String name, description, usage, permission;

    private CommandInfo(String name, String description, String usage, String permission, List<String> aliases) {
        this.aliases = aliases;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public static CommandInfo of(String name, String description, String usage, String permission, List<String> aliases) {
        return new CommandInfo(name, description, usage, permission, aliases);
    }
}
