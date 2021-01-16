package dev.kscott.crash.inject;

import com.google.inject.AbstractModule;
import dev.kscott.crash.CrashPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides JavaPlugin and CrashPlugin.
 */
public class PluginModule extends AbstractModule {

    /**
     * CrashPlugin reference.
     */
    private final @NonNull CrashPlugin plugin;

    /**
     * Constructs PluginModule.
     *
     * @param plugin CrashPlugin reference.
     */
    public PluginModule(final @NonNull CrashPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Configures the {@link PluginModule} to bind {@link Plugin}, {@link JavaPlugin}, and {@link CrashPlugin} to {@link this#plugin)}.
     */
    @Override
    public void configure() {
        this.bind(Plugin.class).toInstance(plugin);
        this.bind(JavaPlugin.class).toInstance(plugin);
        this.bind(CrashPlugin.class).toInstance(plugin);
    }

}
