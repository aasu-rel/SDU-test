package com.stardashup.client;

import com.stardashup.client.core.command.CommandManager;
import com.stardashup.client.core.config.ConfigManager;
import com.stardashup.client.core.event.SDUEventBus;
import com.stardashup.client.core.keybind.KeybindManager;
import com.stardashup.client.core.log.CrashReporter;
import com.stardashup.client.core.log.SDULogger;
import com.stardashup.client.core.module.ModuleManager;
import com.stardashup.client.module.hud.HUDRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * StarDashUp (SDU) Client — Main Entry Point
 *
 * <p>A lightweight, high-performance Minecraft 1.8.9 Forge client mod focused on
 * performance optimizations, responsive input handling, quality-of-life features,
 * and extensive customization.</p>
 *
 * <p>This is NOT a cheat client. Every feature improves usability without
 * providing unfair gameplay advantages.</p>
 */
@Mod(modid = SDUClient.MOD_ID, name = SDUClient.MOD_NAME, version = SDUClient.MOD_VERSION)
public class SDUClient {

    public static final String MOD_ID = "stardashup";
    public static final String MOD_NAME = "StarDashUp Client";
    public static final String MOD_VERSION = "1.0.0";
    public static final String PREFIX = "\u00A78[\u00A7bSDU\u00A78]\u00A7r ";

    @Mod.Instance(MOD_ID)
    private static SDUClient instance;

    /** Root directory for all SDU configuration and data files. */
    private File dataDir;

    // Core systems — initialized in order during mod lifecycle
    private SDULogger logger;
    private SDUEventBus eventBus;
    private ConfigManager configManager;
    private KeybindManager keybindManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private CrashReporter crashReporter;
    private HUDRenderHandler hudRenderHandler;

    /**
     * Returns the singleton SDUClient instance.
     */
    public static SDUClient getInstance() {
        return instance;
    }

    /**
     * Returns the Minecraft client instance. Convenience accessor.
     */
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Set up data directory
        dataDir = new File(Minecraft.getMinecraft().mcDataDir, "stardashup");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Initialize logging first — everything else may log
        logger = new SDULogger();
        crashReporter = new CrashReporter(new File(dataDir, "crash-reports"));

        logger.info("StarDashUp Client v" + MOD_VERSION + " — Pre-initialization");

        // Initialize event bus
        eventBus = new SDUEventBus();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Initializing core systems...");

        try {
            // Config system
            configManager = new ConfigManager(new File(dataDir, "config"));
            logger.info("Config system initialized.");

            // Keybind system
            keybindManager = new KeybindManager();
            logger.info("Keybind system initialized.");

            // Module system — registers all modules
            moduleManager = new ModuleManager();
            moduleManager.init();
            logger.info("Module system initialized. Loaded " + moduleManager.getModules().size() + " modules.");

            // Command system
            commandManager = new CommandManager();
            commandManager.init();
            logger.info("Command system initialized.");

            // HUD render handler — subscribes to Forge events
            hudRenderHandler = new HUDRenderHandler();
            MinecraftForge.EVENT_BUS.register(hudRenderHandler);
            logger.info("HUD render handler registered.");

            // Register keybind handler on Forge bus
            MinecraftForge.EVENT_BUS.register(keybindManager);

            // Load saved configuration
            configManager.loadAll();
            logger.info("Configuration loaded.");

        } catch (Exception e) {
            logger.error("Failed to initialize SDU Client!", e);
            crashReporter.reportCrash(e, "Initialization");
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("StarDashUp Client v" + MOD_VERSION + " — Fully loaded!");
        logger.info("Made Minecraft smoother, not easier.");
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public File getDataDir() {
        return dataDir;
    }

    public SDULogger getLogger() {
        return logger;
    }

    public SDUEventBus getEventBus() {
        return eventBus;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public KeybindManager getKeybindManager() {
        return keybindManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CrashReporter getCrashReporter() {
        return crashReporter;
    }

    public HUDRenderHandler getHudRenderHandler() {
        return hudRenderHandler;
    }
}
