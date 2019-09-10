package com.ziluck.fortuneblocks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ziluck.fortuneblocks.utils.CollectionUtils;
import org.bukkit.Material;

public class ProcessLang
{

    public static void main(String[] args) throws IOException
    {
        File file = new File("C:/Users/Michael/Documents/lang.txt");

        List<String> lines = Files.readAllLines(file.toPath());

        HashMap<String, String> values = new HashMap<>();

        for (int i = 0; i < lines.size(); i += 2)
        {
            values.put(lines.get(i), lines.get(i + 1));
        }

        HashMap<Material, String> names = new HashMap<>();

        for (Material material : Material.values())
        {
            if (!material.isLegacy())
            {
                String value = CollectionUtils.firstNonNull(
                        values.get("block.minecraft." + material.name().replace("WALL_BANNER", "BANNER").toLowerCase()),
                        values.get("item.minecraft." + material.name().replace("WALL_BANNER", "BANNER").toLowerCase()));

                if (value == null)
                {
                    System.out.println(material);
                }
                else
                {
                    names.put(material, value);
                }
            }
        }

        TreeMap<String, String> sortedNames = new TreeMap<>();
        for (Entry<Material, String> entry : names.entrySet())
        {
            sortedNames.put(entry.getKey().name(), entry.getValue());
        }

        for (Entry<String, String> entry : sortedNames.entrySet())
        {
            System.out.printf("%s~%s\n", entry.getKey(), entry.getValue());
        }
    }

}
