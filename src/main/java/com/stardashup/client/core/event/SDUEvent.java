package com.stardashup.client.core.event;

/**
 * Base class for all custom SDU events.
 *
 * <p>Events can be cancelled to prevent further processing by lower-priority listeners.</p>
 */
public abstract class SDUEvent {

    private boolean cancelled;

    /**
     * Returns whether this event has been cancelled.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancelled true to cancel the event
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
