package com.doctorzee.fortuneblocks.commands;

import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.api.commands.ValidBaseCommand;

public class FortuneBlocksCommand extends ValidBaseCommand
{

    public FortuneBlocksCommand()
    {
        super("fortuneblocks", "Manage fortune blocks.", Permission.ADMIN, new String[]{ "fb", "fortuneblock", "fblock" });

        addSubCommand(new FortuneBlocksAddCommand());
        addSubCommand(new FortuneBlocksRemoveCommand());
        addSubCommand(new FortuneBlocksListCommand());
        addSubCommand(new FortuneBlocksReloadCommand());
    }

}
