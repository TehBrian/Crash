package dev.kscott.crash.menu;

import cloud.commandframework.paper.PaperCommandManager;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import dev.kscott.crash.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the creation and displaying of menus.
 */
public class MenuManager {

    /**
     * JavaPlugin reference.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * PaperCommandManager reference.
     */
    private final @NonNull PaperCommandManager<CommandSender> commandManager;

    /**
     * GameManager reference.
     */
    private final @NonNull GameManager gameManager;

    /**
     * A List of GameMenu that holds all opened menus.
     */
    private final @NonNull List<InventoryHolder> openInventories;

    /**
     * Constructs MenuManager.
     *
     * @param plugin         JavaPlugin reference.
     * @param commandManager PaperCommandManager reference.
     */
    public MenuManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final @NonNull GameManager gameManager
    ) {
        this.plugin = plugin;
        this.commandManager = commandManager;
        this.gameManager = gameManager;
        this.openInventories = new ArrayList<>();
    }

    public void updateMenus() {
        for (final @NonNull InventoryHolder holder : openInventories) {
            final @NonNull HumanEntity entity = holder.getInventory().getViewers().get(0);
            if (entity instanceof Player) {
                this.plugin.getServer().broadcast(((Player) entity).getDisplayName(), "");
                showGameMenu((Player) entity);
            }
        }
    }

    public void showGameMenu(final @NonNull Player player) {
        final @NonNull ChestGui menu = this.createGameMenu(player);

        this.openInventories.add(menu);

        menu.show(player);
    }

    private ChestGui createGameMenu(final @NonNull Player player) {
        final GameManager.GameState gameState = this.gameManager.getGameState();

        if (gameState == GameManager.GameState.PRE_GAME) {
            return new PreGameMenu(player, this.gameManager);
        } else if (gameState == GameManager.GameState.RUNNING) {
            return new RunningMenu(player, this.gameManager);
        } else if (gameState == GameManager.GameState.POST_GAME) {
            return new PostGameMenu(player, this.gameManager);
        }

        return new NotRunningMenu(player);
    }

    public void inventoryClosed(final @NonNull Inventory inventory) {
        this.openInventories.removeIf(menu -> menu.getInventory() == inventory);
    }

}
