package com.stardashup.client.core.module.setting;

/**
 * A boolean toggle setting.
 */
public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public String serialize() {
        return Boolean.toString(getValue());
    }

    @Override
    public void deserialize(String serialized) {
        setValue(Boolean.parseBoolean(serialized));
    }
}
