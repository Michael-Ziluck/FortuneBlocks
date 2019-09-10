package com.ziluck.fortuneblocks.handlers.trackers;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.handlers.BlockWrapper;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FlatFileTracker extends BlockTracker {
    private File blockFile;

    private BufferedWriter writer;

    @Override
    public boolean initialize() {
        // set the file
        blockFile = new File(FortuneBlocks.getInstance().getDataFolder(), "blocks.dat");

        // if it does not exist, create it
        try {
            if (!blockFile.exists() && !blockFile.createNewFile()) {
                FortuneBlocks.getInstance().getLogger().severe("Could not create blocks file. Check folder permissions.");
                return false;
            }
        } catch (IOException ex) {
            FortuneBlocks.getInstance().getLogger().severe("Could not create blocks file. Check folder permissions.");
            return false;
        }

        try {
            writer = new BufferedWriter(new FileWriter(blockFile));
        } catch (IOException e) {
            FortuneBlocks.getInstance().getLogger().severe("Could not open writer to file.");
            return false;
        }

        // run on the next tick to ensure the worlds are loaded
        Bukkit.getScheduler().runTask(FortuneBlocks.getInstance(), () ->
        {
            // load each line
            try (Stream<String> stream = Files.lines(blockFile.toPath())) {
                stream.forEach(line -> FortuneBlocks.getBlockHandler().setPlaced(new BlockWrapper(line)));
            } catch (IOException ex) { // if something fails, shutdown
                FortuneBlocks.getInstance().getLogger().severe("An error occurred loading the placed blocks. Try deleting the existing block log.");
                FortuneBlocks.getInstance().getLogger().severe("Shutting down the plugin.");
                Bukkit.getPluginManager().disablePlugin(FortuneBlocks.getInstance());
                throw new RuntimeException(ex);
            }
        });

        return true;
    }

    @Override
    public boolean writeBlock(BlockWrapper blockWrapper) {
        Bukkit.getScheduler().runTaskAsynchronously(FortuneBlocks.getInstance(), () -> {
            try {
                writer.write(blockWrapper.toString());
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        return true;
    }
}
