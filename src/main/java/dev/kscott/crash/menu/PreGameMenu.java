package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.kscott.crash.CrashPlugin;
import dev.kscott.crash.config.Config;
import dev.kscott.crash.config.Lang;
import dev.kscott.crash.config.MenuConfig;
import dev.kscott.crash.config.MenuIconData;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

public class PreGameMenu extends GameMenu {

    /**
     * Constructs a PreGameMenu.
     *
     * @param player Player who's intended to view this inventory
     */
    public PreGameMenu(
            final @NonNull Player player,
            final @NonNull GameManager gameManager,
            final @NonNull Config config,
            final @NonNull Lang lang,
            final @NonNull MenuConfig menuConfig
    ) {
        super(6, lang.s("crash-inventory-title"));

        final int countdown = gameManager.getPreGameCountdown();

        final @NonNull MenuIconData menuIcon = menuConfig.getPreGameIconData(countdown);

        final @NonNull ItemStack itemStack = menuConfig.getItemStack(menuIcon);

        final @NonNull ItemMeta itemMeta = itemStack.getItemMeta();

        if (menuConfig.isPreGameOtherBetsList()) {
            @NonNull List<Component> lore = ItemBuilder.getLore(itemMeta);

            if (gameManager.getBetManager().getBets().size() != 0) {
                final @NonNull Component header = menuConfig.getOtherBetsListHeader();

                lore.add(header);

                final @NonNull Set<Map.Entry<UUID, Double>> betList = gameManager.getBetManager().getBets().entrySet();

                final @NonNull Iterator<Map.Entry<UUID, Double>> betListIterator = betList.iterator();

                int index = 0;

                while (betListIterator.hasNext()) {
                    if (index > menuConfig.getPreGameOtherBetsAmount()) {
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

            ItemBuilder.setLore(itemMeta, lore);

            itemStack.setItemMeta(itemMeta);
        }

        final @NonNull StaticPane bgPane = new StaticPane(0, 0, 9, 6);

        bgPane.fillWith(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build());

        final @NonNull StaticPane fgPane = new StaticPane(0, 0, 9, 6);

        fgPane.addItem(new GuiItem(itemStack), 4, 2);

        addPane(fgPane);
        addPane(bgPane);
    }


}
