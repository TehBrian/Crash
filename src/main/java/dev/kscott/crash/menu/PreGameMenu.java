package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PreGameMenu extends GameMenu {

    /**
     * Constructs a PreGameMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public PreGameMenu(
            final @NonNull Player player,
            final @NonNull GameManager gameManager
    ) {
        super(6, "Crash");

        final int countdown = gameManager.getPreGameCountdown();

        @NonNull Component text;
        @NonNull Material material;

        switch (countdown) {
            case 1: {
                material = Material.RED_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 1 second...").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            case 2: {
                material = Material.RED_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 2 seconds...").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            case 3: {
                material = Material.RED_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 3 seconds...").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            case 4: {
                material = Material.ORANGE_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 4 seconds...").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            case 5: {
                material = Material.YELLOW_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 5 seconds...").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            case 6: {
                material = Material.YELLOW_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 6 seconds...").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            case 7: {
                material = Material.LIME_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 7 seconds...").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);

            }
            break;
            case 8: {
                material = Material.GREEN_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 8 seconds...").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);

            }
            break;
            case 9: {
                material = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 9 seconds...").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);

            }
            break;
            case 10: {
                material = Material.BLUE_STAINED_GLASS_PANE;
                text = Component.text("Game starting in 10 seconds...").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
            default: {
                material = Material.GRAY_STAINED_GLASS_PANE;
                text = Component.text("Game starting in ??? seconds...").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
            }
            break;
        }

        final @NonNull StaticPane bgPane = new StaticPane(0, 0, 9, 6);

        bgPane.fillWith(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());

        final @NonNull StaticPane fgPane = new StaticPane(0, 0, 9, 6);

        fgPane.addItem(new GuiItem(
                new ItemBuilder(material)
                        .name(text)
                        .build()
        ), 4, 2);

        addPane(fgPane);
        addPane(bgPane);
    }


}
