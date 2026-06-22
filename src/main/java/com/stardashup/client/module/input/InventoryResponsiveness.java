package com.stardashup.client.module.input;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;

/**
 * Placeholder for Inventory Responsiveness.
 * <p>Requires Mixin into {@code GuiContainer} to modify the click delay handling.</p>
 */
public class InventoryResponsiveness extends Module {

    public InventoryResponsiveness() {
        super("Inventory Speed", "Improves click responsiveness in GUIs (Requires Mixin)", ModuleCategory.INPUT);
    }
}
