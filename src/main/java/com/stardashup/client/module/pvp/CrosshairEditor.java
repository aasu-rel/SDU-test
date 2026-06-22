package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.ColorSetting;
import com.stardashup.client.core.module.setting.ModeSetting;
import com.stardashup.client.core.module.setting.NumberSetting;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Custom crosshair implementation replacing the vanilla one.
 */
public class CrosshairEditor extends Module {

    private final ModeSetting style = addSetting(new ModeSetting("Style", "Crosshair shape", "Cross", "Cross", "Dot", "Circle"));
    private final NumberSetting size = addSetting(new NumberSetting("Size", "Length of crosshair lines", 5, 1, 20, 1));
    private final NumberSetting gap = addSetting(new NumberSetting("Gap", "Gap in the middle", 2, 0, 10, 1));
    private final NumberSetting thickness = addSetting(new NumberSetting("Thickness", "Line thickness", 1, 0.5, 5, 0.5));
    private final ColorSetting color = addSetting(new ColorSetting("Color", "Crosshair color", 0xFFFFFFFF));

    public CrosshairEditor() {
        super("Custom Crosshair", "Replaces the vanilla crosshair", ModuleCategory.PVP);
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
    public void onRenderCrosshair(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            // Cancel vanilla crosshair
            event.setCanceled(true);

            ScaledResolution sr = event.resolution;
            float midX = sr.getScaledWidth() / 2f;
            float midY = sr.getScaledHeight() / 2f;
            int c = color.getValue();
            float t = thickness.getFloatValue() / 2f;
            float s = size.getFloatValue();
            float g = gap.getFloatValue();

            if (style.is("Cross")) {
                // Left
                RenderUtils.drawRect(midX - g - s, midY - t, midX - g, midY + t, c);
                // Right
                RenderUtils.drawRect(midX + g, midY - t, midX + g + s, midY + t, c);
                // Top
                RenderUtils.drawRect(midX - t, midY - g - s, midX + t, midY - g, c);
                // Bottom
                RenderUtils.drawRect(midX - t, midY + g, midX + t, midY + g + s, c);
            } else if (style.is("Dot")) {
                RenderUtils.drawRect(midX - s, midY - s, midX + s, midY + s, c);
            } else if (style.is("Circle")) {
                // Circle would require a custom GL circle method. Using a hollow square for now as placeholder
                RenderUtils.drawBorderedRect(midX - s, midY - s, midX + s, midY + s, thickness.getFloatValue(), 0x00000000, c);
            }
        }
    }
}
