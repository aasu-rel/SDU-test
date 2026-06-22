package com.stardashup.client.module.performance;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;

/**
 * Placeholder for Chunk Render Optimizer.
 * <p>Requires SpongePowered Mixin into {@code RenderGlobal} and chunk update schedulers.</p>
 */
public class ChunkRenderOptimizer extends Module {

    public final BooleanSetting limitUpdates = addSetting(new BooleanSetting("Limit Updates", "Limit chunk updates per frame", true));
    public final BooleanSetting lazyLoading = addSetting(new BooleanSetting("Lazy Loading", "Delay rendering of distant chunks", true));

    public ChunkRenderOptimizer() {
        super("Chunk Optimizer", "Optimizes chunk render scheduling (Requires Mixin)", ModuleCategory.PERFORMANCE);
    }
}
