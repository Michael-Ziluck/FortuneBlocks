package com.doctorzee.fortuneblocks.api.commands;

import org.bukkit.command.CommandSender;

public interface SenderValidator
{

    /**
     * Validates that the sender is in the proper state.
     * 
     * @param sender the person sending the command.
     * @return {@code true} if the sender is valid.
     */
    public boolean validate(CommandSender sender);

}
