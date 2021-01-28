package dev.kscott.crash.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
    private final @Nullable Component name;

    /**
     * The lore of this icon.
     */
    private final @Nullable Component[] lore;

    /**
     * Constructs the MenuIconData.
     *
     * @param material {@link Material} of this icon.
     */
    public MenuIconData(
            final @NonNull Material material
    ) {
        this.material = material;
        this.name = null;
        this.lore = null;
    }

    /**
     * Constructs the MenuIconData.
     *
     * @param material {@link Material} of this icon.
     * @param name     the name of this icon.
     */
    public MenuIconData(
            final @NonNull Material material,
            final @Nullable Component name
    ) {
        this.material = material;
        this.name = name;
        this.lore = null;
    }

    /**
     * Constructs the MenuIconData.
     *
     * @param material the {@link Material} of this icon.
     * @param name     the name of this icon.
     * @param lore     the lore of this icon.
     */
    public MenuIconData(
            final @NonNull Material material,
            final @Nullable Component name,
            final @Nullable Component[] lore
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
    public @Nullable Component getName() {
        return name;
    }

    /**
     * @return the lore of this icon.
     */
    public @Nullable Component[] getLore() {
        return lore;
    }

    /**
     * @return an {@link ItemStack} constructed with this icon's values.
     */
    public @NonNull ItemStack getItemStack() {
        final @NonNull ComponentSerializer<Component, Component, BaseComponent[]> serializer = BungeeComponentSerializer.get();

        final @NonNull ItemStack itemStack = new ItemStack(material);

        if (this.name == null && this.lore == null) {
            return itemStack;
        }

        final @NonNull ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        if (this.name != null) {
            final @NonNull BaseComponent[] nameComponents = serializer.serialize(this.name);
            meta.setDisplayNameComponent(nameComponents);
        }

        if (this.lore != null && this.lore.length != 0) {
            final @NonNull List<BaseComponent[]> loreComponents = new ArrayList<>();

            for (final @Nullable Component component : this.lore) {
                if (component == null) continue;

                loreComponents.add(serializer.serialize(component));
            }

            meta.setLoreComponents(loreComponents);
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}