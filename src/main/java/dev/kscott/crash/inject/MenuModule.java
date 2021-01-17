package dev.kscott.crash.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.crash.game.GameManager;
import dev.kscott.crash.menu.MenuManager;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MenuModule extends AbstractModule {

    @Provides
    @Singleton
    public @NonNull MenuManager provideMenuManager(final @NonNull GameManager gameManager) {
        return gameManager.getMenuManager();
    }

}
