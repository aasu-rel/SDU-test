package com.stardashup.client.module.pvp;

import com.stardashup.client.module.hud.HUDModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

/**
 * Compact potion effect display.
 */
public class PotionEffectsHUD extends HUDModule {

    public PotionEffectsHUD() {
        super("Potion Effects", "Compact potion display", 2, 174);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) {
            this.width = 50;
            this.height = mc.fontRendererObj.FONT_HEIGHT;
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale.getFloatValue(), scale.getFloatValue(), 1);

        int yOffset = 0;
        int maxWidth = 0;

        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName());
            
            if (effect.getAmplifier() == 1) name += " II";
            else if (effect.getAmplifier() == 2) name += " III";
            else if (effect.getAmplifier() == 3) name += " IV";

            String duration = Potion.getDurationString(effect);
            String text = name + " \u00A77" + duration;

            int color = potion.getLiquidColor();

            mc.fontRendererObj.drawStringWithShadow(text, 0, yOffset, color);

            int w = mc.fontRendererObj.getStringWidth(text);
            if (w > maxWidth) maxWidth = w;
            yOffset += mc.fontRendererObj.FONT_HEIGHT + 2;
        }

        this.width = maxWidth;
        this.height = yOffset;

        GlStateManager.popMatrix();
    }
}
