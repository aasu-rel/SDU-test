package com.stardashup.client.util;

/**
 * High-precision timer utility using System.nanoTime().
 *
 * <p>Avoids object allocation during normal timing checks.</p>
 */
public class TimerUtil {

    private long lastTime;

    public TimerUtil() {
        reset();
    }

    /**
     * Resets the timer to the current time.
     */
    public void reset() {
        lastTime = System.nanoTime();
    }

    /**
     * Checks if the specified number of milliseconds has passed since the last reset.
     *
     * @param ms milliseconds to check
     * @return true if elapsed time >= ms
     */
    public boolean hasPassed(long ms) {
        return (System.nanoTime() - lastTime) >= (ms * 1000000L);
    }

    /**
     * Checks if the specified number of milliseconds has passed, and resets if true.
     */
    public boolean checkAndReset(long ms) {
        if (hasPassed(ms)) {
            reset();
            return true;
        }
        return false;
    }

    /**
     * Returns the elapsed time in milliseconds.
     */
    public long getPassedMs() {
        return (System.nanoTime() - lastTime) / 1000000L;
    }
}
