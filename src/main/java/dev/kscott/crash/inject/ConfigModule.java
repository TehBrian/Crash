package dev.kscott.crash.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.crash.config.Config;
import dev.kscott.crash.config.Lang;
import dev.kscott.crash.config.MenuConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides {@link Lang} and {@link Config} classes.
 */
public class ConfigModule extends AbstractModule {

    /**
     * @param plugin JavaPlugin reference.
     * @return a newly instantiated {@link Config}
     */
    @Provides
    @Singleton
    public @NonNull Config provideConfig(final @NonNull JavaPlugin plugin) {
        return new Config(plugin);
    }

    /**
     * @param plugin JavaPlugin reference.
     * @return a newly instantiated {@link Lang}
     */
    @Provides
    @Singleton
    public @NonNull Lang provideLang(final @NonNull JavaPlugin plugin) {
        return new Lang(plugin);
    }

    /**
     * @param plugin JavaPlugin reference.
     * @param lang   Lang reference.
     * @return a newly instantiated {@link MenuConfig}.
     */
    @Provides
    @Singleton
    public @NonNull MenuConfig provideMenuConfig(
            final @NonNull JavaPlugin plugin,
            final @NonNull Lang lang
    ) {
        return new MenuConfig(plugin, lang);
    }

}
