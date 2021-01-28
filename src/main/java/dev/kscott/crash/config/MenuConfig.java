package dev.kscott.crash.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.file.Paths;
import java.util.*;

/**
 * Stores the GameMenu display configuration.
 */
public class MenuConfig {

    /**
     * The MenuIconData to use as a placeholder.
     */
    private final @NonNull MenuIconData placeholderIcon;

    /**
     * The MenuIconData to use as a placeholder.
     */
    private final @NonNull RunningMenuIconData runningPlaceholderIcon;

    /**
     * MiniMessage reference.
     */
    private final @NonNull MiniMessage miniMessage = MiniMessage.get();

    /**
     * A Map of Integer to MenuIconData, where Integer is the countdown second, and the MenuIconData is the icon to display for that second.
     */
    private final @NonNull Map<Integer, MenuIconData> preGameCountdownIcons;

    /**
     * A Map of Double to RunningMenuIconData, where Integer is the minimum multiplier to display icon at, and the MenuIconData is the icon to display.
     */
    private final @NonNull Map<Double, RunningMenuIconData> runningIcons;

    /**
     * Should the other bets list be displayed on the running menu?
     */
    private boolean runningOtherBetsList = true;

    /**
     * How many other bets should be displayed on the running menu?
     */
    private int runningOtherBetsAmount = 5;

    /**
     * Should the other bets list be displayed on the pre game menu?
     */
    private boolean preGameOtherBetsList = true;

    /**
     * How many other bets should be displayed on the pre game menu?
     */
    private int preGameOtherBetsAmount = 5;

    /**
     * The header to display for the other-bets-list.
     */
    private @MonotonicNonNull Component otherBetsListHeader;

    /**
     * The format to use to display a bet.
     */
    private @MonotonicNonNull Component otherBetsListFormat;

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
        this.placeholderIcon = new MenuIconData(Material.BARRIER, Component.text("Placeholder Icon"));
        this.runningPlaceholderIcon = new RunningMenuIconData(Material.BARRIER, Component.text("Placeholder Icon"), List.of(), List.of());
        this.runningIcons = new HashMap<>();

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
        } catch (final ConfigurateException e) {
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
        this.otherBetsListHeader = this.miniMessage.parse(this.root.node("other-bets-list").node("header").getString("")).decoration(TextDecoration.ITALIC, false);
        this.otherBetsListFormat = this.miniMessage.parse(this.root.node("other-bets-list").node("bet").getString("")).decoration(TextDecoration.ITALIC, false);
        final @NonNull ConfigurationNode countdownNode = this.root.node("pre-game-menu").node("countdown-icon");
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

                int keyNumber;

                try {
                    keyNumber = Integer.parseInt(key);
                } catch (final NumberFormatException e) {
                    this.plugin.getLogger().warning("Key " + key + " could not be parsed into an integer. Please review your configuration.");
                    continue;
                }

                final @NonNull ConfigurationNode iconNode = iconEntry.getValue();

                final @Nullable String materialString = iconNode.node("material").getString();

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
                } catch (final SerializationException e) {
                    this.plugin.getLogger().warning("Failed to parse " + key + "'s lore. Please review your configuration.");
                    this.plugin.getLogger().warning("Stacktrace (if you can't figure it out, send this to bluely):");
                    e.printStackTrace();
                }


                @Nullable Component nameComponent = null;
                @Nullable List<Component> loreComponent = null;

                if (name != null) {
                    nameComponent = this.miniMessage.parse(name).decoration(TextDecoration.ITALIC, false);
                }

                if (lore != null) {
                    loreComponent = new ArrayList<>();

                    for (final @NonNull String loreString : lore) {
                        loreComponent.add(miniMessage.parse(loreString).decoration(TextDecoration.ITALIC, false));
                    }
                }

                final @NonNull MenuIconData menuIconData = new MenuIconData(material, nameComponent, loreComponent);

                this.preGameCountdownIcons.put(keyNumber, menuIconData);
            }
        }

        // load running stuff
        this.runningOtherBetsList = this.root.node("running-menu").node("options").node("other-bets-list").node("enabled").getBoolean(true);
        this.runningOtherBetsAmount = this.root.node("running-menu").node("options").node("other-bets-list").node("amount").getInt(5);
        final @NonNull ConfigurationNode runningNode = this.root.node("running-menu").node("crash-icon");
        if (countdownNode.virtual()) {
            this.plugin.getLogger().warning("There are no crash icons loaded! You may want to review your configuration.");
        } else {
            for (final var iconEntry : runningNode.childrenMap().entrySet()) {
                final @NonNull Object keyObject = iconEntry.getKey();
                if (!(keyObject instanceof String)) {
                    this.plugin.getLogger().warning("Couldn't load key " + keyObject + ": key was not a String. Please review your configuration.");
                    continue;
                }

                final @NonNull String key = (String) keyObject;

                double keyNumber;

                try {
                    keyNumber = Double.parseDouble(key);
                } catch (final NumberFormatException e) {
                    this.plugin.getLogger().warning("Key " + key + " could not be parsed into a double. Please review your configuration.");
                    continue;
                }

                final @NonNull ConfigurationNode iconNode = iconEntry.getValue();

                final @Nullable String materialString = iconNode.node("material").getString();

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
                } catch (final SerializationException e) {
                    this.plugin.getLogger().warning("Failed to parse " + key + "'s lore. Please review your configuration.");
                    this.plugin.getLogger().warning("Stacktrace (if you can't figure it out, send this to bluely):");
                    e.printStackTrace();
                }

                @Nullable List<String> loreIfBet = null;

                try {
                    loreIfBet = iconNode.node("lore-if-bet").getList(String.class);
                } catch (final SerializationException e) {
                    this.plugin.getLogger().warning("Failed to parse " + key + "'s lore-if-bet. Please review your configuration.");
                    this.plugin.getLogger().warning("Stacktrace (if you can't figure it out, send this to bluely):");
                    e.printStackTrace();
                }


                @Nullable Component nameComponent = null;
                @Nullable List<Component> loreComponent = null;
                @Nullable List<Component> loreIfBetComponent = null;

                if (name != null) {
                    nameComponent = this.miniMessage.parse(name).decoration(TextDecoration.ITALIC, false);
                }

                if (lore != null) {
                    loreComponent = new ArrayList<>();

                    for (final @NonNull String loreString : lore) {
                        loreComponent.add(miniMessage.parse(loreString).decoration(TextDecoration.ITALIC, false));
                    }
                }

                if (loreIfBet != null) {
                    loreIfBetComponent = new ArrayList<>();
                }

                final @NonNull RunningMenuIconData menuIconData = new RunningMenuIconData(material, nameComponent, loreComponent, loreIfBetComponent);

                this.runningIcons.put(keyNumber, menuIconData);
            }
        }

    }

    /**
     * Returns a MenuIconData for the given integer.
     *
     * @param number Key of the MenuIconData. Seconds left on the pre game countdown.
     * @return MenuIconData. If the MenuIconData is not present for {@code number}, it will return a placeholder MenuIconData.
     */
    public @NonNull MenuIconData getPreGameIconData(final int number) {
        return this.preGameCountdownIcons.getOrDefault(number, placeholderIcon);
    }

    /**
     * Returns a RunningMenuIconData for the given multiplier value.
     *
     * @param multiplier Multiplier to get MenuIconData for.
     * @return RunningMenuIconData.
     */
    public @NonNull RunningMenuIconData getRunningIconData(final double multiplier) {
        final var entrySet = runningIcons.entrySet();
        final var entryList = new ArrayList<>(entrySet);
        entryList.sort((a, b) -> a.getKey() >= b.getKey() ? 1 : -1);
        Collections.reverse(entryList);

        for (final var entry : entryList) {
            double entryMultiplier = entry.getKey();
            if (entryMultiplier <= multiplier) {
                return entry.getValue();
            }
        }

        return runningPlaceholderIcon;
    }

    /**
     * Returns an ItemStack for a MenuIconData.
     *
     * @param iconData {@link MenuIconData} to get the ItemStack for.
     * @return ItemStack.
     */
    public @NonNull ItemStack getItemStack(final @NonNull MenuIconData iconData) {
        return iconData.getItemStack();
    }

    /**
     * @return The Component to display for the other bets list header.
     */
    public @NonNull Component getOtherBetsListHeader() {
        return otherBetsListHeader;
    }

    /**
     * @return The Component to use for formatting the other bets list.
     */
    public @NonNull Component getOtherBetsListFormat() {
        return otherBetsListFormat;
    }

    /**
     * @return true if the bet list is enabled for the pre game bet list, false if not.
     */
    public boolean isPreGameOtherBetsList() {
        return preGameOtherBetsList;
    }

    /**
     * @return how many other bets to display for the pre game bets list.
     */
    public int getPreGameOtherBetsAmount() {
        return preGameOtherBetsAmount;
    }

    /**
     * @return true if the bet list is enabled for the running bet list, false if not.
     */
    public boolean isRunningOtherBetsList() {
        return runningOtherBetsList;
    }

    /**
     * @return how many other bets to display for the running bets list.
     */
    public int getRunningOtherBetsAmount() {
        return runningOtherBetsAmount;
    }
}
