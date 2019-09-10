package com.ziluck.fortuneblocks.handlers;

import com.ziluck.fortuneblocks.FortuneBlocks;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockHandler {
    private static final BlockHandler instance;

    static {
        instance = new BlockHandler();
    }

    private Set<BlockWrapper> blockWrappers;

    private Set<Material> materials;

    public BlockHandler() {
        // initialize the set
        blockWrappers = Collections.newSetFromMap(new ConcurrentHashMap<>());

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
    }

    /**
     * Mark a block as placed.
     *
     * @param block the block to mark
     */
    public void setPlaced(Block block) {
        instance.blockWrappers.add(wrap(block));
    }

    /**
     * Mark a block as placed.
     *
     * @param blockWrapper the block to mark
     */
    public void setPlaced(BlockWrapper blockWrapper) {
        instance.blockWrappers.add(blockWrapper);
    }

    /**
     * Mark a block as no longer placed by a player.
     *
     * @param block the block to mark.
     */
    public void clearPlaced(Block block) {
        instance.blockWrappers.remove(wrap(block));
    }

    /**
     * @param block the block to check.
     * @return {@code true} if this block was placed by a player.
     */
    public boolean wasPlaced(Block block) {
        return instance.blockWrappers.contains(wrap(block));
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

    private List<String> getTrackedMaterialNames() {
        List<String> values = new LinkedList<>();
        for (Material material : materials) {
            values.add(material.name());
        }
        return values;
    }

    private static BlockWrapper wrap(Block block) {
        return new BlockWrapper(block.getX(), block.getY(), block.getZ(), block.getWorld());
    }
}
