package dev.kscott.crash.command;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.LongArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.kscott.crash.exception.NotEnoughBalanceException;
import dev.kscott.crash.game.CrashProvider;
import dev.kscott.crash.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * Holds a List of String to be used for /crash command completions.
     */
    private final @NonNull List<String> commandCompletions;

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

        this.commandCompletions = new ArrayList<>();
        commandCompletions.add("history");
        commandCompletions.add("1");
        commandCompletions.add("2");
        commandCompletions.add("3");
        commandCompletions.add("4");
        commandCompletions.add("5");
        commandCompletions.add("6");
        commandCompletions.add("7");
        commandCompletions.add("8");
        commandCompletions.add("9");
        commandCompletions.add("10");

        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("crash");

        final @NonNull CommandArgument<CommandSender, String> argument = StringArgument.<CommandSender>newBuilder("argument")
                .asOptional()
                .withSuggestionsProvider((ctx, arg) -> commandCompletions)
                .build();

        this.commandManager.command(
                builder.handler(this::handleCrash)
                        .argument(argument)
                        .permission("crash.command.use")
        );
    }

    /**
     * Handles the /crash main command.
     *
     * @param context command context.
     */
    private void handleCrash(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            return;
        }

        final @NonNull Player player = (Player) sender;

        final @NonNull Optional<String> argumentOptional = context.getOptional("argument");

        if (argumentOptional.isEmpty()) {
            this.gameManager.getMenuManager().showGameMenu((Player) sender);
            return;
        }

        final @NonNull String argument = argumentOptional.get();

        if (argument.equals("history")) {
            // open history menu
            return;
        }

        try {
            final long bet = Long.parseLong(argument);

            this.gameManager.getBetManager().placeBet(player, bet);
        } catch (final @NonNull NumberFormatException ex) {
            sender.sendMessage("oopsy woopsy that isnt a number");
        } catch (final @NonNull NotEnoughBalanceException ex) {
            sender.sendMessage("u dont got that kinda straps g");
        }

    }


}
