package dev.kscott.crash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.crash.inject.CommandModule;
import dev.kscott.crash.inject.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The CrashPlugin main class.
 */
public final class CrashPlugin extends JavaPlugin {

    /**
     * Constructs the injector and initializes some stuff.
     */
    @Override
    public void onEnable() {
        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this),
                new CommandModule(this)
        );
    }
}
