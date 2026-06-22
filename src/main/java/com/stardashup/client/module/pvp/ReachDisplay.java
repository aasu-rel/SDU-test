package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.module.hud.HUDModule;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Displays the reach distance of your last hit.
 */
public class ReachDisplay extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    
    private double lastReach = 0.0;
    private long lastHitTime = 0;

    public ReachDisplay() {
        super("Reach Display", "Shows distance of last hit", 2, 86);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.entityPlayer == mc.thePlayer && event.target != null) {
            // Note: This is an approximation. True reach accounts for hitboxes.
            lastReach = mc.thePlayer.getDistanceToEntity(event.target);
            lastHitTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();

        // Fade out after 2 seconds
        if (System.currentTimeMillis() - lastHitTime > 2000) {
            lastReach = 0.0;
        }

        String text = String.format("Reach: %.2f blocks", lastReach);

        width = mc.fontRendererObj.getStringWidth(text) + 4;
        height = mc.fontRendererObj.FONT_HEIGHT + 4;

        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale.getFloatValue(), scale.getFloatValue(), 1);

        if (showBackground.getValue()) {
            RenderUtils.drawRect(0, 0, width, height, backgroundColor.getValue());
        }

        mc.fontRendererObj.drawStringWithShadow(text, 2, 2, textColor.getValue());
    }
}
