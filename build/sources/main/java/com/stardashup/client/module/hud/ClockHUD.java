package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Displays a real-world clock.
 */
public class ClockHUD extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    private final BooleanSetting format24h = addSetting(new BooleanSetting("24 Hour Format", "Use 24-hour time format", false));
    private final BooleanSetting showDate = addSetting(new BooleanSetting("Show Date", "Display the current date", false));

    public ClockHUD() {
        super("Clock", "Displays the real-world time", 2, 38);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();

        String pattern = format24h.getValue() ? "HH:mm:ss" : "hh:mm:ss a";
        if (showDate.getValue()) {
            pattern = "yyyy-MM-dd " + pattern;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String text = dateFormat.format(new Date());

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
