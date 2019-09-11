package com.ziluck.fortuneblocks.commands.validators;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.handlers.BlockHandler;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.ziluck.fortuneblocks.commands.api.Validator;

public class MaterialNotTrackedValidator implements Validator<Material> {
    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg) {
        if (FortuneBlocks.getBlockHandler().isTracked(arg)) {
            Lang.MATERIALS_TRACKED.sendError(sender);
            return false;
        }

        return true;
    }
}
