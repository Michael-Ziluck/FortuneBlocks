package com.ziluck.fortuneblocks.listeners;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.configuration.Config;
import com.ziluck.fortuneblocks.handlers.BlockHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.ziluck.fortuneblocks.Permission.SILENT_PLACE;

public class BlockPlaceListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        BlockHandler blockHandler = FortuneBlocks.getBlockHandler();
        // if it is not a type we care about, ignore it.
        if (!blockHandler.isTracked(event.getBlock().getType())) {
            return;
        }

        // track placed
        if (Config.TRACKING_ENABLED.booleanValue() && !event.getPlayer().hasPermission(SILENT_PLACE.getPermission())) {
            blockHandler.getTracker().setPlaced(event.getBlock());
        }
    }

}
