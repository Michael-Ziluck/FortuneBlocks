package com.ziluck.fortuneblocks.commands.validators;

import com.ziluck.fortuneblocks.commands.api.Validator;
import com.ziluck.fortuneblocks.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class ItemBlockValidator implements Validator<Material>
{

    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg)
    {
        if (!arg.isBlock())
        {
            Lang.MATERIALS_NOT_BLOCK.sendError(sender);
            return false;
        }
        return true;
    }

}
