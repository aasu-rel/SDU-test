package com.stardashup.client.core.command.commands;

import com.stardashup.client.core.command.Command;
import com.stardashup.client.core.command.CommandManager;
import com.stardashup.client.util.ChatUtils;

/**
 * Lists all available SDU commands.
 */
public class HelpCommand extends Command {

    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("help", "Shows available commands", "/sdu help", "h", "?");
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.send("\u00A7b\u00A7l--- StarDashUp Commands ---");
        for (Command command : commandManager.getCommands()) {
            ChatUtils.send("\u00A7e" + command.getUsage() + " \u00A77- " + command.getDescription());
        }
    }
}
