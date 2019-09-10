package com.ziluck.fortuneblocks.commands;

import com.ziluck.fortuneblocks.Permission;
import com.ziluck.fortuneblocks.commands.api.ValidBaseCommand;

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
