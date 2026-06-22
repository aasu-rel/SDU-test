package com.stardashup.client.module.performance;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Automatically adjusts Minecraft's particle settings to improve performance.
 * (A deeper implementation would require Mixin into EffectRenderer to cull based on frustum).
 */
public class ParticleCulling extends Module {

    private final ModeSetting mode = addSetting(new ModeSetting("Mode", "Level of particle reduction", "Decreased", "Minimal", "Decreased", "All"));
    
    private int originalSetting = -1;

    public ParticleCulling() {
        super("Particle Culling", "Limits rendered particles", ModuleCategory.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        originalSetting = mc.gameSettings.particleSetting;
        applySetting();
        
        // Listen to config changes
        mode.addListener(new com.stardashup.client.core.module.setting.Setting.ChangeListener<String>() {
            @Override
            public void onChanged(String oldValue, String newValue) {
                if (isEnabled()) applySetting();
            }
        });
    }

    @Override
    protected void onDisable() {
        if (originalSetting != -1) {
            Minecraft.getMinecraft().gameSettings.particleSetting = originalSetting;
            originalSetting = -1;
        }
    }
    
    private void applySetting() {
        int val = 0; // All
        if (mode.is("Decreased")) val = 1;
        else if (mode.is("Minimal")) val = 2;
        
        Minecraft.getMinecraft().gameSettings.particleSetting = val;
    }
}
