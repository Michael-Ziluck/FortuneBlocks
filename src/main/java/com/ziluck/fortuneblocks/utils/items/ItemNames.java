package com.ziluck.fortuneblocks.utils.items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * A class to easily look up the actual display name of items in Minecraft. This class
 * was originally made a long time ago using only number id and data values, but it has
 * been updated to work properly with Bukkit's Material enum.
 *
 * @author Michael Ziluck
 * @version 1.13.2
 */
public class ItemNames {
    private static Map<Material, String> ITEM_NAMES;

    static {
        ImmutableMap.Builder<Material, String> builder = ImmutableMap.builder();
        InputStream is = FortuneBlocks.getInstance().getResource("itemNames.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str;
        try {
            while ((str = br.readLine()) != null) {
                String[] split = str.split("~");
                Material material = Material.matchMaterial(split[0], false);
                if (material != null) {
                    builder.put(material, split[1]);
                } else {
                    FortuneBlocks.getInstance().getLogger().severe("Invalid internal material name. Check for plugin updates.");
                }
            }
            ITEM_NAMES = builder.build();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Given an item stack, return a friendly printable name for the item, as
     * the (English-language) vanilla Minecraft client would display it.
     *
     * @param stack the item stack
     * @return a friendly printable name for the item
     */
    public static String lookup(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            return "Fist";
        }
        if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.getDisplayName() != null) {
                return meta.getDisplayName();
            } else if (meta instanceof BookMeta) {
                return ((BookMeta) meta).getTitle();
            }
        }
        return ITEM_NAMES.containsKey(stack.getType()) ? ITEM_NAMES.get(stack.getType()) : stack.getType().toString();
    }

    public static String lookup(MaterialData data) {
        return lookup(data.toItemStack(1));
    }

    public static String lookup(Material material) {
        return lookup(new ItemStack(material));
    }

    /**
     * Given a block, return a friendly printable name for the block, as the
     * (English-language) vanilla Minecraft client would display it.
     *
     * @param block the block
     * @return a friendly printable name for the block
     */
    public static String lookup(Block block) {
        return lookup(new ItemStack(block.getType(), 1, block.getData()));
    }

    /**
     * Given an item stack return a friendly name for the item, in the form
     * "{amount} x {item-name}" where {amount} is the number of items in the
     * stack and {item-name} is the return value of
     * {@link #lookup(org.bukkit.inventory.ItemStack)} .
     *
     * @param stack the item stack
     * @return a friendly printable name for the item, with amount information
     */
    public static String lookupWithAmount(ItemStack stack) {
        return stack.getAmount() + " x " + lookup(stack);
    }

}
