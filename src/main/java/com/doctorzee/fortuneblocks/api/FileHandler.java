package com.doctorzee.fortuneblocks.api;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Michael Ziluck
 */
public class FileHandler
{

    private static List<FileHandler> instances = new LinkedList<>();

    protected File file;

    protected FileConfiguration fileConfig;

    protected HashMap<String, Object> history;

    /**
     * Construct a new optimized file handler.
     *
     * @param file
     */
    public FileHandler(File file, JavaPlugin plugin)
    {
        if (!file.exists())
        {
            plugin.saveResource(file.getName(), false);
        }
        instances.add(this);
        history = new HashMap<>();
        this.file = file;
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Reloads the {@link}. This resets the file and clears the history object.
     *
     * @param
     */
    public void reload()
    {
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
        history.clear();
    }

    /**
     * Gets a formatted string from the config file. Replaces any color placeholders as well. If the string does not
     * exist in the config, returns null.
     *
     * @param key
     * @return the formatted string.
     */
    public String getString(String key)
    {
        String message;
        Object o = history.get(key);
        if (o != null && o instanceof String)
        {
            return (String) o;
        }
        message = fileConfig.getString(key);
        if (message != null)
        {
            message = message.replaceAll("\\{nl\\}", "\n");
            message = ChatColor.translateAlternateColorCodes('&', message);
            history.put(key, message);
            return message;
        }
        else
        {
            return key;
        }
    }

    /**
     * Gets a double value from history or the config. If it does not exist, returns 0.
     *
     * @param key
     * @return the value.
     */
    public Double getDouble(String key)
    {
        double value;
        Object o = history.get(key);
        if (o != null && o instanceof Double)
        {
            return (Double) o;
        }
        value = fileConfig.getDouble(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a long value from history or the config. If it does not exist, returns 0.
     *
     * @param key
     * @return the value.
     */
    public Long getLong(String key)
    {
        long value;
        Object o = history.get(key);
        if (o != null && o instanceof Long)
        {
            return (Long) o;
        }
        value = fileConfig.getLong(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a integer value from history or the config. If it does not exist, returns 0.
     *
     * @param key
     * @return the value.
     */
    public Integer getInteger(String key)
    {
        int value;
        Object o = history.get(key);
        if (o != null && o instanceof Integer)
        {
            return (Integer) o;
        }
        value = fileConfig.getInt(key);
        history.put(key, value);
        return value;
    }

    public ConfigurationSection getConfigurationSection(String key)
    {
        return fileConfig.getConfigurationSection(key);
    }

    /**
     * Gets a boolean value from history or the config. If it does not exist, returns 0.
     *
     * @param key
     * @return the value.
     */
    public Boolean getBoolean(String key)
    {
        boolean value;
        Object o = history.get(key);
        if (o != null && o instanceof Integer)
        {
            return (Boolean) o;
        }
        value = fileConfig.getBoolean(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a formatted string list from the config file. Replaces any color placeholders as well. If the string list
     * does not exist in the config, returns null.
     *
     * @param key
     * @return the formatted string list.
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(String key)
    {
        Object o = history.get(key);
        if (o != null && o instanceof List<?>)
        {
            return (List<String>) o;
        }
        List<String> list = new LinkedList<>();
        for (String str : fileConfig.getStringList(key))
        {
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        if (list.size() == 0)
        {
            list.add(getString(key));
        }
        return list;
    }

    public void setStringList(String key, List<String> values)
    {
        fileConfig.set(key, values);
        history.put(key, values);
        try
        {
            fileConfig.save(file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setString(String key, String value)
    {
        fileConfig.set(key, value);
        history.put(key, value);
        try
        {
            fileConfig.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setInt(String key, int value)
    {
        fileConfig.set(key, value);
        history.put(key, value);
        try
        {
            fileConfig.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setDouble(String key, double value)
    {
        fileConfig.set(key, value);
        history.put(key, value);
        try
        {
            fileConfig.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setBoolean(String key, boolean value)
    {
        fileConfig.set(key, value);
        history.put(key, value);
        try
        {
            fileConfig.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void reloadAll()
    {
        for (FileHandler lang : instances)
        {
            lang.reload();
        }
    }

}