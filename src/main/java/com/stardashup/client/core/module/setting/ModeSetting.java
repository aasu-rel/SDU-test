package com.stardashup.client.core.module.setting;

import java.util.Arrays;
import java.util.List;

/**
 * A setting that cycles through a list of named modes.
 */
public class ModeSetting extends Setting<String> {

    private final List<String> modes;

    public ModeSetting(String name, String description, String defaultMode, String... modes) {
        super(name, description, defaultMode);
        this.modes = Arrays.asList(modes);

        if (!this.modes.contains(defaultMode)) {
            throw new IllegalArgumentException(
                    "Default mode '" + defaultMode + "' not found in modes list: " + this.modes);
        }
    }

    public List<String> getModes() {
        return modes;
    }

    /**
     * Cycles to the next mode in the list.
     */
    public void cycle() {
        int index = modes.indexOf(getValue());
        int nextIndex = (index + 1) % modes.size();
        setValue(modes.get(nextIndex));
    }

    /**
     * Returns the index of the currently selected mode.
     */
    public int getIndex() {
        return modes.indexOf(getValue());
    }

    /**
     * Checks if the current mode matches the given mode name (case-insensitive).
     */
    public boolean is(String mode) {
        return getValue().equalsIgnoreCase(mode);
    }

    @Override
    public void setValue(String value) {
        // Only allow valid modes
        if (modes.contains(value)) {
            super.setValue(value);
        }
    }

    @Override
    public String serialize() {
        return getValue();
    }

    @Override
    public void deserialize(String serialized) {
        if (modes.contains(serialized)) {
            setValue(serialized);
        } else {
            reset();
        }
    }
}
