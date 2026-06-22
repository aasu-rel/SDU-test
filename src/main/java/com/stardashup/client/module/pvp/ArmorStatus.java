package com.stardashup.client.module.pvp;

import com.stardashup.client.module.hud.HUDModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

/**
 * Displays currently equipped armor and its durability.
 */
public class ArmorStatus extends HUDModule {

    public ArmorStatus() {
        super("Armor Status", "Shows your equipped armor", 2, 110);
        this.width = 80;
        this.height = 64;
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale.getFloatValue(), scale.getFloatValue(), 1);

        int yOffset = 0;
        int maxWidth = 0;

        // Iterate backwards through armor inventory (Helmet to Boots)
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
            if (stack == null) continue;

            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, yOffset);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();

            int damage = stack.getMaxDamage() - stack.getItemDamage();
            String text = stack.isItemStackDamageable() ? String.valueOf(damage) : "";
            
            if (!text.isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(text, 20, yOffset + 4, textColor.getValue());
                int w = 20 + mc.fontRendererObj.getStringWidth(text);
                if (w > maxWidth) maxWidth = w;
            } else {
                if (16 > maxWidth) maxWidth = 16;
            }

            yOffset += 16;
        }

        this.width = maxWidth;
        this.height = yOffset > 0 ? yOffset : 16;

        GlStateManager.popMatrix();
    }
}
