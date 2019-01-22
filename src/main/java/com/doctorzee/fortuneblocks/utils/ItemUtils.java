package com.doctorzee.fortuneblocks.utils;

import java.util.Set;

import org.bukkit.Material;

public class ItemUtils
{
    private static final Set<Material> POTIONS;

    static
    {
        POTIONS = EnumUtil.getAllMatching(Material.class, "POTION", "SPLASH_POTION", "LINGERING_POTION", "TIPPED_ARROW");
    }

    /**
     * Checks if the given material is a potion.
     * @param material the material to check.
     * @return
     */
    public static boolean isPotion(Material material)
    {
        return POTIONS.contains(material);
    }
}
