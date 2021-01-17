package dev.kscott.crash.game;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages interactions for player bets.
 */
public class BetManager {

    /**
     * GameManager reference.
     */
    private final @NonNull GameManager gameManager;

    /**
     * The Map which stores the player bets.
     */
    private final @NonNull Map<UUID, Double> betMap;

    /**
     * Constructs BetManager.
     * @param gameManager GameManager reference.
     */
    public BetManager(final @NonNull GameManager gameManager) {
        this.gameManager = gameManager;
        this.betMap = new HashMap<>();
    }

    /**
     * Places a bet for a player.
     *
     * @param player Player to place bet for.
     * @param bet    bet amount.
     */
    public void placeBet(final @NonNull Player player, final double bet) {
        this.betMap.put(player.getUniqueId(), bet);
    }

    /**
     * Gets a bet for a player.
     *
     * @param player Player to get bet for.
     * @return the amount the player bet.
     */
    public double getBet(final @NonNull Player player) {
        return this.betMap.getOrDefault(player.getUniqueId(), 0D);
    }

    /**
     * Removes a bet for a player
     *
     * @param player player to remove bet for
     */
    public void removeBet(final @NonNull Player player) {
        this.betMap.remove(player.getUniqueId());
    }


    /**
     * @param player Player to check.
     * @return true if the player has a bet placed, false if not.
     */
    public boolean didBet(final @NonNull Player player) {
        return this.betMap.containsKey(player.getUniqueId());
    }

    /**
     * @param player Player to check.
     * @return the value of their cashout at the current multiplier.
     */
    public double getCashout(final @NonNull Player player) {
        if (!this.didBet(player)) {
            return 0;
        }

        return BigDecimal.valueOf(this.getBet(player) * this.gameManager.getCurrentMultiplier()).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
    }

    /**
     * Removes all placed bets from the map.
     */
    public void reset() {
        betMap.clear();
    }


}
