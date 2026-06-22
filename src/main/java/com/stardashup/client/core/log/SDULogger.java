package com.stardashup.client.core.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SDU logging wrapper around Log4j.
 *
 * <p>All SDU log messages are prefixed with [SDU] for easy identification in the console.
 * Provides both instance and static logging methods.</p>
 */
public class SDULogger {

    private static final Logger LOGGER = LogManager.getLogger("SDU");
    private static boolean debugEnabled = false;

    public SDULogger() {
        // Constructor for instance usage
    }

    // -------------------------------------------------------------------------
    // Instance methods
    // -------------------------------------------------------------------------

    public void info(String message) {
        LOGGER.info("[SDU] " + message);
    }

    public void warn(String message) {
        LOGGER.warn("[SDU] " + message);
    }

    public void error(String message) {
        LOGGER.error("[SDU] " + message);
    }

    public void error(String message, Throwable throwable) {
        LOGGER.error("[SDU] " + message, throwable);
    }

    public void debug(String message) {
        if (debugEnabled) {
            LOGGER.info("[SDU/DEBUG] " + message);
        }
    }

    // -------------------------------------------------------------------------
    // Static convenience methods
    // -------------------------------------------------------------------------

    public static void staticInfo(String message) {
        LOGGER.info("[SDU] " + message);
    }

    public static void staticWarn(String message) {
        LOGGER.warn("[SDU] " + message);
    }

    public static void staticError(String message) {
        LOGGER.error("[SDU] " + message);
    }

    public static void staticError(String message, Throwable throwable) {
        LOGGER.error("[SDU] " + message, throwable);
    }

    public static void staticDebug(String message) {
        if (debugEnabled) {
            LOGGER.info("[SDU/DEBUG] " + message);
        }
    }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
        staticInfo("Debug logging " + (enabled ? "enabled" : "disabled"));
    }
}
