package com.ziluck.fortuneblocks;

import org.apache.commons.io.IOUtils;
import org.bukkit.Material;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemNamesTest {
    @Test
    public void checkMaterialsExist() throws IOException {
        // file reference
        File itemNamesFile = new File("src/main/resources", "itemNames.txt");

        // list of file values that do not have matching materials
        List<String> invalid = new ArrayList<>();

        // iterate over each line of the display file
        Files.lines(itemNamesFile.toPath(), StandardCharsets.UTF_8).forEach(str -> {
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
        // display file reference
        File itemNamesFile = new File("src/main/resources", "itemNames.txt");

        // lang file references and loading
        File langFile = new File("src/main/resources", "en_us_1.15.2.json");
        InputStream is = new FileInputStream(langFile);
        String jsonText = IOUtils.toString(is, StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(jsonText);

        // cached values
        Map<Material, String> values = new HashMap<>();

        // iterate over each line of the display file
        Files.lines(itemNamesFile.toPath(), StandardCharsets.UTF_8).forEach(str -> {
            String[] split = str.split("~");
            values.put(Material.getMaterial(split[0]), split[1]);
        });

        List<Material> missingMaterials = new ArrayList<>();
        Material[] allMaterials = Material.values();
        for (Material material : allMaterials) {
            if (material.isLegacy() || values.containsKey(material)) {
                continue;
            }

            missingMaterials.add(material);
        }

        if (missingMaterials.size() > 0) {
            for (Material missing : missingMaterials) {
                System.out.println(missing.name() + "~" + ItemNamesTest.findValue(json, missing));
            }
            Assert.fail("Missing materials: " + missingMaterials.size());
        }
    }

    private static String findValue(JSONObject json, Material material) {
        String value = attemptGet(json, "block.minecraft." + material.name().toLowerCase());

        if (value == null) {
            value = attemptGet(json, "item.minecraft." + material.name().toLowerCase());
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
