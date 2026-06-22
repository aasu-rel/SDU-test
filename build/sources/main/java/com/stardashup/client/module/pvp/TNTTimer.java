package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * Displays remaining TNT fuse time visually above the TNT.
 */
public class TNTTimer extends Module {

    private final NumberSetting scale = addSetting(new NumberSetting("Scale", "Text scale", 1.0, 0.5, 3.0, 0.1));

    public TNTTimer() {
        super("TNT Timer", "Displays TNT fuse time", ModuleCategory.PVP);
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
    public void onRenderWorld(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        for (Object e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityTNTPrimed) {
                EntityTNTPrimed tnt = (EntityTNTPrimed) e;
                float time = tnt.fuse / 20.0f;
                String text = String.format("%.1fs", time);
                
                int color = 0xFF55FF55; // Green
                if (time < 1.0f) color = 0xFFFF5555; // Red
                else if (time < 2.5f) color = 0xFFFFFF55; // Yellow

                renderTag(tnt, text, color, event.partialTicks);
            }
        }
    }

    private void renderTag(EntityTNTPrimed tnt, String text, int color, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        double x = tnt.lastTickPosX + (tnt.posX - tnt.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = tnt.lastTickPosY + (tnt.posY - tnt.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        double z = tnt.lastTickPosZ + (tnt.posZ - tnt.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + tnt.height + 0.5F, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        
        float finalScale = scale.getFloatValue();
        GlStateManager.scale(finalScale, finalScale, 1.0f);
        
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        int width = mc.fontRendererObj.getStringWidth(text) / 2;
        GlStateManager.disableTexture2D();
        
        // Background
        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        net.minecraft.client.renderer.WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)(-width - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(-width - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(width + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double)(width + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        mc.fontRendererObj.drawString(text, -mc.fontRendererObj.getStringWidth(text) / 2, 0, color);
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        mc.fontRendererObj.drawString(text, -mc.fontRendererObj.getStringWidth(text) / 2, 0, color);
        
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
