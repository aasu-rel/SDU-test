package com.stardashup.client.core.module;

import com.stardashup.client.core.log.SDULogger;
import com.stardashup.client.module.hud.*;
import com.stardashup.client.module.input.*;
import com.stardashup.client.module.network.*;
import com.stardashup.client.module.performance.*;
import com.stardashup.client.module.pvp.*;
import com.stardashup.client.module.qol.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all registered modules.
 *
 * <p>Provides methods to retrieve modules by name, class, or category.
 * All modules are registered during {@link #init()}.</p>
 */
public class ModuleManager {

    private final List<Module> modules;
    private final Map<Class<? extends Module>, Module> modulesByClass;
    private final Map<String, Module> modulesByName;

    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.modulesByClass = new HashMap<Class<? extends Module>, Module>();
        this.modulesByName = new HashMap<String, Module>();
    }

    /**
     * Registers all modules. Called during initialization.
     */
    public void init() {
        SDULogger.staticInfo("Registering modules...");

        // --- Performance ---
        register(new DynamicFPS());
        register(new EntityCulling());
        register(new ParticleCulling());
        register(new SmartAnimations());
        register(new MemoryOptimizer());
        register(new ChunkRenderOptimizer());
        register(new StutterFix());

        // --- Input ---
        register(new BetterBlockPlacement());
        register(new ImprovedJumpInput());
        register(new InventoryResponsiveness());

        // --- HUD ---
        register(new FPSCounter());
        register(new PingCounter());
        register(new CoordinatesHUD());
        register(new KeystrokesHUD());
        register(new ClockHUD());
        register(new SessionStats());
        register(new PlaytimeTracker());

        // --- PvP ---
        register(new CPSCounter());
        register(new ReachDisplay());
        register(new ComboCounter());
        register(new HitColor());
        register(new CrosshairEditor());
        register(new ArmorStatus());
        register(new PotionEffectsHUD());
        register(new DirectionHUD());
        register(new TNTTimer());

        // --- Network ---
        register(new PingDisplay());
        register(new PacketStatistics());
        register(new ConnectionDiagnostics());

        // --- QoL ---
        register(new ScreenshotManager());
        register(new ChatEnhancements());
        register(new InventoryTweaks());

        SDULogger.staticInfo("Registered " + modules.size() + " modules.");
    }

    /**
     * Registers a single module.
     */
    private void register(Module module) {
        modules.add(module);
        modulesByClass.put(module.getClass(), module);
        modulesByName.put(module.getName().toLowerCase(), module);
    }

    /**
     * Returns all registered modules.
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Returns a module by its class.
     */
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modulesByClass.get(clazz);
    }

    /**
     * Returns a module by name (case-insensitive).
     */
    public Module getModule(String name) {
        return modulesByName.get(name.toLowerCase());
    }

    /**
     * Returns all modules in a given category.
     */
    public List<Module> getModulesByCategory(ModuleCategory category) {
        List<Module> result = new ArrayList<Module>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }

    /**
     * Returns all enabled modules.
     */
    public List<Module> getEnabledModules() {
        List<Module> result = new ArrayList<Module>();
        for (Module module : modules) {
            if (module.isEnabled()) {
                result.add(module);
            }
        }
        return result;
    }
}
