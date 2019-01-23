package com.doctorzee.fortuneblocks.commands.validators;

import com.doctorzee.fortuneblocks.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.doctorzee.fortuneblocks.FortuneBlocks;
import com.doctorzee.fortuneblocks.api.commands.Validator;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;

public class MaterialNotTrackedValidator implements Validator<Material>
{

    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg)
    {
        if (BlockHandler.isTracked(arg))
        {
            Lang.MATERIALS_TRACKED.sendError(sender);
            return false;
        }

        return true;
    }

}
