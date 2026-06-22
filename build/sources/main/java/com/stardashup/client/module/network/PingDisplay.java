package com.stardashup.client.module.network;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;

/**
 * Alias for the PingCounter HUD module.
 * <p>Included to fulfill the Network category requirements from the design doc.</p>
 */
public class PingDisplay extends Module {
    public PingDisplay() {
        super("Ping Display", "Alias for Ping HUD", ModuleCategory.NETWORK);
    }
}
