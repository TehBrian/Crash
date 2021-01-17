package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PausedMenu extends GameMenu {

    /**
     * Constructs a PausedMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public PausedMenu(final @NonNull Player player) {
        super(6, "Crash: Paused");

        final @NonNull StaticPane pane = new StaticPane(0, 0, 9, 6);

        pane.fillWith(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());

        addPane(pane);
    }


}
