package dev.kscott.crash.menu;

import cloud.commandframework.paper.PaperCommandManager;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.inject.Inject;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

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
     * Constructs MenuManager.
     *
     * @param plugin         JavaPlugin reference.
     * @param commandManager PaperCommandManager reference.
     */
    @Inject
    public MenuManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final @NonNull GameManager gameManager
    ) {
        this.plugin = plugin;
        this.commandManager = commandManager;
        this.gameManager = gameManager;
    }

    private Gui createGameMenu(final @NonNull Player player) {
        final GameManager.GameState gameState = this.gameManager.getGameState();

        if (gameState == GameManager.GameState.NOT_RUNNING) {
            return createNotRunningGui(player);
        }

        return null;
    }

    /**
     * Creates the {@link Gui} for {@link dev.kscott.crash.game.GameManager.GameState#NOT_RUNNING}.
     * @return the {@link Gui}.
     */
    private Gui createNotRunningGui(final @NonNull Player player) {
        final @NonNull ChestGui chestGui = new ChestGui(6, "Crash: not running");

        final @NonNull StaticPane pane = new StaticPane(0, 0, 9, 6);
        pane.fillWith(
                new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .name("")
                        .build()
        );

        chestGui.addPane(pane);

        return chestGui;
    }

}
