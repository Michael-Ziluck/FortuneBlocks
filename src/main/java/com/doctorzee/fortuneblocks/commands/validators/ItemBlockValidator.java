package com.doctorzee.fortuneblocks.commands.validators;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.doctorzee.fortuneblocks.FortuneBlocks;
import com.doctorzee.fortuneblocks.api.commands.Validator;

public class ItemBlockValidator implements Validator<Material>
{

    @Override
    public boolean validateArgument(CommandSender sender, String[] label, Material arg)
    {
        if (!arg.isBlock())
        {
            FortuneBlocks.getLangHandler().sendRenderMessage(sender, "materials.not_block");
            return false;
        }
        return true;
    }

}
