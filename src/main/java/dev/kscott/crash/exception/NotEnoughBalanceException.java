package dev.kscott.crash.exception;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an exception thrown when the user doesn't have enough balance.
 */
public class NotEnoughBalanceException extends Exception {

    /**
     * The player who caused this exception.
     */
    private final @NonNull Player player;

    /**
     * The amount that was attempted to be withdrawn from the player.
     */
    private final double attemptedWithdraw;

    /**
     * How much was in the player's balance when this exception was thrown.
     */
    private final double playerBalance;

    /**
     * Constructs NotEnoughBalanceException.
     *
     * @param player            {@link this#player}.
     * @param attemptedWithdraw {@link this#attemptedWithdraw}.
     * @param playerBalance     {@link this#playerBalance}.
     */
    public NotEnoughBalanceException(
            final @NonNull Player player,
            final double attemptedWithdraw,
            final double playerBalance
    ) {
        this.player = player;
        this.attemptedWithdraw = attemptedWithdraw;
        this.playerBalance = playerBalance;
    }

    /**
     * @return the player who caused this exception.
     */
    public @NonNull Player getPlayer() {
        return player;
    }

    /**
     * @return the amount that was attempted to be withdrawn from the player's account.
     */
    public double getAttemptedWithdraw() {
        return attemptedWithdraw;
    }

    /**
     * @return the player's balance when this exception was thrown.
     */
    public double getPlayerBalance() {
        return playerBalance;
    }
}
