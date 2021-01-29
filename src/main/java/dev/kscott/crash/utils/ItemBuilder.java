package dev.kscott.crash.utils;

import com.google.common.collect.Lists;
import dev.kscott.crash.CrashPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ItemBuilder class.
 */
public class ItemBuilder {

    /**
     * ItemStack instance.
     */
    private final @NonNull ItemStack item;

    /**
     * ItemMeta instance.
     */
    private @Nullable ItemMeta meta;

    /**
     * Construct ItemBuilder with an ItemStack.
     *
     * @param item ItemStack to base the new item from
     */
    public ItemBuilder(final @NonNull ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }

    /**
     * Construct ItemBuilder with a Material.
     *
     * @param material creates an ItemStack and the builder from a material
     */
    public ItemBuilder(final @NonNull Material material) {
        this(new ItemStack(material));
    }

    /**
     * Build ItemStack from set properties.
     *
     * @return the built ItemStack
     */
    public @NonNull ItemStack build() {
        if (this.meta != null) {
            this.item.setItemMeta(this.meta);
        }

        return this.item;
    }

    /**
     * Set the quantity of the ItemStack.
     *
     * @param amount the ItemStacks quantity
     * @return the builder
     */
    public @NonNull ItemBuilder amount(final @NonNull Integer amount) {
        this.item.setAmount(amount);
        return this;
    }

    /**
     * Set the name of the ItemStack.
     *
     * @param name the ItemStacks display name
     * @return the builder
     */
    public @NonNull ItemBuilder name(final @Nullable String name) {
        if (this.meta == null) {
            this.meta = Bukkit.getItemFactory().getItemMeta(this.item.getType());
        }

        this.meta.setDisplayName(name);

        return this;
    }

    /**
     * Set the name of the ItemStack.
     *
     * @param component the ItemStack's display name
     * @return this builder
     */
    public @NonNull ItemBuilder name(final @NonNull Component component) {
        if (this.meta == null) {
            this.meta = Bukkit.getItemFactory().getItemMeta(this.item.getType());
        }

        ItemBuilder.setName(this.meta, component);

        return this;
    }

    /**
     * Set the Lore of the ItemStack.
     *
     * @param lines the lines to set the ItemStacks lore to
     * @return the builder
     */
    public @NonNull ItemBuilder lore(final @NonNull List<String> lines) {
        if (this.meta != null) {
            this.meta.setLore(lines);
        }

        return this;
    }

    /**
     * Set the Lore of the ItemStack.
     *
     * @param lines the lines to set the ItemStacks lore to
     * @return the builder
     */
    public @NonNull ItemBuilder loreComponent(final @NonNull List<Component> lines) {
        if (this.meta != null) {
            ItemBuilder.setLore(this.meta, lines);
        }

        return this;
    }

    /**
     * Add a line of lore to the ItemStack.
     *
     * @param lines the lines to add to the ItemStacks lore
     * @return the builder
     */
    public @NonNull ItemBuilder loreAdd(final @NonNull String... lines) {
        if (this.meta != null) {
            List<String> lore = this.meta.getLore();

            if (lore == null) {
                lore = new ArrayList<>();
            }

            lore.addAll(Arrays.asList(lines));
            this.meta.setLore(lore);
        }

        return this;
    }

    /**
     * Add a line of lore to the ItemStack.
     *
     * @param lines the lines to add to the ItemStacks lore
     * @return the builder
     */
    public @NonNull ItemBuilder loreAdd(final @NonNull Component... lines) {
        if (this.meta != null) {
            ItemBuilder.setLore(this.meta, Arrays.asList(lines));
        }

        return this;
    }

    /**
     * Add a line of lore to the ItemStack.
     *
     * @param lines the lines to add to the ItemStacks lore
     * @return the builder
     */
    public @NonNull ItemBuilder loreAdd(final @NonNull List<Component> lines) {
        if (this.meta != null) {
            ItemBuilder.setLore(meta, lines);
        }

        return this;
    }

    /**
     * Remove all lines of an ItemStacks lore.
     *
     * @return the builder
     */
    public @NonNull ItemBuilder clearLore() {
        if (this.meta != null) {
            this.meta.setLore(new ArrayList<>());
        }

        return this;
    }

    /**
     * Remove last line of lore from an ItemStack.
     *
     * @return the builder
     */
    public @NonNull ItemBuilder loreRemoveLast() {
        if (this.meta != null) {
            final List<String> lore = this.meta.getLore();

            if (lore != null && !lore.isEmpty()) {
                lore.remove(lore.size() - 1);
            }

            this.meta.setLore(lore);
        }

        return this;
    }

    /**
     * Add a flag to an ItemStack.
     *
     * @param flag the ItemFlag to add the the ItemStack
     * @return the builder
     */
    public @NonNull ItemBuilder flag(final @NonNull ItemFlag flag) {
        if (this.meta != null) {
            this.meta.addItemFlags(flag);
        }

        return this;
    }

    /**
     * Adds an Enchantment to an ItemStack.
     *
     * @param enchantment the Enchantment to add to the ItemStack
     * @param level       the Level of the Enchantment
     * @return the builder
     */
    public @NonNull ItemBuilder enchant(final @NonNull Enchantment enchantment, final int level) {
        if (this.meta != null) {
            this.meta.addEnchant(enchantment, level, true);
        }

        return this;
    }

    /**
     * Sets the lore of an ItemMeta.
     *
     * @param itemMeta   ItemMeta to apply lore to.
     * @param components List of Components to use when setting lore.
     */
    public static void setLore(final @NonNull ItemMeta itemMeta, final @NonNull List<Component> components) {
        final @NonNull List<String> lore = new ArrayList<>();

        for (final @NonNull Component component : components) {
            lore.add(LegacyComponentSerializer.legacySection().serialize(component));
        }

        itemMeta.setLore(lore);
    }

    /**
     * Gets the lore of an ItemMeta.
     *
     * @param itemMeta ItemMeta to get lore of.
     * @return A List of Components for the lore. May be empty if there was no lore on the ItemMeta.
     */
    public static @NonNull List<Component> getLore(final @NonNull ItemMeta itemMeta) {
        if (!itemMeta.hasLore()) {
            return new ArrayList<>();
        }

        final @NonNull List<String> lore = Objects.requireNonNull(itemMeta.getLore());

        final @NonNull List<Component> loreComponents = new ArrayList<>();

        if (lore.isEmpty()) {
            return new ArrayList<>();
        }

        for (final @NonNull String loreString : lore) {
            loreComponents.add(LegacyComponentSerializer.legacyAmpersand().deserialize(loreString));
        }

        return loreComponents;
    }

    /**
     * Sets the name of an ItemMeta.
     * @param itemMeta ItemMeta to set name for.
     * @param component Component to use as the display name.
     */
    public static void setName(final @NonNull ItemMeta itemMeta, final @NonNull Component component) {
        itemMeta.setDisplayName(LegacyComponentSerializer.legacySection().serialize(component));
    }

    /**
     * Gets the display name of an ItemMeta.
     * @param itemMeta ItemMeta to get display name.
     * @return
     */
    public static @NonNull Component getName(final @NonNull ItemMeta itemMeta) {
        if (itemMeta.hasDisplayName()) {
            return BungeeComponentSerializer.get().deserialize(itemMeta.getDisplayNameComponent());
        }

        return Component.text("");
    }
}
