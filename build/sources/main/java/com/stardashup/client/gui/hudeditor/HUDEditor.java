package com.stardashup.client.gui.hudeditor;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.module.Module;
import com.stardashup.client.module.hud.HUDModule;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

/**
 * Drag-and-drop HUD Editor screen.
 */
public class HUDEditor extends GuiScreen {

    private HUDModule draggingModule = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        ScaledResolution sr = new ScaledResolution(mc);

        // Handle dragging
        if (draggingModule != null) {
            draggingModule.setX(mouseX - dragOffsetX);
            draggingModule.setY(mouseY - dragOffsetY);
        }

        // Render modules
        for (Module m : SDUClient.getInstance().getModuleManager().getEnabledModules()) {
            if (m instanceof HUDModule) {
                HUDModule hud = (HUDModule) m;

                net.minecraft.client.renderer.GlStateManager.pushMatrix();
                hud.renderDummy(sr);
                net.minecraft.client.renderer.GlStateManager.popMatrix();

                // Draw bounding box if hovered
                if (hud.isMouseOver(mouseX, mouseY) || hud == draggingModule) {
                    float scaledWidth = hud.getWidth() * ((com.stardashup.client.core.module.setting.NumberSetting)hud.getSetting("Scale")).getFloatValue();
                    float scaledHeight = hud.getHeight() * ((com.stardashup.client.core.module.setting.NumberSetting)hud.getSetting("Scale")).getFloatValue();
                    
                    com.stardashup.client.render.RenderUtils.drawBorderedRect(
                            hud.getX() - 1, hud.getY() - 1, 
                            hud.getX() + scaledWidth + 1, hud.getY() + scaledHeight + 1, 
                            1.0f, 0x40FFFFFF, 0xFF00AAFF);
                }
            }
        }

        mc.fontRendererObj.drawStringWithShadow("HUD Editor", sr.getScaledWidth() / 2f - mc.fontRendererObj.getStringWidth("HUD Editor") / 2f, 10, 0xFFFFFFFF);
        mc.fontRendererObj.drawStringWithShadow("Drag elements to reposition them.", sr.getScaledWidth() / 2f - mc.fontRendererObj.getStringWidth("Drag elements to reposition them.") / 2f, 22, 0xFFAAAAAA);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            // Find module under cursor (iterate backwards for correct z-index)
            java.util.List<Module> enabled = SDUClient.getInstance().getModuleManager().getEnabledModules();
            for (int i = enabled.size() - 1; i >= 0; i--) {
                Module m = enabled.get(i);
                if (m instanceof HUDModule) {
                    HUDModule hud = (HUDModule) m;
                    if (hud.isMouseOver(mouseX, mouseY)) {
                        draggingModule = hud;
                        dragOffsetX = mouseX - hud.getX();
                        dragOffsetY = mouseY - hud.getY();
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0 && draggingModule != null) {
            draggingModule = null;
            // Save config after dragging
            SDUClient.getInstance().getConfigManager().saveAll();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
