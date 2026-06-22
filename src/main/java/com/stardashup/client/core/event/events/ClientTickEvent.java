package com.stardashup.client.core.event.events;

import com.stardashup.client.core.event.SDUEvent;

/**
 * Fired every client tick (approximately 20 times per second).
 */
public class ClientTickEvent extends SDUEvent {

    // Lightweight event — no extra data needed.
    // Modules use this for periodic logic updates (not rendering).
}
