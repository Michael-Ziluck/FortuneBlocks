package com.ziluck.fortuneblocks;

import java.io.File;

import com.ziluck.fortuneblocks.commands.api.CommandHandler;
import com.ziluck.fortuneblocks.commands.FortuneBlocksCommand;
import com.ziluck.fortuneblocks.configuration.Config;
import com.ziluck.fortuneblocks.configuration.Lang;
import com.ziluck.fortuneblocks.handlers.BlockHandler;
import com.ziluck.fortuneblocks.listeners.BlockBreakListener;
import com.ziluck.fortuneblocks.listeners.BlockPlaceListener;
import com.ziluck.fortuneblocks.utils.items.ItemDb;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FortuneBlocks extends JavaPlugin {
    private static FortuneBlocks instance;

    private static ItemDb itemHandler;

    private static BlockHandler blockHandler;

    public void onEnable() {
        instance = this;

        reloadConfiguration();

        CommandHandler.initialize();
        CommandHandler.getInstance().registerCommand(new FortuneBlocksCommand());

        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    public static void reloadConfiguration() {
        // check lang
        File langFile = new File(instance.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            instance.saveResource("lang.yml", false);
        }
        Lang.update();

        // check config
        File configFile = new File(instance.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            instance.saveDefaultConfig();
        }
        Config.update();

        // check items
        File itemFile = new File(instance.getDataFolder(), "items.json");
        if (!itemFile.exists()) {
            instance.saveResource("items.json", false);
        }
        itemHandler = new ItemDb(itemFile);

        blockHandler = new BlockHandler();
    }

    public static ItemDb getItemHandler() {
        return itemHandler;
    }

    public static BlockHandler getBlockHandler() {
        return blockHandler;
    }

    public static FortuneBlocks getInstance() {
        return instance;
    }
}
