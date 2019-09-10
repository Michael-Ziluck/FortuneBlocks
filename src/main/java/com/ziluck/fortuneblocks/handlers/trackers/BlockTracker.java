package com.ziluck.fortuneblocks.handlers.trackers;

import com.ziluck.fortuneblocks.handlers.BlockWrapper;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

import static com.ziluck.fortuneblocks.handlers.BlockHandler.wrap;

public abstract class BlockTracker {
    protected Set<BlockWrapper> placedBlocks = new HashSet<>();

    public abstract boolean initialize();

    public abstract void writeBlock(BlockWrapper blockWrapper);

    public abstract void clearBlock(BlockWrapper blockWrapper);

    public boolean wasPlaced(BlockWrapper blockWrapper) {
        return placedBlocks.contains(blockWrapper);
    }

    /**
     * Mark a block as placed.
     *
     * @param block the block to mark
     */
    public void setPlaced(Block block) {
        setPlaced(wrap(block));
    }

    /**
     * Mark a block as placed.
     *
     * @param blockWrapper the block to mark
     */
    public void setPlaced(BlockWrapper blockWrapper) {
        writeBlock(blockWrapper);
    }
}
