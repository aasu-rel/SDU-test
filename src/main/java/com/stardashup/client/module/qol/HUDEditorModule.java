package com.stardashup.client.module.qol;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.gui.hudeditor.HUDEditor;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * Module responsible for opening the HUD Editor.
 */
public class HUDEditorModule extends Module {

    private HUDEditor hudEditor;

    public HUDEditorModule() {
        super("HUD Editor", "Opens the GUI to drag HUD elements", ModuleCategory.QOL);
        setKeyBind(Keyboard.KEY_RSHIFT); // Temporarily sharing Right Shift, user can rebind later
    }

    @Override
    protected void onEnable() {
        if (hudEditor == null) {
            hudEditor = new HUDEditor();
        }
        Minecraft.getMinecraft().displayGuiScreen(hudEditor);
        toggle();
    }
}
