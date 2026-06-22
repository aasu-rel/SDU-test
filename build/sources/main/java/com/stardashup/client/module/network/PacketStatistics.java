package com.stardashup.client.module.network;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;

/**
 * Placeholder for Packet Statistics.
 * <p>Requires Mixin into {@code NetworkManager} to intercept packet read/write safely.</p>
 */
public class PacketStatistics extends Module {
    public PacketStatistics() {
        super("Packet Statistics", "Tracks network packets (Requires Mixin)", ModuleCategory.NETWORK);
    }
}
