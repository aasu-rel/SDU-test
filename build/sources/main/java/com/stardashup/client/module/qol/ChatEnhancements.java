package com.stardashup.client.module.qol;

import com.stardashup.client.core.module.Module;
import com.stardashup.client.core.module.ModuleCategory;
import com.stardashup.client.core.module.setting.BooleanSetting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ChatComponentText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Enhances the chat with timestamps and other features.
 */
public class ChatEnhancements extends Module {

    private final BooleanSetting timestamps = addSetting(new BooleanSetting("Timestamps", "Add timestamps to chat messages", true));
    private final BooleanSetting format24h = addSetting(new BooleanSetting("24h Format", "Use 24-hour time format", false));

    public ChatEnhancements() {
        super("Chat Enhancements", "Improves the chat experience", ModuleCategory.QOL);
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
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.type == 2) return; // Ignore action bar messages

        if (timestamps.getValue()) {
            String pattern = format24h.getValue() ? "HH:mm:ss" : "hh:mm:ss a";
            String time = new SimpleDateFormat(pattern).format(new Date());
            
            // Prepend timestamp to message
            ChatComponentText timeComponent = new ChatComponentText("\u00A78[\u00A77" + time + "\u00A78] \u00A7r");
            timeComponent.appendSibling(event.message);
            event.message = timeComponent;
        }
    }
}
