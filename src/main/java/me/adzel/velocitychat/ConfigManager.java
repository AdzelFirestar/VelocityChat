package me.adzel.velocitychat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

public final class ConfigManager {
    private static final String CONFIG_FILE_NAME = "config.yml";
    private final Path configPath;
    private final Logger logger;
    private final String pluginVersion;
    private String prefix = "&a[Staff] &r";

    public ConfigManager(Path dataDirectory, Logger logger, String pluginVersion) {
        this.configPath = dataDirectory.resolve(CONFIG_FILE_NAME);
        this.logger = logger;
        this.pluginVersion = pluginVersion;
    }

    public void load() {
        try {
            Files.createDirectories(configPath.getParent());
            if (!Files.exists(configPath)) {
                writeDefaultConfig();
            }

            Yaml yaml = new Yaml();
            Map<String, Object> data;
            try (var reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
                data = yaml.load(reader);
            }

            if (data == null) {
                data = new LinkedHashMap<>();
            }

            Object configuredPrefix = data.get("Prefix");
            if (configuredPrefix instanceof String configuredValue && !configuredValue.isBlank()) {
                this.prefix = configuredValue;
            }

            Object configuredVersion = data.get("Version");
            if (!(configuredVersion instanceof String version) || !pluginVersion.equals(version)) {
                data.put("Version", pluginVersion);
                if (!data.containsKey("Prefix")) {
                    data.put("Prefix", this.prefix);
                }
                writeConfig(data);
            }
        } catch (IOException ex) {
            logger.error("Failed to load config.yml, using defaults.", ex);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    private void writeDefaultConfig() throws IOException {
        List<String> lines = List.of(
            "# Plugin Version - DO NOT CHANGE",
            "Version: " + pluginVersion,
            "",
            "#Start of Configuration",
            "Prefix: \"&a[Staff] &r\""
        );
        Files.write(configPath, lines, StandardCharsets.UTF_8);
    }

    private void writeConfig(Map<String, Object> data) throws IOException {
        String version = String.valueOf(data.getOrDefault("Version", pluginVersion));
        String configuredPrefix = String.valueOf(data.getOrDefault("Prefix", prefix));
        List<String> lines = new ArrayList<>();
        lines.add("# Plugin Version - DO NOT CHANGE");
        lines.add("Version: " + version);
        lines.add("");
        lines.add("#Start of Configuration");
        lines.add("Prefix: \"" + configuredPrefix.replace("\"", "\\\"") + "\"");
        Files.write(configPath, lines, StandardCharsets.UTF_8);
    }
}
