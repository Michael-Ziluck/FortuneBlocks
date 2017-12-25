package com.doctorzee.fortuneblocks.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.doctorzee.fortuneblocks.FortuneBlocks;
import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.api.commands.CommandArgument;
import com.doctorzee.fortuneblocks.api.commands.ValidCommand;

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
        FortuneBlocks.getLangHandler().sendRenderMessage(sender, "reload");
    }

}
