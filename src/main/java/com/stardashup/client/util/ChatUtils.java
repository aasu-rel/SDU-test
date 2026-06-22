package com.stardashup.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

/**
 * Utility class for chat interactions.
 */
public class ChatUtils {

    /**
     * Sends a client-side chat message to the player.
     * The message is prefixed with the SDU prefix.
     */
    public static void send(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText(com.stardashup.client.SDUClient.PREFIX + message)
            );
        }
    }

    /**
     * Sends a raw message without the SDU prefix.
     */
    public static void sendRaw(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
}
