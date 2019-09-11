package com.ziluck.fortuneblocks.commands;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.Permission;
import com.ziluck.fortuneblocks.commands.api.CommandArgument;
import com.ziluck.fortuneblocks.commands.api.ValidCommand;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.utils.items.ItemNames;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FortuneBlocksListCommand extends ValidCommand {
    public FortuneBlocksListCommand() {
        super("list", "List off all tracked materials.", Permission.ADMIN_LIST);
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments) {
        Lang.HEADER_FOOTER.send(sender);

        for (Material material : FortuneBlocks.getBlockHandler().getTrackedMaterials()) {
            Lang.MATERIALS_LIST.send(sender, "{material}", ItemNames.lookup(material));
        }

        Lang.HEADER_FOOTER.send(sender);
    }
}
