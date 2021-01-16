package dev.kscott.crash.game;

import cloud.commandframework.CommandManager;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Manages the game state.
 */
public class GameManager {

    /**
     * JavaPlugin reference.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * CrashProvider reference.
     */
    private final @NonNull CrashProvider crashProvider;

    /**
     * PaperCommandManager reference.
     */
    private final @NonNull PaperCommandManager<CommandSender> commandManager;

    /**
     * The state of the game. ({@link GameState})
     */
    private @NonNull GameState gameState;

    /**
     * Constructs GameManager.
     *
     * @param plugin        JavaPlugin reference.
     * @param crashProvider CrashProvider reference.
     */
    @Inject
    public GameManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull CrashProvider crashProvider,
            final @NonNull PaperCommandManager<CommandSender> commandManager
            ) {
        this.plugin = plugin;
        this.crashProvider = crashProvider;
        this.commandManager = commandManager;

        this.gameState = GameState.NOT_RUNNING;
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        if (this.gameState != GameState.NOT_RUNNING) {
            throw new RuntimeException("GameManager#startGame was called, but the game is already running!");
        }

        this.plugin.getLogger().info("Starting crash game.");

        runPreGame();
    }

    /**
     * Runs the pre-game.
     */
    private void runPreGame() {
        this.gameState = GameState.PRE_GAME;
        runGame();
    }

    /**
     * Runs the game.
     */
    private void runGame() {
        this.gameState = GameState.RUNNING;
        runPostGame();
    }

    /**
     * Runs the post-game.
     */
    private void runPostGame() {
        this.gameState = GameState.POST_GAME;
        runPreGame();
    }

    /**
     * Covers all possible game states.
     */
    public enum GameState {
        /**
         * The game is not running. (i.e. pre-start).
         */
        NOT_RUNNING,
        /**
         * The game is paused. (i.e. /crash admin pause).
         */
        PAUSED,
        /**
         * The game is about to start. (i.e. timer countdown before start).
         */
        PRE_GAME,
        /**
         * The game is running. (when the multiplier is counting up).
         */
        RUNNING,
        /**
         * The game just ended. (i.e. the few second pause after it crashes).
         */
        POST_GAME
    }


}
