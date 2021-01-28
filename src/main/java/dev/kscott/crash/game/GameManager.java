package dev.kscott.crash.game;

import cloud.commandframework.paper.PaperCommandManager;
import dev.kscott.crash.config.Config;
import dev.kscott.crash.config.Lang;
import dev.kscott.crash.config.MenuConfig;
import dev.kscott.crash.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

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
     * Config reference.
     */
    private final @NonNull Config config;

    /**
     * MenuManager reference.
     */
    private final @NonNull MenuManager menuManager;

    /**
     * BetManager reference.
     */
    private final @NonNull BetManager betManager;

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
     * @param config        Config reference.
     * @param lang          Lang reference.
     */
    public GameManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull CrashProvider crashProvider,
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final @NonNull Config config,
            final @NonNull Lang lang,
            final @NonNull MenuConfig menuConfig
    ) {
        this.plugin = plugin;
        this.crashProvider = crashProvider;
        this.commandManager = commandManager;
        this.config = config;

        this.menuManager = new MenuManager(plugin, commandManager, this, config, lang, menuConfig);
        this.betManager = new BetManager(plugin, this, lang);

        this.currentMultiplier = 0;
        this.crashPoint = 0;

        this.gameState = GameState.NOT_RUNNING;

        if (this.config.isAutoStart()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    startGame();
                }
            }.runTaskLater(plugin, 1);
        }
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

        preGameCountdown = this.config.getCountdownTime();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (preGameCountdown <= 0) {
                    cancel();
                    runGame();
                }

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
                // thanks kevin u straight g
                if (crashPoint > currentMultiplier) {
                    currentMultiplier = Math.round((currentMultiplier + (currentMultiplier * config.getCrashSpeedMultiplier())) * 100.0) / 100.0;
                }

                if (crashPoint < currentMultiplier) {
                    payout();
                    betManager.newGame();
                    cancel();
                    runPostGame();
                }


                menuManager.updateMenus();
            }
        }.runTaskTimer(plugin, 0, config.getGameTick());
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
        }.runTaskLater(plugin, 20 * config.getPostGameTime());
    }

    /**
     * Pay out bets for players.
     */
    private void payout() {
        for (final @NonNull UUID uuid : this.betManager.getCashedOutPlayers()) {
            betManager.cashout(Bukkit.getOfflinePlayer(uuid));
        }
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
     * @return {@link this#betManager}
     */
    public @NonNull BetManager getBetManager() {
        return this.betManager;
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
