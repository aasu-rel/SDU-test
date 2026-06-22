package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

/**
 * Displays player coordinates and facing direction.
 */
public class CoordinatesHUD extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    private final BooleanSetting showFacing = addSetting(new BooleanSetting("Show Facing", "Show direction facing", true));

    public CoordinatesHUD() {
        super("Coordinates", "Displays your X, Y, Z coordinates", 2, 26);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        int x = (int) mc.thePlayer.posX;
        int y = (int) mc.thePlayer.posY;
        int z = (int) mc.thePlayer.posZ;

        String text = String.format("X: %d Y: %d Z: %d", x, y, z);
        
        if (showFacing.getValue()) {
            EnumFacing facing = mc.thePlayer.getHorizontalFacing();
            String dir = facing.getName().substring(0, 1).toUpperCase() + facing.getName().substring(1);
            text += " (" + dir + ")";
        }

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
