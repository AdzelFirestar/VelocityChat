package me.adzel.velocitychat;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

public final class StaffChatCommand implements SimpleCommand {
    private final VelocityChatPlugin plugin;

    public StaffChatCommand(VelocityChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(plugin.prefixed("Only players can use this command."));
            return;
        }

        if (!player.hasPermission(VelocityChatPlugin.PERMISSION)) {
            player.sendMessage(plugin.prefixed("You do not have permission to use staff chat."));
            return;
        }

        boolean enabled = plugin.toggleStaffChat(player.getUniqueId());
        if (enabled) {
            player.sendMessage(plugin.prefixed("Staff chat mode enabled."));
        } else {
            player.sendMessage(plugin.prefixed("Staff chat mode disabled."));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(VelocityChatPlugin.PERMISSION);
    }
}
