package dev.kscott.crash.menu;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.kscott.crash.game.GameManager;
import org.bukkit.command.CommandSender;
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

    private void createGameMenu() {

    }

}
