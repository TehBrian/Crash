package dev.kscott.crash.game;

import dev.kscott.crash.config.Lang;
import dev.kscott.crash.exception.NotEnoughBalanceException;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * Manages interactions for player bets.
 */
public class BetManager {

    /**
     * Vault Economy API.
     */
    private final @NonNull Economy economy;

    /**
     * GameManager reference.
     */
    private final @NonNull GameManager gameManager;

    /**
     * The Map which stores the player bets.
     */
    private @NonNull HashMap<UUID, Double> betMap;

    /**
     * The Map which stores queued player bets.
     */
    private final @NonNull HashMap<UUID, Double> queuedBetMap;

    /**
     * The Map which stores cashed out bets.
     */
    private final @NonNull Map<UUID, Double> cashoutMap;

    /**
     * BukkitAudiences instance.
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Lang reference.
     */
    private final @NonNull Lang lang;

    /**
     * Constructs BetManager.
     *
     * @param gameManager GameManager reference.
     */
    public BetManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull GameManager gameManager,
            final @NonNull Lang lang
    ) {
        this.gameManager = gameManager;
        this.lang = lang;
        this.audiences = BukkitAudiences.create(plugin);
        this.betMap = new HashMap<>();
        this.queuedBetMap = new HashMap<>();
        this.cashoutMap = new HashMap<>();

        final @Nullable RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            throw new RuntimeException("The Vault Economy API could not be found!");
        }

        this.economy = rsp.getProvider();

    }

    /**
     * @return A map of bets where the key is a player UUID and the value is how much they bet.
     */
    public @NonNull Map<UUID, Double> getBets() {
        return new HashMap<>(this.betMap);
    }

    /**
     * Places a bet for a player and withdraws the bet from their account.
     *
     * @param player Player to place bet for.
     * @param bet    bet amount.
     * @throws NotEnoughBalanceException thrown if {@code player} does not have enough balance to place {@code bet}.
     */
    public void placeBet(final @NonNull Player player, final double bet) throws NotEnoughBalanceException {
        final double playerBalance = economy.getBalance(player);

        if (playerBalance >= bet) {
            economy.withdrawPlayer(player, bet);
            if (gameManager.getGameState() != GameManager.GameState.PRE_GAME) {
                this.queuedBetMap.put(player.getUniqueId(), bet);
                audiences.player(player).sendMessage(lang.c("bet-queued-message", Map.of("{money}", Lang.formatCurrency(bet))));
            } else {
                audiences.player(player).sendMessage(lang.c("bet-message", Map.of("{money}", Lang.formatCurrency(bet))));
                this.betMap.put(player.getUniqueId(), bet);
            }
        } else {
            throw new NotEnoughBalanceException(player, bet, playerBalance);
        }

    }

    /**
     * Gets a bet for a player.
     *
     * @param player Player to get bet for.
     * @return the amount the player bet.
     */
    public double getBet(final @NonNull OfflinePlayer player) {
        return this.betMap.getOrDefault(player.getUniqueId(), 0D);
    }

    /**
     * Removes a bet for a player
     *
     * @param player player to remove bet for
     */
    public void removeBet(final @NonNull OfflinePlayer player) {
        this.betMap.remove(player.getUniqueId());
    }


    /**
     * @param player Player to check.
     * @return true if the player has a bet placed, false if not.
     */
    public boolean didBet(final @NonNull OfflinePlayer player) {
        return this.betMap.containsKey(player.getUniqueId());
    }

    /**
     * @param player Player to check.
     * @return the value of their cashout at the current multiplier.
     */
    public double getCashout(final @NonNull OfflinePlayer player) {
        if (!this.didBet(player)) {
            return 0;
        }
        return Math.round(this.getBet(player) * this.gameManager.getCurrentMultiplier() * 100.0) / 100.0;
    }

    /**
     * Returns a Set of UUIDs of players who cashed out.
     */
    public Set<UUID> getCashedOutPlayers() {
        return this.cashoutMap.keySet();
    }

    /**
     * Removes all placed bets from the map.
     */
    public void reset() {
        this.cashoutMap.clear();
        this.betMap.clear();
    }

    /**
     * Runs methods for a new game.
     */
    public void newGame() {
        reset();
        this.betMap.putAll(queuedBetMap);
    }

    /**
     * Cashes out a player.
     * @param player Player to cashout.
     */
    public void cashout(final @NonNull OfflinePlayer player) {
        if (this.didBet(player)) {
            final double cashout = getCashout(player);
            if (cashout != 0) {
                economy.depositPlayer(player, cashout);
                if (player.isOnline()) {
                    audiences.player((Player) player).sendMessage(lang.c("cashout-message", Map.of("{money}", Lang.formatCurrency(cashout))));
                }
            }
        }
    }

}
