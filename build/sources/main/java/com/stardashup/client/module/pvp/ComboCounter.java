package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.module.hud.HUDModule;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Tracks consecutive hits without taking damage.
 */
public class ComboCounter extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    
    private int combo = 0;
    private long lastHitTime = 0;

    public ComboCounter() {
        super("Combo Counter", "Displays current hit combo", 2, 98);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        combo = 0;
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.entityPlayer == mc.thePlayer) {
            combo++;
            lastHitTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.entityLiving == mc.thePlayer) {
            combo = 0; // Reset combo when hurt
        }
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();

        // Reset combo if no hit for 5 seconds
        if (System.currentTimeMillis() - lastHitTime > 5000) {
            combo = 0;
        }

        String text = "Combo: " + combo;

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
