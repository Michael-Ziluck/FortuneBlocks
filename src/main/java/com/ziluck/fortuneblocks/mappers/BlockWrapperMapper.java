package com.ziluck.fortuneblocks.mappers;

import org.apache.ibatis.annotations.Param;
import org.bukkit.World;

import com.ziluck.fortuneblocks.handlers.BlockWrapper;

import java.util.List;

public interface BlockWrapperMapper {
    int insert(BlockWrapper record);

    List<BlockWrapper> selectAll();

    int deleteByAll(BlockWrapper wrapper);

    int countByAll(BlockWrapper wrapper);
}