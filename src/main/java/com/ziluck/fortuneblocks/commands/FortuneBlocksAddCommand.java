package com.ziluck.fortuneblocks.commands;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.Permission;
import com.ziluck.fortuneblocks.commands.api.CommandArgument;
import com.ziluck.fortuneblocks.commands.api.CommandArgumentBuilder;
import com.ziluck.fortuneblocks.commands.api.ValidCommand;
import com.ziluck.fortuneblocks.commands.parsers.MaterialParser;
import com.ziluck.fortuneblocks.commands.validators.ItemBlockValidator;
import com.ziluck.fortuneblocks.commands.validators.MaterialNotTrackedValidator;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.utils.items.ItemNames;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FortuneBlocksAddCommand extends ValidCommand {
    public FortuneBlocksAddCommand() {
        super("add", "Add a material to be tracked.", Permission.ADMIN_ADD, new String[]{"track"});

        addArgument(CommandArgumentBuilder.createBuilder(Material.class)
                .setName("material")
                .setParser(new MaterialParser())
                .addValidator(new MaterialNotTrackedValidator())
                .addValidator(new ItemBlockValidator())
                .build());
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments) {
        Material material = (Material) arguments.get(0).getValue();

        FortuneBlocks.getBlockHandler().setTracked(material, true);

        Lang.MATERIALS_ADD.sendSuccess(sender, "{material}", ItemNames.lookup(material));
    }
}
