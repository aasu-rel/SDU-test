package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Displays the current frame rate.
 */
public class FPSCounter extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    private final BooleanSetting colorCode = addSetting(new BooleanSetting("Color Code", "Color code FPS based on value", true));

    public FPSCounter() {
        super("FPS", "Displays your frames per second", 2, 2);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        int fps = Minecraft.getDebugFPS();
        String text = "FPS: " + fps;

        int color = textColor.getValue();
        if (colorCode.getValue()) {
            if (fps >= 60) color = 0xFF55FF55; // Green
            else if (fps >= 30) color = 0xFFFFFF55; // Yellow
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
