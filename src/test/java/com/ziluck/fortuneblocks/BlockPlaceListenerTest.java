package com.ziluck.fortuneblocks;

import java.io.File;
import java.util.UUID;

import com.doctorzee.fortuneblocks.configuration.Config;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.listeners.BlockPlaceListener;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { BlockPlaceEvent.class, BlockHandler.class, FortuneBlocks.class, Config.class })
public class BlockPlaceListenerTest
{
    private static FortuneBlocks fortuneBlocks;

    private static File outputFolder;

    private static Player player;

    private static Block block;

    private static BlockPlaceEvent event;

    private static BlockPlaceListener blockListener;

    @Before
    public void setup()
    {
        outputFolder = new File("output/");
        if (!outputFolder.exists() && !outputFolder.mkdirs())
        {
            Assert.fail("Could not create folder.");
        }

        // create FortuneBlocks object
        fortuneBlocks = PowerMockito.mock(FortuneBlocks.class);

        // set up data folder
        PowerMockito.when(fortuneBlocks.getDataFolder()).thenReturn(outputFolder);

        // set up fortune blocks getInstance
        PowerMockito.mockStatic(FortuneBlocks.class);
        when(FortuneBlocks.getInstance()).thenReturn(fortuneBlocks);

        // set up player
        player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("Doctor_Zee");
        when(player.getUniqueId()).thenReturn(UUID.fromString("a84a885c-7551-49c4-90c0-31d25f41e7f0"));

        // watch Config
        PowerMockito.mock(Config.class);

        // set up the Block
        block = PowerMockito.mock(Block.class);
        when(block.getType()).thenReturn(Material.DIRT);

        // set up the event
        event = PowerMockito.mock(BlockPlaceEvent.class);
        when(event.getPlayer()).thenReturn(player);

        // tell the event about the block
        when(event.getBlock()).thenReturn(block);

        // set up the BlockHandler
        PowerMockito.mockStatic(BlockHandler.class);

        blockListener = new BlockPlaceListener();
    }

    @Test
    public void onPlaceTestUntracked()
    {
        MutableBoolean mocked = mock(MutableBoolean.class);
        when(mocked.booleanValue()).thenReturn(true);
        Whitebox.setInternalState(Config.class, "TRACKING_ENABLED", mocked);

        // set up the BlockHandler
        when(BlockHandler.isTracked(Mockito.any())).thenReturn(false);

        // fire the event
        blockListener.onPlace(event);

        verify(mocked, never()).booleanValue();
    }

    @Test
    public void onPlaceTest()
    {
        // enable tracking
        MutableBoolean mocked = mock(MutableBoolean.class);
        when(mocked.booleanValue()).thenReturn(true);
        Whitebox.setInternalState(Config.class, "TRACKING_ENABLED", mocked);

        // give the player permission
        when(player.hasPermission(Permission.SILENT_PLACE.getPermission())).thenReturn(false);

        // set up the BlockHandler
        when(BlockHandler.isTracked(Mockito.any())).thenReturn(true);

        // fire the event
        blockListener.onPlace(event);

        // check the conditions
        assertTrue(Config.TRACKING_ENABLED.booleanValue());
        assertTrue(!event.getPlayer().hasPermission(Permission.SILENT_PLACE.getPermission()));

        // verify that setPlaced was called once
        PowerMockito.verifyStatic(BlockHandler.class, times(1));
        BlockHandler.setPlaced(event.getBlock());
    }
}
