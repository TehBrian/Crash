package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.config.*;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

public class RunningMenu extends GameMenu {

    /**
     * Constructs a PreGameMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public RunningMenu(
            final @NonNull Player player,
            final @NonNull GameManager gameManager,
            final @NonNull Config config,
            final @NonNull Lang lang,
            final @NonNull MenuConfig menuConfig
    ) {
        super(6, lang.s("crash-inventory-title"));

        boolean playerDidBet = gameManager.getBetManager().didBet(player);

        @NonNull List<Component> lore;

        final @NonNull RunningMenuIconData iconData = menuConfig.getRunningIconData(gameManager.getCurrentMultiplier());

        @NonNull Component nameComponent = Objects.requireNonNullElse(iconData.getName(), Component.text(""));

        nameComponent = nameComponent.replaceText(TextReplacementConfig.builder().match("\\{multiplier\\}").replacement(Double.toString(gameManager.getCurrentMultiplier())).build());

        if (playerDidBet) {
            lore = new ArrayList<>();
            for (final @NonNull Component loreComponent : iconData.getDidBetLore()) {
                 lore.add(loreComponent.replaceText(TextReplacementConfig.builder().match("\\{multiplied-bet\\}").replacement(Lang.formatCurrency(gameManager.getBetManager().getCashout(player))).build()));
            }
        } else {
            lore = Objects.requireNonNullElse(iconData.getLore(), new ArrayList<>());
        }

        if (menuConfig.isRunningOtherBetsList()) {
            if (gameManager.getBetManager().getBets().size() != 0) {
                final @NonNull Component header = menuConfig.getOtherBetsListHeader();

                lore.add(header);

                final @NonNull Set<Map.Entry<UUID, Double>> betList = gameManager.getBetManager().getBets().entrySet();

                final @NonNull Iterator<Map.Entry<UUID, Double>> betListIterator = betList.iterator();

                int index = 0;

                while (betListIterator.hasNext()) {
                    if (index > menuConfig.getRunningOtherBetsAmount()) {
                        break;
                    }

                    final Map.Entry<UUID, Double> betEntry = betListIterator.next();

                    final @NonNull UUID uuid = betEntry.getKey();
                    final double bet = betEntry.getValue();

                    final @Nullable Player betPlayer = Bukkit.getPlayer(uuid);

                    if (betPlayer == null) {
                        continue;
                    }

                    final @NonNull String playerName = betPlayer.getName();

                    @NonNull Component betComponent = menuConfig.getOtherBetsListFormat();
                    betComponent = betComponent
                            .replaceText(TextReplacementConfig.builder().match("\\{player\\}").replacement(playerName).build())
                            .replaceText(TextReplacementConfig.builder().match("\\{bet\\}").replacement(lang.formatCurrency(bet)).build());

                    lore.add(betComponent);

                    index++;
                }
            }
        }

        final @NonNull ItemStack iconItemStack = MenuIconData.constructItemStack(iconData.getMaterial(), nameComponent, lore);

        final @NonNull StaticPane bgPane = new StaticPane(0, 0, 9, 6);

        bgPane.fillWith(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());

        final @NonNull StaticPane fgPane = new StaticPane(0, 0, 9, 6);

        fgPane.addItem(new GuiItem(iconItemStack, onClick -> {
            final @NonNull Player whoClicked = (Player) onClick.getWhoClicked();
            gameManager.getBetManager().cashout(whoClicked);
        }), 4, 2);


        addPane(fgPane);
        addPane(bgPane);
    }


}
