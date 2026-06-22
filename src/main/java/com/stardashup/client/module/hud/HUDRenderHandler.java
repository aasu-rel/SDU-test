package com.stardashup.client.module.hud;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.event.events.RenderHUDEvent;
import com.stardashup.client.core.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles rendering all enabled HUD modules by listening to Forge's RenderGameOverlayEvent.
 */
public class HUDRenderHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        // Only render on TEXT or ALL to avoid rendering multiple times per frame
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (mc.gameSettings.showDebugInfo) return; // Don't render over F3

        ScaledResolution sr = event.resolution;
        float partialTicks = event.partialTicks;

        // Fire internal event (optional, if any other systems want to hook in)
        SDUClient.getInstance().getEventBus().post(new RenderHUDEvent(sr, partialTicks));

        // Render enabled HUD modules
        if (SDUClient.getInstance().getModuleManager() != null) {
            for (Module module : SDUClient.getInstance().getModuleManager().getEnabledModules()) {
                if (module instanceof HUDModule) {
                    // Push/Pop matrix to ensure HUD modules don't mess up each other's scaling/positioning
                    net.minecraft.client.renderer.GlStateManager.pushMatrix();
                    try {
                        ((HUDModule) module).render(sr);
                    } catch (Exception e) {
                        SDUClient.getInstance().getLogger().error("Error rendering HUD module: " + module.getName(), e);
                    }
                    net.minecraft.client.renderer.GlStateManager.popMatrix();
                }
            }
        }
    }
}
