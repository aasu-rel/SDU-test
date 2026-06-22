package com.stardashup.client.module.performance;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Simple stutter fix that optionally limits thread priorities of non-critical threads.
 */
public class StutterFix extends Module {

    private final BooleanSetting lowerBackgroundThreads = addSetting(new BooleanSetting("Lower BG Threads", "Lowers priority of chunk worker threads", true));

    public StutterFix() {
        super("Stutter Fix", "Attempts to smooth out micro-stutters", ModuleCategory.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        if (lowerBackgroundThreads.getValue()) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    @Override
    protected void onDisable() {
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
    }
}
