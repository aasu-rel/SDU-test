package com.stardashup.client.module.qol;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;

/**
 * Enhanced screenshot manager.
 * <p>Full implementation requires intercepting F2 and opening a custom GUI.</p>
 */
public class ScreenshotManager extends Module {

    public final BooleanSetting copyToClipboard = addSetting(new BooleanSetting("Copy to Clipboard", "Copies screenshots to clipboard", true));

    public ScreenshotManager() {
        super("Screenshot Manager", "Enhances screenshots (Requires Mixin for full features)", ModuleCategory.QOL);
    }
}
