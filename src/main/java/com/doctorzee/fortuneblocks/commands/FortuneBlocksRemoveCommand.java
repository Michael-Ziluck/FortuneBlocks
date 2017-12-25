package com.doctorzee.fortuneblocks.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;

import com.doctorzee.fortuneblocks.FortuneBlocks;
import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.api.commands.CommandArgument;
import com.doctorzee.fortuneblocks.api.commands.CommandArgumentBuilder;
import com.doctorzee.fortuneblocks.api.commands.ValidCommand;
import com.doctorzee.fortuneblocks.commands.parsers.MaterialParser;
import com.doctorzee.fortuneblocks.commands.validators.MaterialTrackedValidator;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.utils.ItemNames;

public class FortuneBlocksRemoveCommand extends ValidCommand
{

    public FortuneBlocksRemoveCommand()
    {
        super("remove", "Remove a material from being tracked.", Permission.ADMIN_REMOVE, new String[] { "delete", "untrack" });

        addArgument(CommandArgumentBuilder.createBuilder(Material.class)
                .setName("material")
                .setParser(new MaterialParser())
                .addValidator(new MaterialTrackedValidator())
                .build());
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> args)
    {
        Material material = (Material) arguments.get(0).getValue();

        BlockHandler.setTracked(material, false);

        FortuneBlocks.getLangHandler().sendRenderMessage(sender, "materials.remove",
                "{material}", ItemNames.lookup(new MaterialData(material)));
    }

}
