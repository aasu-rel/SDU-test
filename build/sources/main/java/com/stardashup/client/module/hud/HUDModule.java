package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.ColorSetting;
import com.stardashup.client.core.module.setting.NumberSetting;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Abstract base class for all HUD modules.
 *
 * <p>Provides settings for position, scale, and colors, along with bounds calculation
 * for drag-and-drop in the HUD editor.</p>
 */
public abstract class HUDModule extends Module {

    private int x, y;
    protected int width, height;

    protected final NumberSetting scale;
    protected final ColorSetting textColor;
    protected final ColorSetting backgroundColor;

    public HUDModule(String name, String description, int defaultX, int defaultY) {
        super(name, description, ModuleCategory.HUD);
        this.x = defaultX;
        this.y = defaultY;

        this.scale = addSetting(new NumberSetting("Scale", "Scale of the HUD element", 1.0, 0.5, 3.0, 0.1));
        this.textColor = addSetting(new ColorSetting("Text Color", "Main text color", 0xFFFFFFFF));
        this.backgroundColor = addSetting(new ColorSetting("Background", "Background color", 0x80000000));
    }

    /**
     * Renders the HUD element.
     *
     * @param sr the current scaled resolution
     */
    public abstract void render(ScaledResolution sr);

    /**
     * Renders a dummy version of the HUD element for the HUD editor.
     * Overriding this is optional, defaults to calling render(sr).
     */
    public void renderDummy(ScaledResolution sr) {
        render(sr);
    }

    /**
     * Checks if the given mouse coordinates are within the bounds of this HUD element.
     * Takes scaling into account.
     */
    public boolean isMouseOver(int mouseX, int mouseY) {
        float scaledWidth = width * scale.getFloatValue();
        float scaledHeight = height * scale.getFloatValue();
        return mouseX >= x && mouseX <= x + scaledWidth && mouseY >= y && mouseY <= y + scaledHeight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
