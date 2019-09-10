package com.ziluck.fortuneblocks.utils.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.utils.ItemUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

public class ItemDb
{
    private static Gson gson = new Gson();

    // Maps primary name to ItemData
    private final transient Map<String, ItemData> items = new HashMap<>();

    // Maps alias to primary name
    private final transient Map<String, String> itemAliases = new HashMap<>();

    // Every known alias
    private final transient Set<String> allAliases = new HashSet<>();

    private transient File file;

    private boolean ready = false;

    public ItemDb(File file)
    {
        this.file = file;
        reloadConfig();
    }

    public void reloadConfig()
    {
        if (file == null)
        {
            file = new File(FortuneBlocks.getInstance().getDataFolder(), "items.json");
        }

        this.rebuild();
        FortuneBlocks.getInstance().getLogger().info(String.format("Loaded %s items from items.json.", listNames().size()));
    }

    private void rebuild()
    {
        this.reset();

        try
        {
            String json = FileUtils.readLines(file, "UTF-8").stream()
                                   .filter(line -> !line.startsWith("#"))
                                   .collect(Collectors.joining());
            this.loadJSON(String.join("\n", json));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        ready = true;
    }

    private void reset()
    {
        ready = false;
        items.clear();
        itemAliases.clear();
        allAliases.clear();
    }

    public void loadJSON(String source)
    {
        JsonObject map = (new JsonParser()).parse(source).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : map.entrySet())
        {
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            boolean valid = false;

            if (element.isJsonObject())
            {
                ItemData data = gson.fromJson(element, ItemData.class);
                items.put(key, data);
                valid = true;
            }
            else
            {
                try
                {
                    String target = element.getAsString();
                    itemAliases.put(key, target);
                    valid = true;
                }
                catch (Exception ignored)
                {
                }
            }

            if (valid)
            {
                allAliases.add(key);
            }
            else
            {
                FortuneBlocks.getInstance().getLogger().warning(String.format("Failed to add item: \"%s\": %s", key, element.toString()));
            }
        }
    }

    public ItemStack get(String id) throws Exception
    {
        id = id.toLowerCase();
        final String[] split = id.split(":");

        ItemData data = getByName(split[0]);

        if (data == null)
        {
            throw new Exception("Unknown item name: " + id);
        }

        Material material = data.getMaterial();

        if (!material.isItem())
        {
            throw new Exception("Cannot spawn " + id + "; this is not a spawnable item.");
        }

        ItemStack stack = new ItemStack(material);
        stack.setAmount(material.getMaxStackSize());

        PotionData potionData = data.getPotionData();
        ItemMeta meta = stack.getItemMeta();

        if (potionData != null && meta instanceof PotionMeta)
        {
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setBasePotionData(potionData);
        }

        // For some reason, Damageable doesn't extend ItemMeta but CB implements them in the same
        // class. As to why, your guess is as good as mine.
        if (split.length > 1 && meta instanceof Damageable)
        {
            Damageable damageMeta = (Damageable) meta;
            damageMeta.setDamage(Integer.parseInt(split[1]));
        }

        EntityType entity = data.getEntity();
        if (entity != null && material.toString().contains("SPAWNER"))
        {
            BlockStateMeta bsm = (BlockStateMeta) meta;

            BlockState bs = bsm.getBlockState();

            ((CreatureSpawner) bs).setSpawnedType(entity);

            bsm.setBlockState(bs);
        }

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemData getByName(String name)
    {
        name = name.toLowerCase();
        if (items.containsKey(name))
        {
            return items.get(name);
        }
        else if (itemAliases.containsKey(name))
        {
            return items.get(itemAliases.get(name));
        }

        return null;
    }

    public List<String> nameList(ItemStack item)
    {
        List<String> names = new ArrayList<>();
        String primaryName = name(item);
        names.add(primaryName);

        for (Map.Entry<String, String> entry : itemAliases.entrySet())
        {
            if (entry.getValue().equalsIgnoreCase(primaryName))
            {
                names.add(entry.getKey());
            }
        }

        return names;
    }

    public String name(ItemStack item)
    {
        ItemData data = lookup(item);

        for (Map.Entry<String, ItemData> entry : items.entrySet())
        {
            if (entry.getValue().equals(data))
            {
                return entry.getKey();
            }
        }

        return null;
    }

    public ItemData lookup(ItemStack item)
    {
        Material type = item.getType();

        if (ItemUtils.isPotion(type) && item.getItemMeta() instanceof PotionMeta)
        {
            PotionData potion = ((PotionMeta) item.getItemMeta()).getBasePotionData();
            return new ItemData(type, potion);
        }
        else if (type.toString().contains("SPAWNER"))
        {
            EntityType entity = ((CreatureSpawner) ((BlockStateMeta) item.getItemMeta()).getBlockState()).getSpawnedType();
            return new ItemData(type, entity);
        }
        else
        {
            return new ItemData(type);
        }
    }

    public Collection<String> listNames()
    {
        return Collections.unmodifiableSet(allAliases);
    }


}
