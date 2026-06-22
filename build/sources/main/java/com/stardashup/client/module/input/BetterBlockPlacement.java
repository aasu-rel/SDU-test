package com.stardashup.client.module.input;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

/**
 * Improves consistency when holding right-click to place blocks.
 * Sets rightClickDelayTimer to 0 to remove the 4-tick delay when continuous building.
 * (Note: Does not add "FastPlace" logic that spam-clicks, just removes the artificial delay 
 * when the button is actively held down for placing blocks).
 */
public class BetterBlockPlacement extends Module {

    private Field rightClickDelayTimerField;

    public BetterBlockPlacement() {
        super("Better Placement", "Improves block placement responsiveness", ModuleCategory.INPUT);

        try {
            // "rightClickDelayTimer" is "field_71467_ac" in SRG mappings
            rightClickDelayTimerField = Minecraft.class.getDeclaredField("rightClickDelayTimer");
            rightClickDelayTimerField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            try {
                // Try SRG name
                rightClickDelayTimerField = Minecraft.class.getDeclaredField("field_71467_ac");
                rightClickDelayTimerField.setAccessible(true);
            } catch (NoSuchFieldException ignored) {
            }
        }
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (event.player != mc.thePlayer) return;

        if (mc.gameSettings.keyBindUseItem.isKeyDown() && rightClickDelayTimerField != null) {
            try {
                // If the player is holding blocks, reduce the delay to make it more responsive.
                // Vanilla sets this to 4. We set it to 0 for instant response on continuous hold.
                int delay = rightClickDelayTimerField.getInt(mc);
                if (delay > 0) {
                    rightClickDelayTimerField.setInt(mc, 0);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
