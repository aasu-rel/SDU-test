package com.stardashup.client.module.input;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

/**
 * Removes the artificial 10-tick delay between jumps when holding the spacebar.
 */
public class ImprovedJumpInput extends Module {

    private Field jumpTicksField;

    public ImprovedJumpInput() {
        super("Improved Jump", "More responsive jumping when holding space", ModuleCategory.INPUT);
        
        try {
            // "jumpTicks" is "field_70773_bE" in SRG mappings on EntityLivingBase
            jumpTicksField = net.minecraft.entity.EntityLivingBase.class.getDeclaredField("jumpTicks");
            jumpTicksField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            try {
                jumpTicksField = net.minecraft.entity.EntityLivingBase.class.getDeclaredField("field_70773_bE");
                jumpTicksField.setAccessible(true);
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

        if (mc.gameSettings.keyBindJump.isKeyDown() && jumpTicksField != null) {
            try {
                // Remove jump delay
                jumpTicksField.setInt(mc.thePlayer, 0);
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
