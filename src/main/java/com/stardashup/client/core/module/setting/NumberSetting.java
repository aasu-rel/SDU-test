package com.stardashup.client.core.module.setting;

/**
 * A numeric setting with min/max bounds and step increments.
 */
public class NumberSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setValue(Double value) {
        // Clamp to bounds
        value = Math.max(min, Math.min(max, value));
        // Snap to step
        if (step > 0) {
            value = Math.round(value / step) * step;
        }
        super.setValue(value);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    /**
     * Convenience method to get value as int.
     */
    public int getIntValue() {
        return getValue().intValue();
    }

    /**
     * Convenience method to get value as float.
     */
    public float getFloatValue() {
        return getValue().floatValue();
    }

    @Override
    public String serialize() {
        return Double.toString(getValue());
    }

    @Override
    public void deserialize(String serialized) {
        try {
            setValue(Double.parseDouble(serialized));
        } catch (NumberFormatException e) {
            reset();
        }
    }
}
