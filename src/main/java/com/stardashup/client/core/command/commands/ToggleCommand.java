package com.stardashup.client.core.command.commands;

import com.stardashup.client.SDUClient;
import com.stardashup.client.core.command.Command;
import com.stardashup.client.core.module.Module;
import com.stardashup.client.util.ChatUtils;

/**
 * Toggles a module on or off by name.
 */
public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "Toggles a module", "/sdu toggle <module>", "t");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            sendUsage();
            return;
        }

        String moduleName = args[0];
        Module module = SDUClient.getInstance().getModuleManager().getModule(moduleName);

        if (module == null) {
            ChatUtils.send("\u00A7cModule not found: \u00A7e" + moduleName);
            return;
        }

        module.toggle();
        String state = module.isEnabled() ? "\u00A7aON" : "\u00A7cOFF";
        ChatUtils.send("\u00A7b" + module.getName() + " \u00A77is now " + state);
    }
}
