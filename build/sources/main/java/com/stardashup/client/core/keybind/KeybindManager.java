package com.stardashup.client.core.keybind;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.module.Module;
import com.stardashup.client.gui.clickgui.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Manages keybinds for module toggling and special actions.
 *
 * <p>Listens for Forge key input events and dispatches to modules whose keybind matches.</p>
 */
public class KeybindManager {

    /** Default keybind for opening the ClickGUI (Right Shift). */
    private int clickGuiKey = Keyboard.KEY_RSHIFT;

    /** Default keybind for opening the HUD Editor (Right Control). */
    private int hudEditorKey = Keyboard.KEY_RCONTROL;

    /**
     * Forge event handler — called when a key is pressed.
     */
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return; // Only handle key-down
        int keyCode = Keyboard.getEventKey();
        if (keyCode == 0) return;

        // ClickGUI keybind
        if (keyCode == clickGuiKey) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(new ClickGUI());
            }
            return;
        }

        // HUD Editor keybind
        if (keyCode == hudEditorKey) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(
                        new com.stardashup.client.gui.hudeditor.HUDEditor());
            }
            return;
        }

        // Module keybinds
        if (SDUClient.getInstance().getModuleManager() != null) {
            for (Module module : SDUClient.getInstance().getModuleManager().getModules()) {
                if (module.getKeyBind() == keyCode) {
                    module.toggle();
                }
            }
        }
    }

    public int getClickGuiKey() {
        return clickGuiKey;
    }

    public void setClickGuiKey(int clickGuiKey) {
        this.clickGuiKey = clickGuiKey;
    }

    public int getHudEditorKey() {
        return hudEditorKey;
    }

    public void setHudEditorKey(int hudEditorKey) {
        this.hudEditorKey = hudEditorKey;
    }
}
