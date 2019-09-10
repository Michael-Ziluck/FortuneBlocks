package com.ziluck.fortuneblocks.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

public class BlockHandler
{

    private static File blockFile;

    private static Set<BlockWrapper> blockWrappers;

    private static BukkitTask fileSaveTask;

    private static Set<Material> materials;

    /**
     * Mark a block as placed.
     *
     * @param block the block to mark.
     */
    public static void setPlaced(Block block)
    {
        blockWrappers.add(wrap(block));
    }

    /**
     * Mark a block as no longer placed by a player.
     *
     * @param block the block to mark.
     */
    public static void clearPlaced(Block block)
    {
        blockWrappers.remove(wrap(block));
    }

    /**
     * @param block the block to check.
     *
     * @return {@code true} if this block was placed by a player.
     */
    public static boolean wasPlaced(Block block)
    {
        return blockWrappers.contains(wrap(block));
    }

    /**
     * @return {@code true} if we track the material.
     */
    public static boolean isTracked(Material material)
    {
        return materials.contains(material);
    }

    /**
     * @param material the material to update.
     * @param status   {@code true} to add, {@code false} to remove.
     */
    public static void setTracked(Material material, boolean status)
    {
        if (status)
        {
            materials.add(material);
        }
        else
        {
            materials.remove(material);
        }
        FortuneBlocks.getInstance().getConfig().set("blocks", getTrackedMaterialNames());
        FortuneBlocks.getInstance().saveConfig();
    }

    /**
     * @return an immutable view of the tracked materials.
     */
    public static Set<Material> getTrackedMaterials()
    {
        return Collections.unmodifiableSet(materials);
    }

    public static List<String> getTrackedMaterialNames()
    {
        List<String> values = new LinkedList<>();
        for (Material material : materials)
        {
            values.add(material.name());
        }
        return values;
    }

    private static BlockWrapper wrap(Block block)
    {
        return new BlockWrapper(block.getX(), block.getY(), block.getZ(), block.getWorld());
    }

    public static void initialize()
    {
        // set the file
        blockFile = new File(FortuneBlocks.getInstance().getDataFolder(), "blocks.dat");

        // if it does not exist, create it
        if (!blockFile.exists())
        {
            try
            {
                blockFile.createNewFile();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        // initialize the set
        blockWrappers = Collections.newSetFromMap(new ConcurrentHashMap<>());

        // run on the next tick to ensure the worlds are loaded
        Bukkit.getScheduler().runTask(FortuneBlocks.getInstance(), () ->
        {
            // open up the buffered reader
            try (BufferedReader br = new BufferedReader(new FileReader(blockFile)))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    blockWrappers.add(new BlockWrapper(line));
                }
            }
            // if something fails, let the console know
            catch (Exception ex)
            {
                FortuneBlocks.getInstance().getLogger().severe("An error occurred loading the placed blocks.");
                FortuneBlocks.getInstance().getLogger().severe("Don't manually edit the block file next time.");

                // we have to have a file, so delete the corrupted one and create a new one
                try
                {
                    blockFile.delete();
                    blockFile.createNewFile();
                }
                catch (IOException ex2)
                {
                    ex2.printStackTrace();
                }
            }
        });

        // if the block handler has been initialized before, cancel the task
        if (fileSaveTask != null)
        {
            fileSaveTask.cancel();
        }

        Runnable fileRunnable = () ->
        {
            if (blockFile.delete())
            {
                try
                {
                    blockFile.createNewFile();
                    FileWriter fw = new FileWriter(blockFile);
                    for (BlockWrapper blockWrapper : blockWrappers)
                    {
                        fw.append(String.valueOf(blockWrapper.getX())).append(",").append(String.valueOf(blockWrapper.getY())).append(",").append(String.valueOf(blockWrapper.getZ())).append(",").append(blockWrapper.getWorld().getName());
                    }
                    fw.flush();
                    fw.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                FortuneBlocks.getInstance().getLogger().severe("Could not delete the block file.");
            }
        };

        // save it periodically
        fileSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(FortuneBlocks.getInstance(),
                                                                        fileRunnable,
                                                                        Config.TRACKING_SAVE_RATE.intValue(),
                                                                        Config.TRACKING_SAVE_RATE.intValue());

        // create enum set
        materials = EnumSet.noneOf(Material.class);

        // load the materials we care about
        for (String string : FortuneBlocks.getInstance().getConfig().getStringList("blocks"))
        {
            try
            {
                materials.add(FortuneBlocks.getItemHandler().get(string).getType());
            }
            catch (Exception ex)
            {
                FortuneBlocks.getInstance().getLogger().severe("Invalid Block: '" + string + "'.");
            }
        }
    }

    /**
     * Used to wrap block locations without storing the Block objects themselves.
     *
     * @author Michael Ziluck
     */
    public static class BlockWrapper
    {
        private int   x;
        private int   y;
        private int   z;
        private World world;

        public BlockWrapper(int x, int y, int z, World world)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
        }

        public BlockWrapper(String line)
        {
            String[] pieces = line.split(",");
            this.x = Integer.parseInt(pieces[0]);
            this.y = Integer.parseInt(pieces[1]);
            this.z = Integer.parseInt(pieces[2]);
            this.world = Bukkit.getWorld(pieces[3]);
        }

        /**
         * @return the x
         */
        public int getX()
        {
            return x;
        }

        /**
         * @param x the x to set
         */
        public void setX(int x)
        {
            this.x = x;
        }

        /**
         * @return the y
         */
        public int getY()
        {
            return y;
        }

        /**
         * @param y the y to set
         */
        public void setY(int y)
        {
            this.y = y;
        }

        /**
         * @return the z
         */
        public int getZ()
        {
            return z;
        }

        /**
         * @param z the z to set
         */
        public void setZ(int z)
        {
            this.z = z;
        }

        /**
         * @return the world
         */
        public World getWorld()
        {
            return world;
        }

        /**
         * @param world the world to set
         */
        public void setWorld(World world)
        {
            this.world = world;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(x, y, z, world.getUID());
        }
    }

}
