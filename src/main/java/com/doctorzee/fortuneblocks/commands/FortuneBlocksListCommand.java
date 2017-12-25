package com.doctorzee.fortuneblocks.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;

import com.doctorzee.fortuneblocks.FortuneBlocks;
import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.api.commands.CommandArgument;
import com.doctorzee.fortuneblocks.api.commands.ValidCommand;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.utils.ItemNames;

public class FortuneBlocksListCommand extends ValidCommand
{

    public FortuneBlocksListCommand()
    {
        super("list", "List off all tracked materials.", Permission.ADMIN_LIST);
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments)
    {
        FortuneBlocks.getLangHandler().sendRenderMessage(sender, "header_footer");
        for (Material material : BlockHandler.getTrackedMaterials())
        {
            FortuneBlocks.getLangHandler().sendRenderMessage(sender, "materials.list",
                    "{material}", ItemNames.lookup(new MaterialData(material)));
        }
        FortuneBlocks.getLangHandler().sendRenderMessage(sender, "header_footer");
    }

}
