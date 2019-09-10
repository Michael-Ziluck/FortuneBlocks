package com.ziluck.fortuneblocks.handlers.trackers;

import com.ziluck.fortuneblocks.handlers.BlockWrapper;

public abstract class BlockTracker {
    public abstract boolean initialize();

    public abstract boolean writeBlock(BlockWrapper blockWrapper);
}
