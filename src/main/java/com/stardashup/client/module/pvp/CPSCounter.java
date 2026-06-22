package com.stardashup.client.module.pvp;

import com.stardashup.client.core.module.setting.BooleanSetting;
import com.stardashup.client.module.hud.HUDModule;
import com.stardashup.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Displays Clicks Per Second (CPS) for left and right mouse buttons.
 */
public class CPSCounter extends HUDModule {

    private final BooleanSetting showBackground = addSetting(new BooleanSetting("Background", "Show background rect", true));
    private final BooleanSetting showRightClick = addSetting(new BooleanSetting("Show RMB", "Show right-click CPS", true));

    private final List<Long> leftClicks = new ArrayList<Long>();
    private final List<Long> rightClicks = new ArrayList<Long>();

    private boolean wasLeftDown;
    private boolean wasRightDown;

    public CPSCounter() {
        super("CPS Counter", "Displays your clicks per second", 2, 74);
    }

    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        leftClicks.clear();
        rightClicks.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        boolean isLeftDown = Mouse.isButtonDown(0);
        boolean isRightDown = Mouse.isButtonDown(1);

        if (isLeftDown && !wasLeftDown) {
            leftClicks.add(System.currentTimeMillis());
        }
        if (isRightDown && !wasRightDown) {
            rightClicks.add(System.currentTimeMillis());
        }

        wasLeftDown = isLeftDown;
        wasRightDown = isRightDown;

        // Cleanup old clicks (>1 second)
        long now = System.currentTimeMillis();
        leftClicks.removeIf(time -> now - time > 1000);
        rightClicks.removeIf(time -> now - time > 1000);
    }

    @Override
    public void render(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();

        String text = leftClicks.size() + " CPS";
        if (showRightClick.getValue()) {
            text += " | " + rightClicks.size() + " CPS";
        }

        width = mc.fontRendererObj.getStringWidth(text) + 4;
        height = mc.fontRendererObj.FONT_HEIGHT + 4;

        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale.getFloatValue(), scale.getFloatValue(), 1);

        if (showBackground.getValue()) {
            RenderUtils.drawRect(0, 0, width, height, backgroundColor.getValue());
        }

        mc.fontRendererObj.drawStringWithShadow(text, 2, 2, textColor.getValue());
    }
}
