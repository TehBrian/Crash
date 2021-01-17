package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PostGameMenu extends GameMenu {

    /**
     * Constructs a PostGameMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public PostGameMenu(final @NonNull Player player, final @NonNull GameManager gameManager) {
        super(6, "Crash");

        final @NonNull StaticPane bgPane = new StaticPane(0, 0, 9, 6);

        bgPane.fillWith(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());

        final @NonNull StaticPane fgPane = new StaticPane(0, 0, 9, 6);

        fgPane.addItem(new GuiItem(
                new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                        .name(Component.text("Crashed @ " + gameManager.getCrashPoint()+"x")
                                .decorate(TextDecoration.BOLD)
                                .color(NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false)
                        )
                        .build()
        ), 4, 2);

        addPane(fgPane);
        addPane(bgPane);
    }

}
