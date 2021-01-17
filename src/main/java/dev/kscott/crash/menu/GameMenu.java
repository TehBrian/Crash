package dev.kscott.crash.menu;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class GameMenu extends ChestGui {
    /**
     * Constructs a new chest GUI
     *
     * @param rows  the amount of rows this gui should contain, in range 1..6.
     * @param title the title/name of this gui.
     * @since 0.8.0
     */
    public GameMenu(int rows, @NonNull String title) {
        super(rows, title);
    }
}
