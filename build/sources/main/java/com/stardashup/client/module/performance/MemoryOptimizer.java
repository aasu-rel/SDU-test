package com.stardashup.client.module.performance;

import com.stardashup.client.core.log.SDULogger;
import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.core.module.setting.NumberSetting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Periodically hints the JVM to run Garbage Collection when memory is high.
 */
public class MemoryOptimizer extends Module {

    private final NumberSetting checkInterval = addSetting(new NumberSetting("Interval (s)", "Seconds between memory checks", 60, 10, 300, 5));
    private final NumberSetting threshold = addSetting(new NumberSetting("Threshold (%)", "Memory usage percentage to trigger cleanup", 85, 50, 95, 5));
    private final BooleanSetting aggressive = addSetting(new BooleanSetting("Aggressive", "Call System.gc() immediately", false));

    private long lastCheckTime;

    public MemoryOptimizer() {
        super("Memory Optimizer", "Periodically cleans up unused memory", ModuleCategory.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        lastCheckTime = System.currentTimeMillis();
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        long now = System.currentTimeMillis();
        if (now - lastCheckTime >= checkInterval.getIntValue() * 1000L) {
            lastCheckTime = now;

            Runtime rt = Runtime.getRuntime();
            long max = rt.maxMemory();
            long total = rt.totalMemory();
            long free = rt.freeMemory();
            long used = total - free;

            double usedPercentage = ((double) used / max) * 100.0;

            if (usedPercentage >= threshold.getIntValue()) {
                SDULogger.staticDebug("Memory threshold reached: " + String.format("%.1f%%", usedPercentage) + ". Suggesting GC.");
                if (aggressive.getValue()) {
                    System.gc(); // Suggest immediate garbage collection
                } else {
                    // Less aggressive: clear out Minecraft's internal caches if needed, or just standard GC
                    System.gc(); 
                }
            }
        }
    }
}
