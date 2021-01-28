package dev.kscott.crash;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.crash.command.CrashAdminCommand;
import dev.kscott.crash.command.CrashCommand;
import dev.kscott.crash.inject.CommandModule;
import dev.kscott.crash.inject.ConfigModule;
import dev.kscott.crash.inject.GameModule;
import dev.kscott.crash.inject.PluginModule;
import dev.kscott.crash.listeners.InventoryCloseListener;
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
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            throw new RuntimeException("The Vault plugin is not installed. Please install it!");
        }

        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this),
                new CommandModule(this),
                new ConfigModule(),
                new GameModule()
        );

        injector.getInstance(CrashCommand.class);
        injector.getInstance(CrashAdminCommand.class);

        this.getServer().getPluginManager().registerEvents(injector.getInstance(InventoryCloseListener.class), this);
    }
}
