
package com.dokaibe.utils;

import com.dokaibe.DokaibePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class ChatIdentity {

    private final DokaibePlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ChatIdentity(DokaibePlugin plugin) {
        this.plugin = plugin;
    }

    private String prefix() {
        String icon = plugin.getConfig().getString("identity.icon", "✦");
        String name = plugin.getConfig().getString("identity.display-name", "DOKAIBE");
        String color = plugin.getConfig().getString("identity.primary-color", "#B78AFF");
        String prefix = plugin.getConfig().getString("identity.prefix", "[DOKAIBE AI]");

        return "<" + color + ">" + icon + " " + name + "</" + color + "> "
                + "<dark_gray>" + escape(prefix) + "</dark_gray>";
    }

    // Escape untrusted text so players cannot inject MiniMessage formatting.
    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("<", "\\<");
    }

    // Trusted messages may contain MiniMessage formatting.
    private Component format(String key, String fallback, String message) {
        String template = plugin.getConfig().getString(key, fallback);

        String rendered = template
                .replace("{prefix}", prefix())
                .replace("{message}", message);

        return mm.deserialize(rendered);
    }

    public Component player(String message) {
        return format(
                "identity.player-format",
                "{prefix} {message}",
                message
        );
    }

    public Component admin(String message) {
        return format(
                "identity.admin-format",
                "{prefix} <dark_gray>•</dark_gray> <#B78AFF>ADMIN</#B78AFF> {message}",
                message
        );
    }

    public Component owner(String message) {
        return format(
                "identity.owner-format",
                "{prefix} <dark_gray>•</dark_gray> <#D9A441>SUPREME FOUNDER</#D9A441> {message}",
                message
        );
    }

    public Component system(String message) {
        String template = plugin.getConfig().getString(
                "identity.system-format",
                "{prefix} {message}"
        );

        return mm.deserialize(
                template
                        .replace("{prefix}", prefix())
                        .replace("{message}", message)
        );
    }
}
