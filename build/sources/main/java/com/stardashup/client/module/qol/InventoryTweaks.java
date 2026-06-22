package com.stardashup.client.module.qol;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;

/**
 * Inventory QoL tweaks.
 * <p>Requires GuiScreen / GuiContainer hooks to add buttons.</p>
 */
public class InventoryTweaks extends Module {

    public InventoryTweaks() {
        super("Inventory Tweaks", "Adds sorting and search to inventories (Requires Mixin)", ModuleCategory.QOL);
    }
}
