package me.flame.havic.spigot;

import me.flame.havic.common.sender.Provide;
import me.flame.havic.common.sender.Sender;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public class SpigotSender implements Sender, Permissible, Conversable, Provide<SpigotSender, SpigotSender> {
    @NotNull
    private final CommandSender sender;

    private static final UUID console = UUID.randomUUID();

    private static SpigotCommandManager manager;

    static void manager(SpigotCommandManager manager) {
        SpigotSender.manager = manager;
    }

    SpigotSender(CommandSender sender) {
        this.sender = Objects.requireNonNull(sender);
    }

    @NotNull
    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    @Nullable
    public Player getPlayer() {
        return sender instanceof Player ? (Player) sender : null;
    }

    @Override
    public ConsoleCommandSender getConsole() {
        return sender instanceof ConsoleCommandSender ? (ConsoleCommandSender) sender : null;
    }

    @Override
    public <C> C as(Class<C> senderType) {
        return manager.senderProviderRegistry().provideSender(this, senderType);
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    @Override
    public UUID getUniqueId() {
        return isPlayer() ? ((Player) sender).getUniqueId() : console;
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public void send(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean isPermissionSet(String s) {
        return sender.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return sender.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s) {
        return sender.hasPermission(s);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return sender.addAttachment(plugin, s, b);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return sender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return sender.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return sender.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        sender.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        sender.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return sender.isOp();
    }

    @Override
    public void setOp(boolean b) {
        sender.setOp(b);
    }

    @Override
    public boolean isConversing() {
        if (sender instanceof Conversable)
            return ((Conversable) sender).isConversing();
        return false;
    }

    @Override
    public void acceptConversationInput(String input) {
        if (sender instanceof Conversable) {
            ((Conversable) sender).acceptConversationInput(input);
        }
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        if (sender instanceof Conversable)
            return ((Conversable) sender).beginConversation(conversation);
        return false;
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        if (sender instanceof Conversable) {
            ((Conversable) sender).abandonConversation(conversation);
        }
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        if (sender instanceof Conversable) {
            ((Conversable) sender).abandonConversation(conversation, details);
        }
    }

    @Override
    public void sendRawMessage(String message) {
        if (sender instanceof Conversable) {
            ((Conversable) sender).sendRawMessage(message);
        }
    }

    @Override
    public SpigotSender map(@NotNull SpigotSender sender) {
        return sender;
    }
}
