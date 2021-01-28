package dev.kscott.crash.config;

import cloud.commandframework.arguments.standard.IntegerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the GameMenu display configuration.
 */
public class MenuConfig {

    /**
     * MiniMessage reference.
     */
    private final @NonNull MiniMessage miniMessage = MiniMessage.get();

    /**
     * A Map of Integer to MenuIconData, where Integer is the countdown second, and the MenuIconData is the icon to display for that second.
     */
    private final @NonNull Map<Integer, MenuIconData> preGameCountdownIcons;

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
     * @param plugin {@link this#plugin}.
     */
    public MenuConfig(
            final @NonNull JavaPlugin plugin,
            final @NonNull Lang lang
    ) {
        this.plugin = plugin;
        this.preGameCountdownIcons = new HashMap<>();

        // Save config to file if it doesn't already exist
        // TODO: change this
        if (true) {
//        if (!new File(this.plugin.getDataFolder(), "menu.conf").exists()) {
            plugin.saveResource("menu.conf", true);
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
        // load pregame stuff
        this.preGameCountdownIcons.clear();
        this.preGameOtherBetsList = this.root.node("pre-game-menu").node("options").node("other-bets-list").node("enabled").getBoolean(true);
        this.preGameOtherBetsAmount = this.root.node("pre-game-menu").node("options").node("other-bets-list").node("amount").getInt(5);
        final @NonNull ConfigurationNode countdownNode = this.root.node("pre-game-menu").node("countdown-node");
        if (countdownNode.virtual()) {
            this.plugin.getLogger().warning("There are no countdown icons loaded! You may want to review your configuration.");
        } else {
            for (final var iconEntry : countdownNode.childrenMap().entrySet()) {
                final @NonNull Object keyObject = iconEntry.getKey();
                if (!(keyObject instanceof String)) {
                    this.plugin.getLogger().warning("Couldn't load key " + keyObject + ": key was not a String. Please review your configuration.");
                    continue;
                }

                final @NonNull String key = (String) keyObject;

                @Nullable Integer keyNumber = null;

                try {
                    keyNumber = Integer.parseInt(key);
                } catch (final @NonNull NumberFormatException e) {
                    this.plugin.getLogger().warning("Key " + key + " could not be parsed into an integer. Please review your configuration.");
                    continue;
                }

                final @NonNull ConfigurationNode iconNode = iconEntry.getValue();

                final @Nullable String materialString = iconNode.getString("material");

                if (materialString == null) {
                    this.plugin.getLogger().warning("Key " + key + " had no material value. This icon will not be loaded. Please review your configuration.");
                    continue;
                }

                final @Nullable Material material = Material.getMaterial(materialString);

                if (material == null) {
                    this.plugin.getLogger().warning("Material " + materialString + " could not be parsed into a valid material. Please review your configuration.");
                    continue;
                }
                final @Nullable String name = iconNode.node("name").getString();

                @Nullable List<String> lore = null;

                try {
                    lore = iconNode.node("lore").getList(String.class);
                } catch (final @NonNull SerializationException e) {
                    this.plugin.getLogger().warning("Failed to parse " + key + "'s lore. Please review your configuration.");
                    this.plugin.getLogger().warning("Stacktrace (if you can't figure it out, send this to bluely):");
                    e.printStackTrace();
                }


                @Nullable Component nameComponent = null;
                @Nullable List<Component> loreComponent = null;

                if (name != null) {
                    nameComponent = this.miniMessage.parse(name);
                }

                if (lore != null) {
                    loreComponent = new ArrayList<>();

                    for (final @NonNull String loreString : lore) {
                        loreComponent.add(miniMessage.parse(loreString));
                    }
                }

                final @NonNull MenuIconData menuIconData = new MenuIconData(material, nameComponent, loreComponent);

                this.preGameCountdownIcons.put(keyNumber, menuIconData);
            }
        }
    }

    /**
     * Returns a MenuIconData for the given integer.
     *
     * @param number Key of the MenuIconData. Seconds left on the pre game countdown.
     * @return MenuIconData. May be null.
     */
    public @Nullable MenuIconData getPreGameIconData(final int number) {
        return this.preGameCountdownIcons.get(number);
    }

}
