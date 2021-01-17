package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RunningMenu extends GameMenu {

    /**
     * Constructs a PreGameMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public RunningMenu(final @NonNull Player player, final @NonNull GameManager gameManager) {
        super(6, "Crash");

        boolean playerDidBet = gameManager.getBetManager().didBet(player);

        final @NonNull List<Component> loreList = new ArrayList<>();

        final @NonNull Style style = Style.style(NamedTextColor.GRAY)
                .decoration(TextDecoration.BOLD, false)
                .decoration(TextDecoration.ITALIC, false);

        if (playerDidBet) {
            loreList.add(Component.text("Click to cash out ").style(style)
                    .append(Component.text(gameManager.getBetManager().getCashout(player)).style(style.color(NamedTextColor.GOLD)))
                    .append(Component.text("!")).style(style));
        }

        final @NonNull StaticPane bgPane = new StaticPane(0, 0, 9, 6);

        bgPane.fillWith(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());


        final @NonNull StaticPane fgPane = new StaticPane(0, 0, 9, 6);

        fgPane.addItem(new GuiItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                        .name(Component.text(gameManager.getCurrentMultiplier()+"x")
                                .color(NamedTextColor.GREEN)
                                .decorate(TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, false)
                        )
                        .loreAdd(loreList)
                        .build()
        ), 4, 2);

        addPane(fgPane);
        addPane(bgPane);
    }


}
