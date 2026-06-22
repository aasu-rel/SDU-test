package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Tracks kills, deaths, and K/D ratio for the current session.
 */
public class SessionStats extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    
    private int kills = 0;
    private int deaths = 0;

    public SessionStats() {
        super("Session Stats", "Tracks session kills and deaths", 2, 50);
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
    public void onEntityDeath(LivingDeathEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Check if player died
        if (event.entityLiving == mc.thePlayer) {
            deaths++;
        }
        // Check if player killed someone (must be another player for PvP stats usually, but we count all entities here for simplicity, can be refined)
        else if (event.source.getEntity() == mc.thePlayer && event.entityLiving instanceof EntityPlayer) {
            kills++;
        }
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        
        double kd = deaths == 0 ? kills : (double) kills / deaths;
        String kdStr = String.format("%.2f", kd);

        String text = String.format("K: %d D: %d K/D: %s", kills, deaths, kdStr);

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
