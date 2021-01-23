package dev.kscott.crash.command;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.kscott.crash.game.CrashProvider;
import dev.kscott.crash.game.GameManager;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CrashAdminCommand {

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
    public CrashAdminCommand(
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final @NonNull CrashProvider crashProvider,
            final @NonNull GameManager gameManager
    ) {
        this.commandManager = commandManager;
        this.gameManager = gameManager;
        this.crashProvider = crashProvider;

        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("crashadmin", "ca");

        this.commandManager.command(
                builder.literal("start")
                        .handler(this::handleStart)
                        .permission("crash.admin")
        );
    }

    /**
     * Handles the /crash main command.
     *
     * @param context command context.
     */
    private void handleStart(final @NonNull CommandContext<CommandSender> context) {
        this.gameManager.startGame();
    }


}
