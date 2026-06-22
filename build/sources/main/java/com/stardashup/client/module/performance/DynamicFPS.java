package com.stardashup.client.module.performance;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

/**
 * Reduces the framerate when the game is unfocused or the player is AFK.
 */
public class DynamicFPS extends Module {

    private final NumberSetting unfocusedFPS = addSetting(new NumberSetting("Unfocused FPS", "FPS limit when window is out of focus", 15, 1, 60, 1));
    private final NumberSetting afkTimeout = addSetting(new NumberSetting("AFK Timeout", "Seconds until considered AFK", 60, 30, 300, 10));
    private final NumberSetting afkFPS = addSetting(new NumberSetting("AFK FPS", "FPS limit when AFK", 30, 1, 60, 1));

    private int originalFPS = -1;
    private long lastInteractionTime;

    public DynamicFPS() {
        super("Dynamic FPS", "Lowers FPS when unfocused or AFK", ModuleCategory.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        lastInteractionTime = System.currentTimeMillis();
        originalFPS = -1;
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        restoreFPS();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        boolean isFocused = Display.isActive();
        
        // Update interaction time if mouse or keyboard is pressed
        if (org.lwjgl.input.Mouse.isButtonDown(0) || org.lwjgl.input.Mouse.isButtonDown(1) || org.lwjgl.input.Keyboard.getEventKeyState() || 
            mc.thePlayer.motionX != 0 || mc.thePlayer.motionZ != 0) {
            lastInteractionTime = System.currentTimeMillis();
        }

        boolean isAfk = (System.currentTimeMillis() - lastInteractionTime) > (afkTimeout.getIntValue() * 1000L);

        if (!isFocused) {
            applyFPSLimit(unfocusedFPS.getIntValue());
        } else if (isAfk) {
            applyFPSLimit(afkFPS.getIntValue());
        } else {
            restoreFPS();
        }
    }

    private void applyFPSLimit(int limit) {
        Minecraft mc = Minecraft.getMinecraft();
        if (originalFPS == -1) {
            originalFPS = mc.gameSettings.limitFramerate;
        }
        if (mc.gameSettings.limitFramerate != limit) {
            mc.gameSettings.limitFramerate = limit;
        }
    }

    private void restoreFPS() {
        if (originalFPS != -1) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.gameSettings.limitFramerate = originalFPS;
            originalFPS = -1;
        }
    }
}
