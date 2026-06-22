package com.stardashup.client.gui.clickgui.animation;

/**
 * Simple easing-based animation helper.
 */
public class Animation {

    private float value;
    private float target;
    private float speed;

    public Animation(float startValue, float speed) {
        this.value = startValue;
        this.target = startValue;
        this.speed = speed;
    }

    /**
     * Updates the animation value towards the target.
     * Should be called every frame before rendering.
     */
    public void update() {
        if (value == target) return;

        float diff = target - value;
        // Simple easing (ease-out)
        value += diff * speed;

        // Snap to target if very close
        if (Math.abs(target - value) < 0.01f) {
            value = target;
        }
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
