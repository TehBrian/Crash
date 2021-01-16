package dev.kscott.crash.inject;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.crash.game.CrashProvider;
import dev.kscott.crash.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GameModule extends AbstractModule {

    @Provides
    @Singleton
    public @NonNull CrashProvider provideCrashProvider() {
        return new CrashProvider();
    }

    @Provides
    @Singleton
    public @NonNull GameManager provideGameManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull CrashProvider crashProvider,
            final @NonNull PaperCommandManager<CommandSender> commandManager
    ) {
        return new GameManager(plugin, crashProvider, commandManager);
    }

}
