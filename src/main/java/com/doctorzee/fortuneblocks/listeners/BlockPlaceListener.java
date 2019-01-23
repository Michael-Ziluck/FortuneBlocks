package com.doctorzee.fortuneblocks.listeners;

import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.configuration.Config;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event)
    {
        // if it is not a type we care about, ignore it.
        if (!BlockHandler.isTracked(event.getBlock().getType()))
        {
            return;
        }

        // track placed
        if (Config.TRACKING_ENABLED.booleanValue() && !event.getPlayer().hasPermission(Permission.SILENT_PLACE.getPermission()))
        {
            BlockHandler.setPlaced(event.getBlock());
        }
    }

}
