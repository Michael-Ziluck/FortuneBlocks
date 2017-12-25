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
import com.doctorzee.fortuneblocks.commands.validators.ItemBlockValidator;
import com.doctorzee.fortuneblocks.commands.validators.MaterialNotTrackedValidator;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.utils.ItemNames;

public class FortuneBlocksAddCommand extends ValidCommand
{

    public FortuneBlocksAddCommand()
    {
        super("add", "Add a material to be tracked.", Permission.ADMIN_ADD, new String[] { "track" });

        addArgument(CommandArgumentBuilder.createBuilder(Material.class)
                .setName("material")
                .setParser(new MaterialParser())
                .addValidator(new MaterialNotTrackedValidator())
                .addValidator(new ItemBlockValidator())
                .build());
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Material material = (Material) arguments.get(0).getValue();

        BlockHandler.setTracked(material, true);

        FortuneBlocks.getLangHandler().sendRenderMessage(sender, "materials.add",
                "{material}", ItemNames.lookup(new MaterialData(material)));
    }

}
