package com.stardashup.client.core.command;

import com.stardashup.client.util.ChatUtils;

/**
 * Abstract base class for SDU chat commands.
 */
public abstract class Command {

    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param args command arguments (excluding the command name)
     */
    public abstract void execute(String[] args);

    /**
     * Sends a usage message to the player.
     */
    protected void sendUsage() {
        ChatUtils.send("\u00A7cUsage: \u00A7e" + usage);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }
}
