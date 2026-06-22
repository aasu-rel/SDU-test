package com.stardashup.client.render;

import java.awt.Color;

/**
 * Color manipulation utilities.
 */
public class ColorUtils {

    /**
     * Generates a rainbow color based on time and offset.
     *
     * @param offset    Offset in seconds
     * @param speed     Speed of the rainbow cycle (e.g., 2000 for 2 seconds)
     * @param saturation Saturation of the color (0.0 - 1.0)
     * @param brightness Brightness of the color (0.0 - 1.0)
     * @return The generated color as an ARGB int
     */
    public static int getRainbow(float offset, int speed, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis() + (long)(offset * 1000)) % speed) / (float)speed;
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    /**
     * Blends two colors together.
     *
     * @param c1      The first color (ARGB)
     * @param c2      The second color (ARGB)
     * @param ratio   The ratio of the blend (0.0 = 100% c1, 1.0 = 100% c2)
     * @return The blended color as an ARGB int
     */
    public static int blendColors(int c1, int c2, float ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        float inverseRatio = 1.0f - ratio;

        int a1 = (c1 >> 24) & 0xFF;
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int a2 = (c2 >> 24) & 0xFF;
        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        int a = (int)((a1 * inverseRatio) + (a2 * ratio));
        int r = (int)((r1 * inverseRatio) + (r2 * ratio));
        int g = (int)((g1 * inverseRatio) + (g2 * ratio));
        int b = (int)((b1 * inverseRatio) + (b2 * ratio));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Multiplies the alpha channel of a color.
     *
     * @param color      The color (ARGB)
     * @param alphaMultiplier The multiplier (0.0 - 1.0)
     * @return The new color with modified alpha
     */
    public static int multiplyAlpha(int color, float alphaMultiplier) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        a = (int)(a * alphaMultiplier);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
