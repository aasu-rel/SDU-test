package com.stardashup.client.core.event.events;

import com.stardashup.client.core.event.SDUEvent;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Fired each frame when HUD elements should render.
 */
public class RenderHUDEvent extends SDUEvent {

    private final ScaledResolution scaledResolution;
    private final float partialTicks;

    public RenderHUDEvent(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
