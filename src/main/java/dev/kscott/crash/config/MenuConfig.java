package dev.kscott.crash.config;

import cloud.commandframework.arguments.CommandArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

/**
 * Stores the GameMenu display configuration.
 */
public class MenuConfig {

    /**
     * Should the other bets list be displayed on the pre game menu?
     */
    private boolean preGameOtherBetsList = true;

    /**
     * How many other bets should be displayed on the pre game menu?
     */
    private int preGameOtherBetsAmount = 5;

    /**
     * JavaPlugin reference.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * The root config.conf config node.
     */
    private @MonotonicNonNull CommentedConfigurationNode root;

    /**
     * Constructs the config, and loads it.
     *
     * @param plugin          {@link this#plugin}.
     */
    public MenuConfig(
            final @NonNull JavaPlugin plugin,
            final @NonNull Lang lang
    ) {
        this.plugin = plugin;

        // Save config to file if it doesn't already exist
        if (!new File(this.plugin.getDataFolder(), "menu.conf").exists()) {
            plugin.saveResource("menu.conf", false);
        }

        // Load the config
        this.loadConfig();
        this.loadConfigurationValues();
    }

    /**
     * Loads the config into the {@link this.root} node.
     */
    private void loadConfig() {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder().getAbsolutePath(), "menu.conf"))
                .build();

        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load the configuration.", e);
        }
    }

    /**
     * Loads Crash's configuration values.
     */
    private void loadConfigurationValues() {
    }

}
