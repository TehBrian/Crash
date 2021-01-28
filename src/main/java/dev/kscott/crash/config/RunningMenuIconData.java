package dev.kscott.crash.config;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
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

    /**
     * @return the lore of this icon to display if the player viewing has a bet placed. If no lore was present, it will be an empty list.
     */
    public @NonNull List<Component> getDidBetLore() {
        return didBetLore == null ? new ArrayList<>() : new ArrayList<>(this.didBetLore);
    }
}
