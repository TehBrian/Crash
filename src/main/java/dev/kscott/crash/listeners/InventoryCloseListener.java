package dev.kscott.crash.listeners;

import com.google.inject.Inject;
import dev.kscott.crash.menu.GameMenu;
import dev.kscott.crash.menu.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on the InventoryCloseEvent.
 */
public class InventoryCloseListener implements Listener {

    /**
     * MenuManager reference.
     */
    private final @NonNull MenuManager menuManager;

    /**
     * Constructs InventoryCloseListener.
     *
     * @param menuManager MenuManager reference.
     */
    @Inject
    public InventoryCloseListener(final @NonNull MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    /**
     * Removes the inventory from the MenuManager inventory list if it's a GameMenu.
     *
     * @param event InventoryCloseEvent.
     */
    @EventHandler
    public void onInventoryCloseEvent(final @NonNull InventoryCloseEvent event) {
        final @NonNull Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof GameMenu) {
            this.menuManager.inventoryClosed(inventory);
        }
    }

}
