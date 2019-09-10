package com.ziluck.fortuneblocks.commands;

import java.util.List;

import com.ziluck.fortuneblocks.configuration.Lang;
import org.bukkit.command.CommandSender;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.Permission;
import com.ziluck.fortuneblocks.commands.api.CommandArgument;
import com.ziluck.fortuneblocks.commands.api.ValidCommand;

public class FortuneBlocksReloadCommand extends ValidCommand
{
    public FortuneBlocksReloadCommand()
    {
        super("reload", "Reload the config and lang files.", Permission.ADMIN_RELOAD);
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments)
    {
        FortuneBlocks.reloadConfiguration();
        Lang.RELOAD.sendSuccess(sender);
    }
}
