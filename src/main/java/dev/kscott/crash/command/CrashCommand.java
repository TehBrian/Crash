package dev.kscott.crash.command;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.kscott.crash.game.CrashProvider;
import dev.kscott.crash.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Handles /crash commands.
 */
public class CrashCommand {

    /**
     * PaperCommandManager reference.
     */
    private final @NonNull PaperCommandManager<CommandSender> commandManager;

    /**
     * CrashProvider reference.
     */
    private final @NonNull CrashProvider crashProvider;

    /**
     * GameManager reference.
     */
    private final @NonNull GameManager gameManager;

    /**
     * Constructs CrashCommand.
     *
     * @param commandManager PaperCommandManager reference.
     */
    @Inject
    public CrashCommand(
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final @NonNull CrashProvider crashProvider,
            final @NonNull GameManager gameManager
            ) {
        this.commandManager = commandManager;
        this.gameManager = gameManager;
        this.crashProvider = crashProvider;

        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("crash");

        this.commandManager.command(
                builder.handler(this::handleCrash)
                        .permission("crash.command.use")
        );

        this.commandManager.command(
                builder.literal("start")
                        .handler(ctx -> this.gameManager.startGame())
                        .permission("crash.command.start")
        );
    }

    /**
     * Handles the /crash main command.
     *
     * @param context command context.
     */
    private void handleCrash(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();
        this.gameManager.getMenuManager().showGameMenu((Player) sender);
    }


}
