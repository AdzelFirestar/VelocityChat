package me.adzel.velocitychat;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

@Plugin(
    id = "velocitychat",
    name = "VelocityChat",
    version = "0.1",
    authors = {"Adzel"}
)
public final class VelocityChatPlugin {
    public static final String PERMISSION = "velocitychat.sc";
    private static final LegacyComponentSerializer LEGACY_SERIALIZER =
        LegacyComponentSerializer.legacyAmpersand();

    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;
    private final Set<UUID> staffChatEnabled = ConcurrentHashMap.newKeySet();
    private ConfigManager configManager;

    @Inject
    public VelocityChatPlugin(ProxyServer proxyServer, Logger logger, @com.velocitypowered.api.plugin.annotation.DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(com.velocitypowered.api.event.proxy.ProxyInitializeEvent event) {
        this.configManager = new ConfigManager(dataDirectory, logger, getVersion());
        this.configManager.load();

        CommandManager commandManager = proxyServer.getCommandManager();
        commandManager.register(
            commandManager.metaBuilder("sc")
                .plugin(this)
                .build(),
            new StaffChatCommand(this)
        );
        logger.info("VelocityChat enabled.");
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!isStaffChatEnabled(player.getUniqueId())) {
            return;
        }
        if (!player.hasPermission(PERMISSION)) {
            staffChatEnabled.remove(player.getUniqueId());
            return;
        }

        event.setResult(PlayerChatEvent.ChatResult.denied());
        sendStaffMessage(player, event.getMessage());
    }

    public boolean toggleStaffChat(UUID playerId) {
        if (staffChatEnabled.contains(playerId)) {
            staffChatEnabled.remove(playerId);
            return false;
        }
        staffChatEnabled.add(playerId);
        return true;
    }

    public boolean isStaffChatEnabled(UUID playerId) {
        return staffChatEnabled.contains(playerId);
    }

    public Component prefixed(String message) {
        return LEGACY_SERIALIZER.deserialize(configManager.getPrefix() + message);
    }

    public void sendStaffMessage(Player sender, String message) {
        Component component = prefixed(sender.getUsername() + ": " + message);
        proxyServer.getAllPlayers().stream()
            .filter(player -> player.hasPermission(PERMISSION))
            .forEach(player -> player.sendMessage(component));
    }

    private String getVersion() {
        return this.getClass().getAnnotation(Plugin.class).version();
    }
}
