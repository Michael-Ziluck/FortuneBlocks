package com.ziluck.fortuneblocks;

import com.google.gson.JsonSerializer;
import org.apache.commons.io.IOUtils;
import org.bukkit.Material;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ItemNamesTest {

    @Test
    public void checkMaterialsExist() throws IOException {
        File file = new File("src/main/resources", "itemNames.txt");

        List<String> invalid = new ArrayList<>();
        Files.lines(file.toPath(), StandardCharsets.UTF_8).forEach(str -> {
            str = str.split("~")[0];
            Material material = Material.getMaterial(str);
            if (material == null) {
                invalid.add(str);
            }
        });

        Assert.assertTrue("Invalid: " + invalid.size(), invalid.isEmpty());
    }

    @Test
    public void checkMissingMaterials() throws IOException {
        File langFile = new File("src/main/resources", "en_us.json");
        File displayFile = new File("src/main/resources", "itemNames.txt");

        InputStream is = new FileInputStream(langFile);
        String jsonText = IOUtils.toString(is, StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(jsonText);

        Map<Material, String> values = new HashMap<>();
        Files.lines(displayFile.toPath(), StandardCharsets.UTF_8).forEach(str -> {
            String[] split = str.split("~");
            values.put(Material.getMaterial(split[0]), split[1]);
        });

        List<Material> missingMaterials = new ArrayList<>();
        Material[] materials = Material.values();
        for (Material material : materials) {
            if (material.isLegacy() || values.containsKey(material)) {
                continue;
            }

            missingMaterials.add(material);
        }

        Assert.assertTrue("Missing materials: " + missingMaterials.size(), missingMaterials.isEmpty());
    }

    private static String findValue(JSONObject json, Material material) {
        String value = attemptGet(json, "block.minecraft." + material.name().toLowerCase());

        if (value == null) {
            value = attemptGet(json, "item.minecraft." + material.name().toLowerCase());
        }
        if (value == null) {
            throw new IllegalStateException("Could not find " + material + " in lang file.");
        }

        return value;
    }

    private static String attemptGet(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException ex) {
            return null;
        }
    }
}
