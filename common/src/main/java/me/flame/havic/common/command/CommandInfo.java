package me.flame.havic.common.command;

import lombok.Data;

import java.util.List;


@Data
public class CommandInfo {
    private final String name, description, usage, permission;
    private final List<String> aliases;
}
