package com.ziluck.fortuneblocks.utils;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

public class VersionUtils
{

    public static void stopDrops(BlockBreakEvent event)
    {
        try
        {
            event.setDropItems(false);
        }
        catch (NoSuchMethodError ex)
        {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }

}
