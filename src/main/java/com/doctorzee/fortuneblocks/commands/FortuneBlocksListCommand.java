package com.doctorzee.fortuneblocks.commands;

import java.util.List;

import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.api.commands.CommandArgument;
import com.doctorzee.fortuneblocks.api.commands.ValidCommand;
import com.doctorzee.fortuneblocks.configuration.Lang;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.utils.items.ItemNames;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class FortuneBlocksListCommand extends ValidCommand
{

    public FortuneBlocksListCommand()
    {
        super("list", "List off all tracked materials.", Permission.ADMIN_LIST);
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Lang.HEADER_FOOTER.send(sender);

        for (Material material : BlockHandler.getTrackedMaterials())
        {
            Lang.MATERIALS_LIST.send(sender, "{material}", ItemNames.lookup(material));
        }

        Lang.HEADER_FOOTER.send(sender);
    }

}
