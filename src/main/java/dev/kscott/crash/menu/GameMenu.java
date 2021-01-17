package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The base menu class for all crash game menus.
 */
public abstract class GameMenu extends ChestGui {
    /**
     * Constructs a new GameMenu
     */
    public GameMenu(int rows, @NonNull String title) {
        super(rows, title);
    }
}
