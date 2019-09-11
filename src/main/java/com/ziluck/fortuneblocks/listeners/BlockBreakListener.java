package com.ziluck.fortuneblocks.listeners;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.Permission;
import com.ziluck.fortuneblocks.configuration.Config;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.handlers.BlockHandler;
import com.ziluck.fortuneblocks.handlers.BlockWrapper;
import com.ziluck.fortuneblocks.utils.VersionUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BlockBreakListener implements Listener {
    // cooldown on sending them full messages
    private final HashMap<String, Long> fullMessageCooldown = new HashMap<>();

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        // if they aren't in survival, ignore them
        if (Config.REQUIRE_SURVIVAL.booleanValue() && event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        BlockHandler blockHandler = FortuneBlocks.getBlockHandler();
        BlockWrapper blockWrapper = BlockHandler.wrap(event.getBlock());

        // if it was placed, ignore it
        if (Config.TRACKING_ENABLED.booleanValue() && blockHandler.getTracker().wasPlaced(blockWrapper)) {
            return;
        }
        // if we don't track it, ignore it
        if (!blockHandler.isTracked(event.getBlock().getType())) {
            return;
        }
        // get drops
        ArrayList<ItemStack> drops = getDrops(event.getBlock(), event.getPlayer().getItemInHand());

        // stopping drops varies by version. this handles that logic
        VersionUtils.stopDrops(event);

        // if everyone has it or they have the permission, send it to their inventory
        if (Config.PICKUP.booleanValue() || event.getPlayer().hasPermission(Permission.PICKUP.getPermission())) {
            // check if their inventory is full.
            boolean full = true;
            for (ItemStack content : event.getPlayer().getInventory().getContents()) {
                // if they have an empty slot, it's fine
                if (content == null || content.getType() == Material.AIR || content.getAmount() == 0) {
                    full = false;
                    break;
                }

                // go through the drops
                for (ItemStack drop : drops) {
                    // if it's the same type and the new size will be less than 64, we good
                    if (drop.getType() == content.getType() && content.getAmount() + drop.getAmount() <= 64) {
                        full = false;
                        break;
                    }
                }
            }

            // when their inventory is full
            if (full) {
                // check if we should message them
                if (Config.FULL_MESSAGE_USE.booleanValue()) {
                    // default to true
                    boolean message = true;

                    // check if it contains their key
                    if (fullMessageCooldown.containsKey(event.getPlayer().getName())) {
                        // get how often they should be message
                        int cooldownTime = Config.FULL_MESSAGE_COOLDOWN.intValue();

                        // set how many seconds are left
                        long secondsLeft = ((fullMessageCooldown.get(event.getPlayer().getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);

                        // if the seconds left is > 0, don't message the player
                        if (secondsLeft > 0) {
                            message = false;
                        }
                    }

                    // if they passed everything, send them their message and put it on cooldown
                    if (message) {
                        fullMessageCooldown.put(event.getPlayer().getName(), System.currentTimeMillis());
                        Lang.INVENTORY_FULL.sendInfo(event.getPlayer());
                    }
                }

                // if the blocks should drop when their inventory is full, do it
                if (Config.FULL_DROP.booleanValue()) {
                    for (ItemStack drop : drops) {
                        event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
                    }
                }
            } else {
                // they aren't full, just add the items
                for (ItemStack drop : drops) {
                    event.getPlayer().getInventory().addItem(drop);
                }
            }
        } else {
            // drop the items naturally
            for (ItemStack drop : drops) {
                event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
        }

    }

    private ArrayList<ItemStack> getDrops(Block block, ItemStack tool) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) >= 1) {
            drops.add(new ItemStack(block.getType(), getDropCount(tool, true)));
        } else {
            for (ItemStack is : block.getDrops(tool)) {
                drops.add(new ItemStack(is.getType(), getDropCount(tool, false)));
            }
        }
        return drops;
    }

    private static int getDropCount(ItemStack is, boolean ignoreFortune) {
        if (ignoreFortune) {
            return 1;
        }
        int i = is.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        Random r = new Random();
        int j = r.nextInt(i + 2) - 1;
        if (j < 0) {
            j = 0;
        }
        return (j + 1);
    }

}
