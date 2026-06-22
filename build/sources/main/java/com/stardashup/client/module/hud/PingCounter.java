package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Displays the player's current ping to the server.
 */
public class PingCounter extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    private final BooleanSetting colorCode = addSetting(new BooleanSetting("Color Code", "Color code ping based on value", true));

    public PingCounter() {
        super("Ping", "Displays your server latency", 2, 14);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        
        int ping = 0;
        if (!mc.isSingleplayer() && mc.getNetHandler() != null && mc.thePlayer != null) {
            NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
            if (info != null) {
                ping = info.getResponseTime();
            }
        }
        
        String text = "Ping: " + (mc.isSingleplayer() ? "0" : ping) + " ms";

        int color = textColor.getValue();
        if (colorCode.getValue()) {
            if (ping < 50) color = 0xFF55FF55; // Green
            else if (ping < 150) color = 0xFFFFFF55; // Yellow
            else color = 0xFFFF5555; // Red
        }

        width = mc.fontRendererObj.getStringWidth(text) + 4;
        height = mc.fontRendererObj.FONT_HEIGHT + 4;

        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale.getFloatValue(), scale.getFloatValue(), 1);

        if (showBackground.getValue()) {
            RenderUtils.drawRect(0, 0, width, height, backgroundColor.getValue());
        }

        mc.fontRendererObj.drawStringWithShadow(text, 2, 2, color);
    }
}
