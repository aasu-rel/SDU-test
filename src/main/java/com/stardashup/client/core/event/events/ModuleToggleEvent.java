package com.stardashup.client.core.event.events;

import com.stardashup.client.core.event.SDUEvent;
import com.stardashup.client.core.module.Module;

/**
 * Fired when a module is toggled on or off.
 */
public class ModuleToggleEvent extends SDUEvent {

    private final Module module;
    private final boolean enabled;

    public ModuleToggleEvent(Module module, boolean enabled) {
        this.module = module;
        this.enabled = enabled;
    }

    public Module getModule() {
        return module;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
