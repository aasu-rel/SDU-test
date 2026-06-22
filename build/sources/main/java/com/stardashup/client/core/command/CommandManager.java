package com.stardashup.client.core.command;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.command.commands.*;
import com.stardashup.client.core.log.SDULogger;
import com.stardashup.client.util.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages SDU chat commands.
 *
 * <p>Registers a {@code .sdu} command with Forge's client command handler.
 * Subcommands are dispatched to registered {@link Command} instances.</p>
 */
public class CommandManager {

    private final List<Command> commands;

    public CommandManager() {
        this.commands = new ArrayList<Command>();
    }

    /**
     * Registers all commands and the root command handler.
     */
    public void init() {
        // Register built-in commands
        commands.add(new HelpCommand(this));
        commands.add(new ToggleCommand());
        commands.add(new ConfigCommand());
        commands.add(new ProfileCommand());

        // Register with Forge's client command handler
        ClientCommandHandler.instance.registerCommand(new CommandBase() {
            @Override
            public String getCommandName() {
                return "sdu";
            }

            @Override
            public String getCommandUsage(ICommandSender sender) {
                return "/sdu <subcommand> [args]";
            }

            @Override
            public int getRequiredPermissionLevel() {
                return 0; // No permission needed for client commands
            }

            @Override
            public void processCommand(ICommandSender sender, String[] args) throws CommandException {
                if (args.length == 0) {
                    ChatUtils.send("\u00A7b\u00A7lStarDashUp Client \u00A77v" + SDUClient.MOD_VERSION);
                    ChatUtils.send("\u00A77Use \u00A7e/sdu help \u00A77for available commands.");
                    return;
                }

                String subCommand = args[0].toLowerCase();
                String[] subArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];

                for (Command command : commands) {
                    if (command.getName().equalsIgnoreCase(subCommand)) {
                        command.execute(subArgs);
                        return;
                    }
                    for (String alias : command.getAliases()) {
                        if (alias.equalsIgnoreCase(subCommand)) {
                            command.execute(subArgs);
                            return;
                        }
                    }
                }

                ChatUtils.send("\u00A7cUnknown subcommand: \u00A7e" + subCommand);
                ChatUtils.send("\u00A77Use \u00A7e/sdu help \u00A77for available commands.");
            }
        });

        SDULogger.staticInfo("Registered " + commands.size() + " commands.");
    }

    public List<Command> getCommands() {
        return commands;
    }
}
