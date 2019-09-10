package com.ziluck.fortuneblocks.commands.validators;

import com.ziluck.fortuneblocks.commands.api.Validator;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.handlers.BlockHandler;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class MaterialTrackedValidator implements Validator<Material>
{

    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg)
    {
        if (!BlockHandler.isTracked(arg))
        {
            Lang.MATERIALS_NOT_TRACKED.sendError(sender);
            return false;
        }

        return true;
    }

}
