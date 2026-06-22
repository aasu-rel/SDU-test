package com.stardashup.client.module.hud;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.core.module.setting.ColorSetting;
import com.stardashup.client.render.ColorUtils;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Visual key display (WASD + Space + LMB/RMB).
 */
public class KeystrokesHUD extends HUDModule {

    private final BooleanSetting showMouse = addSetting(new BooleanSetting("Show Mouse", "Show LMB and RMB", true));
    private final BooleanSetting showSpace = addSetting(new BooleanSetting("Show Space", "Show space bar", true));
    private final ColorSetting pressedColor = addSetting(new ColorSetting("Pressed Color", "Color when key is pressed", 0x80FFFFFF));

    public KeystrokesHUD() {
        super("Keystrokes", "Displays your key presses visually", 2, 40);
        this.width = 64; // Base width
        this.height = 64; // Base height (will adjust based on settings)
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale.getFloatValue(), scale.getFloatValue(), 1);

        int gap = 2;
        int size = 20;

        // W key
        drawKey(size + gap, 0, size, size, mc.gameSettings.keyBindForward.getKeyCode(), "W", false);
        // A, S, D keys
        drawKey(0, size + gap, size, size, mc.gameSettings.keyBindLeft.getKeyCode(), "A", false);
        drawKey(size + gap, size + gap, size, size, mc.gameSettings.keyBindBack.getKeyCode(), "S", false);
        drawKey((size + gap) * 2, size + gap, size, size, mc.gameSettings.keyBindRight.getKeyCode(), "D", false);

        int currentY = (size + gap) * 2;

        // Mouse buttons
        if (showMouse.getValue()) {
            drawKey(0, currentY, size * 1.5f + gap / 2f, size, 0, "LMB", true);
            drawKey(size * 1.5f + gap * 1.5f, currentY, size * 1.5f + gap / 2f, size, 1, "RMB", true);
            currentY += size + gap;
        }

        // Space bar
        if (showSpace.getValue()) {
            drawKey(0, currentY, (size * 3) + (gap * 2), size / 2f, mc.gameSettings.keyBindJump.getKeyCode(), "----", false);
            currentY += size / 2f + gap;
        }

        this.width = (size * 3) + (gap * 2);
        this.height = currentY - gap;
    }

    private void drawKey(float x, float y, float w, float h, int code, String name, boolean isMouse) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean pressed = isMouse ? Mouse.isButtonDown(code) : Keyboard.isKeyDown(code);

        int bg = pressed ? pressedColor.getValue() : backgroundColor.getValue();
        int textC = pressed ? 0xFF000000 : textColor.getValue();

        RenderUtils.drawRect(x, y, x + w, y + h, bg);

        int strWidth = mc.fontRendererObj.getStringWidth(name);
        mc.fontRendererObj.drawString(name, (int)(x + w / 2f - strWidth / 2f), (int)(y + h / 2f - mc.fontRendererObj.FONT_HEIGHT / 2f), textC);
    }
}
