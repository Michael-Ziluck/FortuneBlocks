package com.doctorzee.fortuneblocks.commands.validators;

import com.doctorzee.fortuneblocks.api.commands.Validator;
import com.doctorzee.fortuneblocks.configuration.Lang;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class MaterialTrackedValidator implements Validator<Material>
{

    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg)
    {
        if (!BlockHandler.isTracked(arg))
        {
            Lang.MATERIALS_NOT_TRACKED.send(sender);
            return false;
        }

        return true;
    }

}
