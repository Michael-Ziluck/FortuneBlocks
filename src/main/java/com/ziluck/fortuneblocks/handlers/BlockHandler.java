package com.ziluck.fortuneblocks.handlers;

import com.ziluck.fortuneblocks.FortuneBlocks;
import com.ziluck.fortuneblocks.configuration.Config;
import com.ziluck.fortuneblocks.handlers.trackers.BlockTracker;
import com.ziluck.fortuneblocks.handlers.trackers.MySQLTracker;
import com.ziluck.fortuneblocks.handlers.trackers.TrackerType;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class BlockHandler {
    private Set<Material> materials;

    private BlockTracker tracker;

    public BlockHandler() {
        // create enum set
        materials = EnumSet.noneOf(Material.class);

        // load the materials we care about
        for (String string : FortuneBlocks.getInstance().getConfig().getStringList("blocks")) {
            try {
                materials.add(FortuneBlocks.getItemHandler().get(string).getType());
            } catch (Exception ex) {
                FortuneBlocks.getInstance().getLogger().severe("Invalid Block: '" + string + "'.");
            }
        }

        // check if we're tracking blocks
        if (Config.TRACKING_ENABLED.booleanValue()) {
            TrackerType trackerType = Config.TRACKER_TYPE.getValue();
            switch (trackerType) {
                case MYSQL:
                    this.tracker = new MySQLTracker();
                    break;
                default:
                    return;
            }
            tracker.initialize();
        }
    }

    /**
     * @return {@code true} if we track the material.
     */
    public boolean isTracked(Material material) {
        return materials.contains(material);
    }

    /**
     * @param material the material to update.
     * @param status   {@code true} to add, {@code false} to remove.
     */
    public void setTracked(Material material, boolean status) {
        if (status) {
            materials.add(material);
        } else {
            materials.remove(material);
        }
        FortuneBlocks.getInstance().getConfig().set("blocks", getTrackedMaterialNames());
        FortuneBlocks.getInstance().saveConfig();
    }

    /**
     * @return an immutable view of the tracked materials.
     */
    public Set<Material> getTrackedMaterials() {
        return Collections.unmodifiableSet(materials);
    }

    public BlockTracker getTracker() {
        return tracker;
    }

    private List<String> getTrackedMaterialNames() {
        List<String> values = new LinkedList<>();
        for (Material material : materials) {
            values.add(material.name());
        }
        return values;
    }

    public static BlockWrapper wrap(Block block) {
        return new BlockWrapper(block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
    }
}
