package dev.kscott.crash.game;

import com.google.inject.Inject;
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
     * Constructs GameManager.
     * @param plugin JavaPlugin reference.
     * @param crashProvider CrashProvider reference.
     */
    @Inject
    public GameManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull CrashProvider crashProvider
    ) {
        this.plugin = plugin;
        this.crashProvider = crashProvider;
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
