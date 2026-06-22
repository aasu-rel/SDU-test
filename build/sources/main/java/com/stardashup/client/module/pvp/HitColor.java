package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.ColorSetting;

/**
 * Placeholder for Hit Color module.
 * <p>Requires Mixin into {@code RendererLivingEntity.setBrightness} to modify the red flash.</p>
 */
public class HitColor extends Module {

    public final ColorSetting color = addSetting(new ColorSetting("Color", "Hit overlay color", 0x80FF0000));

    public HitColor() {
        super("Hit Color", "Changes the red hit flash color (Requires Mixin)", ModuleCategory.PVP);
    }
}
