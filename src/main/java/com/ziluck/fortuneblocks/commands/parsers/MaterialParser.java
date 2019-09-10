package com.ziluck.fortuneblocks.commands.parsers;

import java.util.LinkedList;
import java.util.List;

import com.ziluck.fortuneblocks.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.commands.api.Parser;

public class MaterialParser implements Parser<Material>
{

    @Override
    public Material parseArgument(CommandSender sender, String[] label, String rawArgument)
    {
        ItemStack is;
        try
        {
            is = FortuneBlocks.getItemHandler().get(rawArgument);
        }
        catch (Exception ex)
        {
            Lang.MATERIALS_INVALID_ITEM.sendError(sender);
            return null;
        }

        return is.getType();
    }

    @Override
    public List<String> getRecommendations(CommandSender sender, String lastWord)
    {
        lastWord = lastWord.toLowerCase();
        List<String> materials = new LinkedList<>();
        for (Material material : Material.values())
        {
            if (material.name().toLowerCase().startsWith(lastWord))
            {
                materials.add(material.name());
            }
        }
        return materials;
    }

}
