package com.doctorzee.fortuneblocks.commands.validators;

import com.doctorzee.fortuneblocks.api.commands.Validator;
import com.doctorzee.fortuneblocks.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class ItemBlockValidator implements Validator<Material>
{

    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg)
    {
        if (!arg.isBlock())
        {
            Lang.MATERIALS_NOT_BLOCK.send(sender);
            return false;
        }
        return true;
    }

}
