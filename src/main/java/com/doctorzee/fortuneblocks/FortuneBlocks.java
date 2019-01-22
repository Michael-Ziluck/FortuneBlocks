package com.doctorzee.fortuneblocks;

import java.io.File;
import java.util.logging.FileHandler;

import com.doctorzee.fortuneblocks.api.commands.CommandHandler;
import com.doctorzee.fortuneblocks.commands.FortuneBlocksCommand;
import com.doctorzee.fortuneblocks.configuration.Config;
import com.doctorzee.fortuneblocks.configuration.Lang;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.listeners.BlockListener;
import com.doctorzee.fortuneblocks.utils.items.ItemDb;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FortuneBlocks extends JavaPlugin
{

    private static FortuneBlocks instance;

    private static ItemDb itemHandler;

    public void onEnable()
    {
        instance = this;

        reloadConfiguration();

        CommandHandler.initialize();
        CommandHandler.getInstance().registerCommand(new FortuneBlocksCommand());

        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    public static void reloadConfiguration()
    {
        // check lang
        File langFile = new File(instance.getDataFolder(), "lang.yml");
        if (!langFile.exists())
        {
            instance.saveResource("lang.yml", false);
        }
        Lang.update();

        // check config
        File configFile = new File(instance.getDataFolder(), "config.yml");
        if (!configFile.exists())
        {
            instance.saveDefaultConfig();
        }
        Config.update();

        // check items
        File itemFile = new File(instance.getDataFolder(), "items.json");
        if (!itemFile.exists())
        {
            instance.saveResource("items.json", false);
        }
        itemHandler = new ItemDb(itemFile);

        BlockHandler.initialize();
    }

    public static ItemDb getItemHandler()
    {
        return itemHandler;
    }

    public static FortuneBlocks getInstance()
    {
        return instance;
    }

}
