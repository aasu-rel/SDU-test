package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Tracks time played in the current session.
 */
public class PlaytimeTracker extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    
    private final long sessionStartTime;

    public PlaytimeTracker() {
        super("Playtime Tracker", "Displays your session playtime", 2, 62);
        this.sessionStartTime = System.currentTimeMillis();
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();

        long elapsedMillis = System.currentTimeMillis() - sessionStartTime;
        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / (1000 * 60)) % 60;
        long hours = (elapsedMillis / (1000 * 60 * 60));

        String text = String.format("Playtime: %02d:%02d:%02d", hours, minutes, seconds);

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
