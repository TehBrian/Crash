package dev.kscott.crash.listeners;

import com.google.inject.Inject;
import dev.kscott.crash.menu.GameMenu;
import dev.kscott.crash.menu.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.checkerframework.checker.nullness.qual.NonNull;

public class InventoryCloseListener implements Listener {

    private final @NonNull MenuManager menuManager;

    @Inject
    public InventoryCloseListener(final @NonNull MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @EventHandler
    public void onInventoryCloseEvent(final @NonNull InventoryCloseEvent event) {
        final @NonNull Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof GameMenu) {
            this.menuManager.inventoryClosed(inventory);
        }
    }

}
