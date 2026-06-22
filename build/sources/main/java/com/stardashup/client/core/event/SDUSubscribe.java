package com.stardashup.client.core.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods as SDU event subscribers.
 *
 * <p>Methods annotated with {@code @SDUSubscribe} must accept exactly one parameter
 * that extends {@link SDUEvent}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * &#64;SDUSubscribe(priority = 5)
 * public void onTick(ClientTickEvent event) {
 *     // Handle tick
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SDUSubscribe {

    /**
     * Priority of this listener. Higher values execute first.
     * Default is 0 (normal priority).
     */
    int priority() default 0;
}
