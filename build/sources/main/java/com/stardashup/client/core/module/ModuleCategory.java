package com.stardashup.client.core.module;

/**
 * Enumeration of module categories for organization in the ClickGUI.
 */
public enum ModuleCategory {

    PERFORMANCE("Performance", "\u26A1"),
    INPUT("Input", "\u2328"),
    HUD("HUD", "\uD83D\uDCCA"),
    PVP("PvP", "\u2694"),
    NETWORK("Network", "\uD83C\uDF10"),
    QOL("Quality of Life", "\u2728");

    private final String displayName;
    private final String icon;

    ModuleCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }
}
