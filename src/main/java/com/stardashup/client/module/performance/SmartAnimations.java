package com.stardashup.client.module.performance;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;

/**
 * Placeholder for Smart Animations.
 * <p>Requires SpongePowered Mixin into {@code TextureAtlasSprite.updateAnimation()}
 * to conditionally skip ticking animations that are out of the camera frustum.</p>
 */
public class SmartAnimations extends Module {

    public final BooleanSetting water = addSetting(new BooleanSetting("Water", "Optimize water animation", true));
    public final BooleanSetting lava = addSetting(new BooleanSetting("Lava", "Optimize lava animation", true));
    public final BooleanSetting fire = addSetting(new BooleanSetting("Fire", "Optimize fire animation", true));
    public final BooleanSetting portals = addSetting(new BooleanSetting("Portals", "Optimize portal animation", true));

    public SmartAnimations() {
        super("Smart Animations", "Pauses unseen animated textures (Requires Mixin)", ModuleCategory.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        // Handled via Mixin
    }

    @Override
    protected void onDisable() {
        // Handled via Mixin
    }
}
