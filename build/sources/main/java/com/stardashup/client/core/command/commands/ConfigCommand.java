package com.stardashup.client.core.command.commands;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.command.Command;
import com.stardashup.client.util.ChatUtils;

/**
 * Saves or loads the main configuration.
 */
public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "Saves or loads configuration", "/sdu config <save|load>", "cfg");
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
                SDUClient.getInstance().getConfigManager().saveAll();
                ChatUtils.send("\u00A7aConfiguration saved!");
                break;
            case "load":
                SDUClient.getInstance().getConfigManager().loadAll();
                ChatUtils.send("\u00A7aConfiguration loaded!");
                break;
            default:
                sendUsage();
                break;
        }
    }
}
