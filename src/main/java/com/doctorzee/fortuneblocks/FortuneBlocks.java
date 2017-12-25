package com.doctorzee.fortuneblocks;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.doctorzee.fortuneblocks.api.FileHandler;
import com.doctorzee.fortuneblocks.api.LangHandler;
import com.doctorzee.fortuneblocks.api.commands.CommandHandler;
import com.doctorzee.fortuneblocks.commands.FortuneBlocksCommand;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.listeners.BlockListener;
import com.doctorzee.fortuneblocks.utils.ItemDb;

public class FortuneBlocks extends JavaPlugin
{

    private static FortuneBlocks instance;

    private static File configFile;
    private static FileHandler configHandler;

    private static File langFile;
    private static LangHandler langHandler;

    private static File itemFile;
    private static ItemDb itemHandler;

    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();

        configFile = new File(getDataFolder(), "config.yml");
        langFile = new File(getDataFolder(), "lang.yml");
        itemFile = new File(getDataFolder(), "items.csv");

        if (!getConfig().isSet("version") || !getConfig().getString("version").equals("2.0"))
        {
            getLogger().info("Detected old config. Overriding old settings with new version.");
            saveResource("config.yml", true);
            saveResource("lang.yml", true);
            saveResource("items.csv", true);
        }

        reloadConfiguration();

        CommandHandler.initialize();
        CommandHandler.getInstance().registerCommand(new FortuneBlocksCommand());

        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    public static void reloadConfiguration()
    {
        if (!itemFile.exists())
        {
            instance.saveResource("items.csv", false);
        }
        itemHandler = new ItemDb(itemFile);

        if (!configFile.exists())
        {
            instance.saveDefaultConfig();
        }
        configHandler = new FileHandler(configFile, instance);

        if (!langFile.exists())
        {
            instance.saveResource("lang.yml", false);
        }
        langHandler = new LangHandler(langFile, instance);

        BlockHandler.initialize();
    }

    public static ItemDb getItemHandler()
    {
        return itemHandler;
    }

    public static FileHandler getConfigHandler()
    {
        return configHandler;
    }

    public static LangHandler getLangHandler()
    {
        return langHandler;
    }

    public static FortuneBlocks getInstance()
    {
        return instance;
    }

}
