package dev.kscott.crash.inject;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Provides CommandManager.
 */
public class CommandModule extends AbstractModule {

    /**
     * JavaPlugin reference.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * PaperCommandManager reference.
     */
    private final @NonNull PaperCommandManager<CommandSender> commandManager;

    /**
     * Constructs CommandModule.
     *
     * @param plugin JavaPlugin reference.
     */
    public CommandModule(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;

        try {
            final @NonNull Function<CommandSender, CommandSender> mapper = Function.identity();

            commandManager = new PaperCommandManager<>(
                    plugin,
                    AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                    mapper,
                    mapper
            );

            if (commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the CommandManager.");
        }
    }

    /**
     * Provides the PaperCommandManager.
     *
     * @return PaperCommandManager.
     */
    @Provides
    public PaperCommandManager<CommandSender> providePaperCommandManager() {
        return this.commandManager;
    }

    /**
     * Provides the CommandManager.
     *
     * @return CommandManager.
     */
    @Provides
    public CommandManager<CommandSender> provideCommandManager() {
        return this.commandManager;
    }

}
