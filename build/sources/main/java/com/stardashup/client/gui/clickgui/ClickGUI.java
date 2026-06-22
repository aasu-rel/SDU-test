package com.stardashup.client.gui.clickgui;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.*;
import com.stardashup.client.gui.clickgui.animation.Animation;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * Modern ClickGUI for StarDashUp Client.
 * <p>Design inspired by Discord / GitHub / Vercel with smooth animations.</p>
 */
public class ClickGUI extends GuiScreen {

    private ModuleCategory selectedCategory = ModuleCategory.PERFORMANCE;
    private final Animation openAnim = new Animation(0f, 0.1f);
    
    // UI layout constants
    private final int width = 600;
    private final int height = 400;
    private final int sidebarWidth = 140;

    // Scroll handling
    private float scrollY = 0;
    private float targetScrollY = 0;
    private Module expandedModule = null;

    @Override
    public void initGui() {
        openAnim.setValue(0f);
        openAnim.setTarget(1f);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        openAnim.update();
        
        // Background blur or tint
        this.drawDefaultBackground();
        
        ScaledResolution sr = new ScaledResolution(mc);
        float centerX = sr.getScaledWidth() / 2f;
        float centerY = sr.getScaledHeight() / 2f;
        
        // Scale animation
        net.minecraft.client.renderer.GlStateManager.pushMatrix();
        net.minecraft.client.renderer.GlStateManager.translate(centerX, centerY, 0);
        float scale = 0.9f + (0.1f * openAnim.getValue());
        net.minecraft.client.renderer.GlStateManager.scale(scale, scale, 1f);
        net.minecraft.client.renderer.GlStateManager.translate(-centerX, -centerY, 0);
        
        // Alpha animation
        int alpha = (int) (255 * openAnim.getValue());
        int bgAlpha = (int) (240 * openAnim.getValue());
        int mainBg = (bgAlpha << 24) | 0x111111;
        int sideBg = (bgAlpha << 24) | 0x181818;
        
        float startX = centerX - this.width / 2f;
        float startY = centerY - this.height / 2f;

        // Draw Main Background
        RenderUtils.drawRect(startX + sidebarWidth, startY, startX + this.width, startY + this.height, mainBg);
        
        // Draw Sidebar Background
        RenderUtils.drawRect(startX, startY, startX + sidebarWidth, startY + this.height, sideBg);

        // Sidebar Header
        mc.fontRendererObj.drawStringWithShadow("\u00A7bStarDashUp", startX + 15, startY + 20, (alpha << 24) | 0xFFFFFF);
        mc.fontRendererObj.drawString("\u00A77v" + SDUClient.MOD_VERSION, (int)(startX + 15), (int)(startY + 32), (alpha << 24) | 0xAAAAAA);

        // Sidebar Categories
        float catY = startY + 60;
        for (ModuleCategory cat : ModuleCategory.values()) {
            boolean isSelected = (cat == selectedCategory);
            int catColor = isSelected ? 0xFFFFFF : 0xAAAAAA;
            
            if (isSelected) {
                RenderUtils.drawRect(startX + 10, catY - 4, startX + sidebarWidth - 10, catY + 12, (alpha << 24) | 0x333333);
                // Accent line
                RenderUtils.drawRect(startX + 10, catY - 4, startX + 12, catY + 12, (alpha << 24) | 0x00AAFF);
            }

            mc.fontRendererObj.drawStringWithShadow(cat.getIcon() + " " + cat.getDisplayName(), startX + 20, catY, (alpha << 24) | catColor);
            catY += 24;
        }

        // Handle smooth scrolling
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            targetScrollY += wheel > 0 ? 30 : -30;
        }
        scrollY += (targetScrollY - scrollY) * 0.2f;

        // Main Panel (Modules)
        // Set scissor box to restrict rendering to the main panel
        GL11Scissor(startX + sidebarWidth, startY, this.width - sidebarWidth, this.height, sr);

        float modX = startX + sidebarWidth + 20;
        float modY = startY + 20 + scrollY;

        List<Module> modules = SDUClient.getInstance().getModuleManager().getModulesByCategory(selectedCategory);
        
        for (Module module : modules) {
            boolean isHovered = mouseX >= modX && mouseX <= startX + this.width - 20 && mouseY >= modY && mouseY <= modY + 40;
            
            // Draw module card
            RenderUtils.drawRect(modX, modY, startX + this.width - 20, modY + 40, (alpha << 24) | (isHovered ? 0x222222 : 0x1A1A1A));
            RenderUtils.drawBorderedRect(modX, modY, startX + this.width - 20, modY + 40, 1.0f, 0x00000000, (alpha << 24) | 0x333333);

            // Toggle switch rendering
            int switchBg = module.isEnabled() ? 0xFF00AAFF : 0xFF444444;
            RenderUtils.drawRect(startX + this.width - 60, modY + 14, startX + this.width - 30, modY + 26, (alpha << 24) | switchBg);
            RenderUtils.drawRect(module.isEnabled() ? startX + this.width - 45 : startX + this.width - 60, modY + 14, module.isEnabled() ? startX + this.width - 30 : startX + this.width - 45, modY + 26, (alpha << 24) | 0xFFFFFFFF);

            mc.fontRendererObj.drawStringWithShadow(module.getName(), modX + 15, modY + 10, (alpha << 24) | 0xFFFFFF);
            mc.fontRendererObj.drawString(module.getDescription(), (int)(modX + 15), (int)(modY + 24), (alpha << 24) | 0x888888);

            // Draw settings if expanded
            if (expandedModule == module && !module.getSettings().isEmpty()) {
                modY += 40;
                float setY = modY;
                
                RenderUtils.drawRect(modX, setY, startX + this.width - 20, setY + (module.getSettings().size() * 20) + 10, (alpha << 24) | 0x151515);

                for (Setting<?> set : module.getSettings()) {
                    mc.fontRendererObj.drawString(set.getName(), (int)(modX + 25), (int)(setY + 6), (alpha << 24) | 0xCCCCCC);
                    
                    if (set instanceof BooleanSetting) {
                        BooleanSetting bs = (BooleanSetting) set;
                        int sBg = bs.getValue() ? 0xFF00AAFF : 0xFF444444;
                        RenderUtils.drawRect(startX + this.width - 60, setY + 4, startX + this.width - 40, setY + 14, (alpha << 24) | sBg);
                    } else if (set instanceof ModeSetting) {
                        ModeSetting ms = (ModeSetting) set;
                        mc.fontRendererObj.drawString(ms.getValue(), (int)(startX + this.width - 40 - mc.fontRendererObj.getStringWidth(ms.getValue())), (int)(setY + 6), (alpha << 24) | 0x00AAFF);
                    } else if (set instanceof NumberSetting) {
                        NumberSetting ns = (NumberSetting) set;
                        String val = String.format("%.1f", ns.getValue());
                        mc.fontRendererObj.drawString(val, (int)(startX + this.width - 40 - mc.fontRendererObj.getStringWidth(val)), (int)(setY + 6), (alpha << 24) | 0x00AAFF);
                    } else if (set instanceof ColorSetting) {
                        ColorSetting cs = (ColorSetting) set;
                        RenderUtils.drawRect(startX + this.width - 60, setY + 4, startX + this.width - 40, setY + 14, (alpha << 24) | cs.getValue());
                    }
                    
                    setY += 20;
                }
                modY = setY;
            } else {
                modY += 45;
            }
        }
        
        // Prevent scrolling out of bounds (simplified)
        if (targetScrollY > 0) targetScrollY = 0;

        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);

        net.minecraft.client.renderer.GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        ScaledResolution sr = new ScaledResolution(mc);
        float centerX = sr.getScaledWidth() / 2f;
        float centerY = sr.getScaledHeight() / 2f;
        float startX = centerX - this.width / 2f;
        float startY = centerY - this.height / 2f;

        // Sidebar clicks
        if (mouseX >= startX && mouseX <= startX + sidebarWidth) {
            float catY = startY + 60;
            for (ModuleCategory cat : ModuleCategory.values()) {
                if (mouseY >= catY - 4 && mouseY <= catY + 20) {
                    selectedCategory = cat;
                    targetScrollY = 0; // Reset scroll
                    expandedModule = null;
                    return;
                }
                catY += 24;
            }
        }

        // Main panel clicks
        if (mouseX >= startX + sidebarWidth + 20 && mouseX <= startX + this.width - 20) {
            float modY = startY + 20 + scrollY;
            List<Module> modules = SDUClient.getInstance().getModuleManager().getModulesByCategory(selectedCategory);
            
            for (Module module : modules) {
                // Module header click
                if (mouseY >= modY && mouseY <= modY + 40) {
                    if (mouseButton == 0) {
                        // Toggle module if clicking on the right side
                        if (mouseX >= startX + this.width - 70) {
                            module.toggle();
                            SDUClient.getInstance().getConfigManager().saveAll();
                        } else {
                            // Expand/collapse settings
                            expandedModule = (expandedModule == module) ? null : module;
                        }
                    } else if (mouseButton == 1) {
                        // Right click to expand/collapse
                        expandedModule = (expandedModule == module) ? null : module;
                    }
                    return;
                }
                
                // Settings click
                if (expandedModule == module && !module.getSettings().isEmpty()) {
                    modY += 40;
                    float setY = modY;
                    for (Setting<?> set : module.getSettings()) {
                        if (mouseY >= setY && mouseY <= setY + 20) {
                            if (set instanceof BooleanSetting) {
                                BooleanSetting bs = (BooleanSetting) set;
                                bs.setValue(!bs.getValue());
                            } else if (set instanceof ModeSetting) {
                                ModeSetting ms = (ModeSetting) set;
                                ms.cycle();
                            }
                            // (Sliders for NumberSettings would go here, requiring mouse dragging logic)
                            SDUClient.getInstance().getConfigManager().saveAll();
                            return;
                        }
                        setY += 20;
                    }
                    modY = setY;
                } else {
                    modY += 45;
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void GL11Scissor(float x, float y, float width, float height, ScaledResolution sr) {
        int scaleFactor = sr.getScaleFactor();
        org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor((int)(x * scaleFactor), (int)(mc.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }
}
