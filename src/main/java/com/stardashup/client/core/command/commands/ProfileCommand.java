package com.stardashup.client.core.command.commands;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.command.Command;
import com.stardashup.client.util.ChatUtils;

import java.util.List;

/**
 * Manages configuration profiles.
 */
public class ProfileCommand extends Command {

    public ProfileCommand() {
        super("profile", "Manages config profiles", "/sdu profile <save|load|list|delete> [name]", "p");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            sendUsage();
            return;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "save":
                if (args.length < 2) {
                    ChatUtils.send("\u00A7cUsage: /sdu profile save <name>");
                    return;
                }
                SDUClient.getInstance().getConfigManager().saveProfile(args[1]);
                ChatUtils.send("\u00A7aProfile '" + args[1] + "' saved!");
                break;

            case "load":
                if (args.length < 2) {
                    ChatUtils.send("\u00A7cUsage: /sdu profile load <name>");
                    return;
                }
                SDUClient.getInstance().getConfigManager().loadProfile(args[1]);
                ChatUtils.send("\u00A7aProfile '" + args[1] + "' loaded!");
                break;

            case "list":
                List<String> profiles = SDUClient.getInstance().getConfigManager().getProfileNames();
                if (profiles.isEmpty()) {
                    ChatUtils.send("\u00A77No saved profiles.");
                } else {
                    ChatUtils.send("\u00A7b\u00A7l--- Profiles ---");
                    for (String profile : profiles) {
                        String active = profile.equals(SDUClient.getInstance().getConfigManager().getActiveProfile())
                                ? " \u00A7a(active)" : "";
                        ChatUtils.send("\u00A7e" + profile + active);
                    }
                }
                break;

            case "delete":
                if (args.length < 2) {
                    ChatUtils.send("\u00A7cUsage: /sdu profile delete <name>");
                    return;
                }
                if (SDUClient.getInstance().getConfigManager().deleteProfile(args[1])) {
                    ChatUtils.send("\u00A7aProfile '" + args[1] + "' deleted.");
                } else {
                    ChatUtils.send("\u00A7cProfile '" + args[1] + "' not found.");
                }
                break;

            default:
                sendUsage();
                break;
        }
    }
}
