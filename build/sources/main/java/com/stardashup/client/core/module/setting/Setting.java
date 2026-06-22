package com.stardashup.client.core.module.setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all module settings.
 *
 * <p>Settings are type-safe configuration values that modules expose for user customization.
 * Each setting has a name, description, and a current value with change listeners.</p>
 *
 * @param <T> the type of the setting value
 */
public abstract class Setting<T> {

    private final String name;
    private final String description;
    private T value;
    private final T defaultValue;
    private final List<ChangeListener<T>> listeners;

    /**
     * Functional interface for setting change listeners.
     */
    public interface ChangeListener<T> {
        void onChanged(T oldValue, T newValue);
    }

    public Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.listeners = new ArrayList<ChangeListener<T>>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        for (ChangeListener<T> listener : listeners) {
            listener.onChanged(oldValue, value);
        }
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void reset() {
        setValue(defaultValue);
    }

    public void addListener(ChangeListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Returns a string representation of the current value for config serialization.
     */
    public abstract String serialize();

    /**
     * Parses a string representation and sets the value.
     */
    public abstract void deserialize(String serialized);
}
