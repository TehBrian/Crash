package dev.kscott.crash.inject;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.crash.game.BetManager;
import dev.kscott.crash.game.CrashProvider;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.menu.MenuManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides objects related to the crash game.
 */
public class GameModule extends AbstractModule {

    /**
     * @return the {@link CrashProvider}.
     */
    @Provides
    @Singleton
    public @NonNull CrashProvider provideCrashProvider() {
        return new CrashProvider();
    }

    /**
     * @param plugin JavaPlugin reference.
     * @param crashProvider CrashProvider reference ({@link this#provideCrashProvider()}.
     * @param commandManager PaperCommandManager reference.
     * @return the {@link GameManager}.
     */
    @Provides
    @Singleton
    public @NonNull GameManager provideGameManager(
            final @NonNull JavaPlugin plugin,
            final @NonNull CrashProvider crashProvider,
            final @NonNull PaperCommandManager<CommandSender> commandManager
    ) {
        return new GameManager(plugin, crashProvider, commandManager);
    }

    /**
     * @param gameManager GameManager reference.
     * @return {@link MenuManager}.
     */
    @Provides
    @Singleton
    public @NonNull MenuManager provideMenuManager(final @NonNull GameManager gameManager) {
        return gameManager.getMenuManager();
    }

    /**
     * @param gameManager GameManager reference.
     * @return {@link BetManager}.
     */
    @Provides
    @Singleton
    public @NonNull BetManager provideBetManager(final @NonNull GameManager gameManager) {
        return gameManager.getBetManager();
    }


}
