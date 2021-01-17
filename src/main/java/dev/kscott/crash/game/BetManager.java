package dev.kscott.crash.game;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages interactions for player bets.
 */
public class BetManager {

    /**
     * The Map which stores the player bets.
     */
    private final @NonNull Map<UUID, Long> betMap;

    /**
     * Constructs BetManager.
     */
    public BetManager() {
        this.betMap = new HashMap<>();
    }

    /**
     * Places a bet for a player.
     * @param player Player to place bet for.
     * @param bet bet amount.
     */
    public void placeBet(final @NonNull Player player, final long bet) {
        this.betMap.put(player.getUniqueId(), bet);
    }

    /**
     * Gets a bet for a player.
     * @param player Player to get bet for.
     * @return the amount the player bet.
     */
    public long getBet(final @NonNull Player player) {
        return this.betMap.getOrDefault(player.getUniqueId(), 0L);
    }

    /**
     * Removes a bet for a player
     * @param player player to remove bet for
     */
    public void removeBet(final @NonNull Player player) {
        this.betMap.remove(player.getUniqueId());
    }

    /**
     * Removes all placed bets from the map.
     */
    public void reset() {
        betMap.clear();
    }



}
