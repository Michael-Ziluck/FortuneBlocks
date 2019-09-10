package com.ziluck.fortuneblocks.commands.api;

import org.bukkit.command.CommandSender;

public interface SenderValidator {
    /**
     * Validates that the sender is in the proper state.
     *
     * @param sender the person sending the command.
     * @return {@code true} if the sender is valid.
     */
    boolean validate(CommandSender sender);
}
