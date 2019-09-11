package com.ziluck.fortuneblocks.commands;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.Permission;
import com.ziluck.fortuneblocks.commands.api.CommandArgument;
import com.ziluck.fortuneblocks.commands.api.CommandArgumentBuilder;
import com.ziluck.fortuneblocks.commands.api.ValidCommand;
import com.ziluck.fortuneblocks.commands.parsers.MaterialParser;
import com.ziluck.fortuneblocks.commands.validators.MaterialTrackedValidator;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.utils.items.ItemNames;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FortuneBlocksRemoveCommand extends ValidCommand {
    public FortuneBlocksRemoveCommand() {
        super("remove", "Remove a material from being tracked.", Permission.ADMIN_REMOVE, new String[]{"delete", "untrack"});

        addArgument(CommandArgumentBuilder.createBuilder(Material.class)
                .setName("material")
                .setParser(new MaterialParser())
                .addValidator(new MaterialTrackedValidator())
                .build());
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> args) {
        Material material = (Material) arguments.get(0).getValue();

        FortuneBlocks.getBlockHandler().setTracked(material, false);

        Lang.MATERIALS_REMOVE.sendSuccess(sender, "{material}", ItemNames.lookup(material));
    }
}
