package com.doctorzee.fortuneblocks;

import org.bukkit.Material;

public class MaterialsTest
{
    public static void main(String[] args)
    {
        for (Material material : Material.values())
        {
            System.out.println(material.name());
        }
    }
}
