package com.stardashup.client.core.event.events;

import com.stardashup.client.core.event.SDUEvent;

/**
 * Fired when a key is pressed or released.
 */
public class KeyPressEvent extends SDUEvent {

    private final int keyCode;
    private final boolean pressed;

    public KeyPressEvent(int keyCode, boolean pressed) {
        this.keyCode = keyCode;
        this.pressed = pressed;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isPressed() {
        return pressed;
    }
}
