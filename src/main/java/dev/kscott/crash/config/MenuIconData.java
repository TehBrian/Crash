package dev.kscott.crash.config;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.BaseComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores data for constructing an ItemStack from menu configuration.
 */
public class MenuIconData {

    /**
     * The material of this icon.
     */
    private final @NonNull Material material;

    /**
     * The name of this icon.
     */
    private final @NonNull Component name;

    /**
     * The lore of this icon.
     */
    private final @NonNull Component[] lore;

    /**
     * Constructs the MenuIconData.
     * <p>
     * Will add placeholder values to {@link this#name} and assign an empty array to {@link this#lore}.
     *
     * @param material {@link Material} of this icon.
     */
    public MenuIconData(
            final @NonNull Material material
    ) {
        this.material = material;
        this.name = Component.text("");
        this.lore = new Component[]{};
    }

    /**
     * Constructs the MenuIconData.
     * <p>
     * Will assign an empty array to {@link this#lore}.
     *
     * @param material {@link Material} of this icon.
     * @param name the name of this icon.
     */
    public MenuIconData(
            final @NonNull Material material,
            final @NonNull Component name
    ) {
        this.material = material;
        this.name = name;
        this.lore = new Component[]{};
    }

    /**
     * Constructs the MenuIconData.
     *
     * @param material the {@link Material} of this icon.
     * @param name the name of this icon.
     * @param lore the lore of this icon.
     */
    public MenuIconData(
            final @NonNull Material material,
            final @NonNull Component name,
            final @NonNull Component[] lore
    ) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    /**
     * @return the {@link Material} of this icon.
     */
    public @NonNull Material getMaterial() {
        return material;
    }

    /**
     * @return the name of this icon.
     */
    public @NonNull Component getName() {
        return name;
    }

    /**
     * @return the lore of this icon.
     */
    public @NonNull Component[] getLore() {
        return lore;
    }

    /**
     * @return an {@link ItemStack} constructed with this icon's values.
     */
    public @NonNull ItemStack getItemStack() {
        final @NonNull ComponentSerializer<Component, Component, BaseComponent[]> serializer = BungeeComponentSerializer.get();

        final @NonNull ItemStack itemStack = new ItemStack(material);
        final @NonNull ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
        final @NonNull BaseComponent[] nameComponents = serializer.serialize(this.name);
        final @NonNull List<BaseComponent[]> loreComponents = new ArrayList<>();

        for (final @NonNull Component component : this.lore) {
            loreComponents.add(serializer.serialize(component));
        }

        meta.setDisplayNameComponent(nameComponents);
        meta.setLoreComponents(loreComponents);

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
