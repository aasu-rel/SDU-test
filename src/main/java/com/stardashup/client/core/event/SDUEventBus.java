package com.stardashup.client.core.event;

import com.stardashup.client.core.log.SDULogger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Lightweight event bus for SDU internal events.
 *
 * <p>Uses annotation-based discovery ({@link SDUSubscribe}) to find event handler methods.
 * Supports priority ordering and thread-safe registration/unregistration.</p>
 *
 * <p>This is separate from Forge's event bus — used only for SDU-internal communication
 * between modules and core systems.</p>
 */
public class SDUEventBus {

    /**
     * Internal listener wrapper holding the subscriber object, method, and priority.
     */
    private static class Listener implements Comparable<Listener> {
        final Object subscriber;
        final Method method;
        final int priority;

        Listener(Object subscriber, Method method, int priority) {
            this.subscriber = subscriber;
            this.method = method;
            this.priority = priority;
        }

        @Override
        public int compareTo(Listener other) {
            // Higher priority executes first
            return Integer.compare(other.priority, this.priority);
        }
    }

    /** Map from event class to sorted list of listeners. */
    private final ConcurrentHashMap<Class<? extends SDUEvent>, CopyOnWriteArrayList<Listener>> listenerMap;

    public SDUEventBus() {
        this.listenerMap = new ConcurrentHashMap<Class<? extends SDUEvent>, CopyOnWriteArrayList<Listener>>();
    }

    /**
     * Registers all {@link SDUSubscribe}-annotated methods from the given object.
     *
     * @param subscriber the object containing event handler methods
     */
    public void register(Object subscriber) {
        for (Method method : subscriber.getClass().getDeclaredMethods()) {
            SDUSubscribe annotation = method.getAnnotation(SDUSubscribe.class);
            if (annotation == null) continue;

            // Validate: must have exactly one parameter that extends SDUEvent
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !SDUEvent.class.isAssignableFrom(params[0])) {
                SDULogger.staticWarn("Invalid @SDUSubscribe method: " + method.getName()
                        + " in " + subscriber.getClass().getSimpleName()
                        + " — must accept exactly one SDUEvent parameter.");
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends SDUEvent> eventType = (Class<? extends SDUEvent>) params[0];
            method.setAccessible(true);

            Listener listener = new Listener(subscriber, method, annotation.priority());

            CopyOnWriteArrayList<Listener> listeners = listenerMap.get(eventType);
            if (listeners == null) {
                listeners = new CopyOnWriteArrayList<Listener>();
                CopyOnWriteArrayList<Listener> existing = listenerMap.putIfAbsent(eventType, listeners);
                if (existing != null) {
                    listeners = existing;
                }
            }
            listeners.add(listener);

            // Re-sort by priority (CopyOnWriteArrayList doesn't have sort in Java 8 directly)
            sortListeners(listeners);
        }
    }

    /**
     * Unregisters all listeners belonging to the given subscriber.
     *
     * @param subscriber the object to unregister
     */
    public void unregister(Object subscriber) {
        for (CopyOnWriteArrayList<Listener> listeners : listenerMap.values()) {
            Iterator<Listener> it = listeners.iterator();
            while (it.hasNext()) {
                if (it.next().subscriber == subscriber) {
                    listeners.remove(it.next());
                }
            }
        }
    }

    /**
     * Posts an event to all registered listeners.
     *
     * <p>Listeners are invoked in priority order (highest first). If a listener cancels
     * the event, subsequent listeners are skipped.</p>
     *
     * @param event the event to post
     * @return the event (for chaining / checking cancellation state)
     */
    public <T extends SDUEvent> T post(T event) {
        CopyOnWriteArrayList<Listener> listeners = listenerMap.get(event.getClass());
        if (listeners == null || listeners.isEmpty()) {
            return event;
        }

        for (Listener listener : listeners) {
            if (event.isCancelled()) break;

            try {
                listener.method.invoke(listener.subscriber, event);
            } catch (Exception e) {
                SDULogger.staticError("Error dispatching event " + event.getClass().getSimpleName()
                        + " to " + listener.subscriber.getClass().getSimpleName()
                        + "." + listener.method.getName(), e);
            }
        }

        return event;
    }

    /**
     * Sorts listeners by priority (highest first).
     */
    private void sortListeners(CopyOnWriteArrayList<Listener> listeners) {
        List<Listener> sorted = new ArrayList<Listener>(listeners);
        Collections.sort(sorted);
        listeners.clear();
        listeners.addAll(sorted);
    }
}
