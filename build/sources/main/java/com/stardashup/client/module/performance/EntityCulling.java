package com.stardashup.client.module.performance;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Basic entity culling using Forge render events to skip rendering entities far away or not matching criteria.
 * (A full occlusion culling would require Mixins to hook RenderGlobal, this provides a lighter distance-based approach).
 */
public class EntityCulling extends Module {

    private final BooleanSetting cullPlayers = addSetting(new BooleanSetting("Cull Players", "Skip rendering players too far away", false));
    private final BooleanSetting cullMobs = addSetting(new BooleanSetting("Cull Mobs", "Skip rendering mobs too far away", true));
    private final BooleanSetting cullAnimals = addSetting(new BooleanSetting("Cull Animals", "Skip rendering animals too far away", true));
    private final BooleanSetting cullItems = addSetting(new BooleanSetting("Cull Items/XP", "Skip rendering items and XP too far away", true));
    
    public EntityCulling() {
        super("Entity Culling", "Skips rendering hidden or distant entities", ModuleCategory.PERFORMANCE);
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
    public void onRenderLivingPre(RenderLivingEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        
        Entity entity = event.entity;
        if (entity == mc.thePlayer) return;

        double distSq = mc.thePlayer.getDistanceSqToEntity(entity);

        // Simple distance culling (distance squared)
        if (entity instanceof EntityPlayer) {
            if (cullPlayers.getValue() && distSq > 4096) { // 64 blocks
                event.setCanceled(true);
            }
        } else if (entity instanceof EntityMob) {
            if (cullMobs.getValue() && distSq > 2048) { // ~45 blocks
                event.setCanceled(true);
            }
        } else if (entity instanceof EntityAnimal) {
            if (cullAnimals.getValue() && distSq > 1024) { // 32 blocks
                event.setCanceled(true);
            }
        }
        // Items and XP are not instances of EntityLivingBase, so they won't trigger RenderLivingEvent.
        // A Mixin to RenderEntityItem would be required for them.
    }
}
