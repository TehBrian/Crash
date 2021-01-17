package dev.kscott.crash.game;

import cloud.commandframework.paper.PaperCommandManager;
import dev.kscott.crash.menu.MenuManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Manages the game state.
 */
public class GameManager {

    /**
     * How long will the pre-game countdown take? (in seconds)
     */
    private static final int COUNTDOWN_TIME = 10;

    /**
     * How long will the post-game menu run?
     */
    public static final int POST_GAME_TIME = 5;

    /**
     * How often will the crash game tick? (in Minecrft ticks)
     */
    private static final int GAME_TICK = 5;

    /**
     * How fast will the multiplier increase?
     */
    private static final double CRASH_SPEED_MULTIPLIER = 0.02;

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
     * MenuManager reference.
     */
    private final @NonNull MenuManager menuManager;

    /**
     * The state of the game. ({@link GameState})
     */
    private @NonNull GameState gameState;

    /**
     * The current crash multiplier.
     */
    private double currentMultiplier;

    /**
     * The crash point.
     */
    private double crashPoint;

    /**
     * The countdown variable for pre-game.
     */
    private int preGameCountdown;

    /**
     * Constructs GameManager.
     *
     * @param plugin        JavaPlugin reference.
     * @param crashProvider CrashProvider reference.
     */
    public GameManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull CrashProvider crashProvider,
            final @NonNull PaperCommandManager<CommandSender> commandManager
    ) {
        this.plugin = plugin;
        this.crashProvider = crashProvider;
        this.commandManager = commandManager;

        this.menuManager = new MenuManager(plugin, commandManager, this);

        this.currentMultiplier = 0;
        this.crashPoint = 0;

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

        preGameCountdown = COUNTDOWN_TIME;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (preGameCountdown <= 0) {
                    cancel();
                    runGame();
                }

                plugin.getServer().broadcast(""+preGameCountdown, "");

                menuManager.updateMenus();
                preGameCountdown--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Runs the game.
     */
    private void runGame() {
        this.gameState = GameState.RUNNING;

        this.currentMultiplier = 1;
        this.crashPoint = this.crashProvider.generateCrashPoint();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (crashPoint > currentMultiplier) {
                    currentMultiplier = BigDecimal.valueOf(currentMultiplier + (currentMultiplier * CRASH_SPEED_MULTIPLIER)).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                } else {
                    // game crashed
                    cancel();
                    runPostGame();
                }

                menuManager.updateMenus();
            }
        }.runTaskTimer(plugin, 0, GAME_TICK);
    }

    /**
     * Runs the post-game.
     */
    private void runPostGame() {
        this.gameState = GameState.POST_GAME;

        this.menuManager.updateMenus();

        new BukkitRunnable() {
            @Override
            public void run() {
                runPreGame();
            }
        }.runTaskLater(plugin, 20 * POST_GAME_TIME);
    }

    /**
     * @return the {@link GameState}.
     */
    public @NonNull GameState getGameState() {
        return this.gameState;
    }

    /**
     * @return {@link this#menuManager}
     */
    public @NonNull MenuManager getMenuManager() {
        return this.menuManager;
    }

    /**
     * @return {@link this#preGameCountdown}
     */
    public int getPreGameCountdown() {
        return this.preGameCountdown;
    }

    /**
     * @return {@link this#currentMultiplier}
     */
    public double getCurrentMultiplier() {
        return currentMultiplier;
    }

    /**
     * @return {@link this#crashPoint}
     */
    public double getCrashPoint() {
        return crashPoint;
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
