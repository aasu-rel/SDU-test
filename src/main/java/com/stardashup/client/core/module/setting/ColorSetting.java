package com.stardashup.client.core.module.setting;

import java.awt.Color;

/**
 * A color setting stored as an ARGB integer.
 */
public class ColorSetting extends Setting<Integer> {

    private final boolean hasAlpha;

    public ColorSetting(String name, String description, int defaultColor, boolean hasAlpha) {
        super(name, description, defaultColor);
        this.hasAlpha = hasAlpha;
    }

    public ColorSetting(String name, String description, int defaultColor) {
        this(name, description, defaultColor, true);
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

    public int getRed() {
        return (getValue() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getValue() >> 8) & 0xFF;
    }

    public int getBlue() {
        return getValue() & 0xFF;
    }

    public int getAlpha() {
        return (getValue() >> 24) & 0xFF;
    }

    /**
     * Creates the color from RGBA components.
     */
    public void setColor(int r, int g, int b, int a) {
        setValue((a << 24) | (r << 16) | (g << 8) | b);
    }

    public void setColor(int r, int g, int b) {
        setColor(r, g, b, 255);
    }

    /**
     * Returns a java.awt.Color for convenience.
     */
    public Color toAwtColor() {
        return new Color(getRed(), getGreen(), getBlue(), getAlpha());
    }

    @Override
    public String serialize() {
        return Integer.toHexString(getValue());
    }

    @Override
    public void deserialize(String serialized) {
        try {
            // Parse as unsigned to handle alpha channel
            setValue((int) Long.parseLong(serialized, 16));
        } catch (NumberFormatException e) {
            reset();
        }
    }
}
