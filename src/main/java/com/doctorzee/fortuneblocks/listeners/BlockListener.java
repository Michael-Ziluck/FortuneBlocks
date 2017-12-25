package com.doctorzee.fortuneblocks.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.doctorzee.fortuneblocks.FortuneBlocks;
import com.doctorzee.fortuneblocks.Permission;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.utils.VersionUtils;

public class BlockListener implements Listener
{

    // cooldown on sending them full messages
    private final HashMap<String, Long> fullMessageCooldown = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event)
    {
        // if it is not a type we care about, ignore it.
        if (!BlockHandler.isTracked(event.getBlock().getType()))
        {
            return;
        }

        // track placed
        if (FortuneBlocks.getConfigHandler().getBoolean("tracking.enabled") && !event.getPlayer().hasPermission(Permission.SILENT_PLACE.getPermission()))
        {
            BlockHandler.setPlaced(event.getBlock());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event)
    {
        // should already ignore, but just in case
        if (event.isCancelled())
        {
            return;
        }
        // if they aren't in survival, ignore them
        if (FortuneBlocks.getConfigHandler().getBoolean("require_survival") && event.getPlayer().getGameMode() != GameMode.SURVIVAL)
        {
            return;
        }
        // if it was placed, ignore it
        if (FortuneBlocks.getConfigHandler().getBoolean("tracking.enabled") && BlockHandler.wasPlaced(event.getBlock()))
        {
            return;
        }
        // if we don't track it, ignore it
        if (!BlockHandler.isTracked(event.getBlock().getType()))
        {
            return;
        }
        // get drops
        ArrayList<ItemStack> drops = getDrops(event.getBlock(), event.getPlayer().getItemInHand());

        // stopping drops varies by version. this handles that logic
        VersionUtils.stopDrops(event);

        // if everyone has it or they have the permission, send it to their inventory
        if (FortuneBlocks.getConfigHandler().getBoolean("pickup") || event.getPlayer().hasPermission(Permission.PICKUP.getPermission()))
        {
            // check if their inventory is full.
            boolean full = true;
            for (ItemStack content : event.getPlayer().getInventory().getContents())
            {
                // if they have an empty slot, it's fine
                if (content == null || content.getType() == Material.AIR || content.getAmount() == 0)
                {
                    full = false;
                    break;
                }

                // go through the drops
                for (ItemStack drop : drops)
                {
                    // if it's the same type and the new size will be less than 64, we good
                    if (drop.getType() == content.getType() && content.getAmount() + drop.getAmount() <= 64)
                    {
                        full = false;
                        break;
                    }
                }
            }

            // when their inventory is full
            if (full)
            {
                // check if we should message them
                if (FortuneBlocks.getConfigHandler().getBoolean("full.message.use"))
                {
                    // default to true
                    boolean message = true;

                    // check if it contains their key
                    if (fullMessageCooldown.containsKey(event.getPlayer().getName()))
                    {
                        // get how often they should be message
                        int cooldownTime = FortuneBlocks.getConfigHandler().getInteger("full.message.cooldown");

                        // set how many seconds are left
                        long secondsLeft = ((fullMessageCooldown.get(event.getPlayer().getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);

                        // if the seconds left is > 0, don't message the player
                        if (secondsLeft > 0)
                        {
                            message = false;
                        }
                    }

                    // if they passed everything, send them their message and put it on cooldown
                    if (message)
                    {
                        fullMessageCooldown.put(event.getPlayer().getName(), System.currentTimeMillis());
                        FortuneBlocks.getLangHandler().sendRenderMessage(event.getPlayer(), "inventory_full");
                    }
                }

                // if the blocks should drop when their inventory is full, do it
                if (FortuneBlocks.getConfigHandler().getBoolean("full.drop"))
                {
                    for (ItemStack drop : drops)
                    {
                        event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
                    }
                }
            }
            else
            {
                // they aren't full, just add the items
                for (ItemStack drop : drops)
                {
                    event.getPlayer().getInventory().addItem(drop);
                }
            }
        }
        else
        {
            // drop the items naturally
            for (ItemStack drop : drops)
            {
                event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
        }

    }

    private ArrayList<ItemStack> getDrops(Block block, ItemStack tool)
    {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) >= 1)
        {
            drops.add(new ItemStack(block.getType(), getDropCount(tool, true)));
        }
        else
        {
            for (ItemStack is : block.getDrops(tool))
            {
                drops.add(new ItemStack(is.getType(), getDropCount(tool, false)));
            }
        }
        return drops;
    }

    private static int getDropCount(ItemStack is, boolean ignoreFortune)
    {
        if (ignoreFortune)
        {
            return 1;
        }
        int i = is.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        Random r = new Random();
        int j = r.nextInt(i + 2) - 1;
        if (j < 0)
        {
            j = 0;
        }
        return (j + 1);
    }

}
