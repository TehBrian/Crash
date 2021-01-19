package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.config.Config;
import dev.kscott.crash.config.Lang;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PreGameMenu extends GameMenu {

    /**
     * Constructs a PreGameMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public PreGameMenu(
            final @NonNull Player player,
            final @NonNull GameManager gameManager,
            final @NonNull Config config
    ) {
        super(6, "Crash");

        final int countdown = gameManager.getPreGameCountdown();

        @NonNull Component text;
        @NonNull Material material;

        final @NonNull List<Component> lore = new ArrayList<>();

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

        if (config.isShowBetList()) {
            if (gameManager.getBetManager().getBets().size() != 0) {
                lore.add(
                        Component.text(" ".repeat(10))
                                .style(Style.style(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.STRIKETHROUGH))
                                .append(Component.text("BETS").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
                                .append(Component.text(" ".repeat(10)).style(Style.style(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.STRIKETHROUGH)))
                );

                final @NonNull Map<UUID, Double> betMap = gameManager.getBetManager().getBets();

                for (final var entry : betMap.entrySet()) {
                    final @NonNull UUID uuid = entry.getKey();
                    final double bet = entry.getValue();

                    final @Nullable Player betPlayer = Bukkit.getPlayer(uuid);

                    if (betPlayer == null) {
                        continue;
                    }

                    final @NonNull String playerName = betPlayer.getName();

                    lore.add(Component.text(playerName+": "+bet).color(NamedTextColor.AQUA));
                }
            }
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
