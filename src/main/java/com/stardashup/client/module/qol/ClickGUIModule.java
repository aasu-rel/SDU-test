package com.stardashup.client.module.qol;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.gui.clickgui.ClickGUI;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * Module responsible for opening the ClickGUI.
 */
public class ClickGUIModule extends Module {

    private ClickGUI clickGUI;

    public ClickGUIModule() {
        super("ClickGUI", "Opens the module manager GUI", ModuleCategory.QOL);
        setKeyBind(Keyboard.KEY_RSHIFT);
    }

    @Override
    protected void onEnable() {
        if (clickGUI == null) {
            clickGUI = new ClickGUI();
        }
        Minecraft.getMinecraft().displayGuiScreen(clickGUI);
        // Turn off the module immediately so it can be pressed again
        toggle();
    }
}
