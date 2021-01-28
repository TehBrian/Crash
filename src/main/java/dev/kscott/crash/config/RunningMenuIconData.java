package dev.kscott.crash.config;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class RunningMenuIconData extends MenuIconData {

    private final @Nullable List<Component> didBetLore;

    public RunningMenuIconData(
            final @NonNull Material material,
            final @Nullable Component name,
            final @Nullable List<Component> lore,
            final @Nullable List<Component> didBetLore
    ) {
        super(material, name, lore);
        this.didBetLore = didBetLore;
    }

    public @Nullable List<Component> getDidBetLore() {
        return this.didBetLore;
    }
}
