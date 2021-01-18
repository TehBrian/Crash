package dev.kscott.crash.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.nio.file.Paths;

/**
 * Stores the Quantum configuration and handles the loading and registration of rulesets
 */
public class Config {

    /**
     * How many other bets to display on the crash icon?
     */
    private int otherPlayersListAmount = 5;

    /**
     * How long will the post-game menu run?
     */
    private int countdownTime = 10;

    /**
     * How long will the post-game menu run?
     */
    private int postGameTime = 5;

    /**
     * How often will the crash game tick? (in Minecrft ticks)
     */
    private int gameTick = 5;

    /**
     * How fast will the multiplier increase?
     */
    private double crashSpeedMultiplier = 0.03;

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * The root quantum.conf config node
     */
    private @MonotonicNonNull CommentedConfigurationNode root;

    /**
     * Constructs the config, and loads it.
     *
     * @param plugin          {@link this#plugin}
     */
    public Config(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;

        // Save config to file if it doesn't already exist
        if (!new File(this.plugin.getDataFolder(), "config.conf").exists()) {
            plugin.saveResource("config.conf", false);
        }

        // Load the config
        this.loadConfig();
        this.loadConfigurationValues();
    }

    /**
     * Loads the config into the {@link this.root} node
     */
    private void loadConfig() {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder().getAbsolutePath(), "config.conf"))
                .build();

        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load the configuration.", e);
        }
    }

    /**
     * Loads Crash's configuration values
     */
    private void loadConfigurationValues() {
    }

    public int getOtherPlayersListAmount() {
        return otherPlayersListAmount;
    }

    public int getCountdownTime() {
        return countdownTime;
    }

    public int getPostGameTime() {
        return postGameTime;
    }

    public int getGameTick() {
        return gameTick;
    }

    public double getCrashSpeedMultiplier() {
        return crashSpeedMultiplier;
    }


}
