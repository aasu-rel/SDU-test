package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.module.hud.HUDModule;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

/**
 * Compass-style direction indicator.
 */
public class DirectionHUD extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));

    public DirectionHUD() {
        super("Direction", "Shows your facing direction", 2, 200);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        float yaw = mc.thePlayer.rotationYaw;
        int heading = (MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3);
        String dir = "";
        switch (heading) {
            case 0: dir = "S"; break;
            case 1: dir = "W"; break;
            case 2: dir = "N"; break;
            case 3: dir = "E"; break;
        }

        String text = dir + " (" + (int) MathHelper.wrapAngleTo180_float(yaw) + "\u00B0)";

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
