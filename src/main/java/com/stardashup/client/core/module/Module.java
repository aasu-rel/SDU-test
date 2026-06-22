package com.stardashup.client.core.module;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.event.events.ModuleToggleEvent;
import com.stardashup.client.core.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all SDU modules.
 *
 * <p>A module represents a toggleable feature with settings, keybinds, and lifecycle hooks.
 * Modules are registered with the {@link ModuleManager} and can be enabled/disabled by
 * the user via keybind, ClickGUI, or command.</p>
 *
 * <p>Subclasses should:</p>
 * <ul>
 *     <li>Call {@link #addSetting(Setting)} in constructor for any configurable settings</li>
 *     <li>Override {@link #onEnable()} and {@link #onDisable()} for lifecycle logic</li>
 *     <li>Use {@code @SubscribeEvent} for Forge events when enabled</li>
 * </ul>
 */
public abstract class Module {

    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;
    private int keyBind;
    private final List<Setting<?>> settings;

    /**
     * Creates a new module.
     *
     * @param name        display name
     * @param description short description of what this module does
     * @param category    the category this module belongs to
     * @param keyBind     default LWJGL key code, or 0 for none
     */
    public Module(String name, String description, ModuleCategory category, int keyBind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.keyBind = keyBind;
        this.settings = new ArrayList<Setting<?>>();
    }

    public Module(String name, String description, ModuleCategory category) {
        this(name, description, category, 0);
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Called when the module is enabled. Override to register event listeners,
     * start tracking, etc.
     */
    protected void onEnable() {
    }

    /**
     * Called when the module is disabled. Override to clean up resources,
     * unregister listeners, etc.
     */
    protected void onDisable() {
    }

    /**
     * Toggles the enabled state of this module.
     */
    public void toggle() {
        setEnabled(!enabled);
    }

    /**
     * Sets the enabled state and calls lifecycle hooks.
     */
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }

        // Fire toggle event
        SDUClient.getInstance().getEventBus().post(new ModuleToggleEvent(this, enabled));
    }

    // -------------------------------------------------------------------------
    // Settings
    // -------------------------------------------------------------------------

    /**
     * Registers a setting for this module.
     *
     * @param setting the setting to add
     * @return the setting (for chaining)
     */
    protected <T extends Setting<?>> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    /**
     * Returns a setting by name, or null if not found.
     */
    public Setting<?> getSetting(String name) {
        for (Setting<?> setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name + " [" + category.getDisplayName() + "] " + (enabled ? "ON" : "OFF");
    }
}
