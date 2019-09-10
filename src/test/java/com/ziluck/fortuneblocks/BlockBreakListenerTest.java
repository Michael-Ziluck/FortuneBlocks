package com.ziluck.fortuneblocks;

import java.util.UUID;

import com.doctorzee.fortuneblocks.configuration.Config;
import com.doctorzee.fortuneblocks.handlers.BlockHandler;
import com.doctorzee.fortuneblocks.listeners.BlockBreakListener;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { Config.class, BlockHandler.class, BlockBreakEvent.class, BlockEvent.class })
public class BlockBreakListenerTest
{
    @Mock
    private Player player;

    @Mock
    private Block block;

    @Mock
    private MutableBoolean TRACKING_ENABLED, REQUIRE_SURVIVAL;

    @Mock
    private BlockBreakEvent event;

    @Mock
    private BlockBreakListener listener;

    @Before
    public void setup()
    {
        // set up player
        when(player.getName()).thenReturn("Doctor_Zee");
        when(player.getUniqueId()).thenReturn(UUID.fromString("a84a885c-7551-49c4-90c0-31d25f41e7f0"));

        // mock Config
        PowerMockito.mock(Config.class);
        Whitebox.setInternalState(Config.class, "TRACKING_ENABLED", TRACKING_ENABLED);

        // mock BlockHandler
        PowerMockito.mockStatic(BlockHandler.class);

        // set up the Block
        when(block.getType()).thenReturn(Material.DIRT);

        // set up the Event
        when(event.getPlayer()).thenReturn(player);
        when(event.getBlock()).thenReturn(block);
    }

    @Test
    public void onBreakTestSurvival()
    {
        when(player.getGameMode()).thenReturn(GameMode.CREATIVE);

        listener.onBreak(event);

        verify(TRACKING_ENABLED, never()).booleanValue();
    }

    @Test
    public void onBreakTestTracking()
    {
        listener.onBreak(event);

        when(TRACKING_ENABLED.booleanValue()).thenReturn(true);
        when(BlockHandler.wasPlaced(Mockito.any())).thenReturn(true);

        PowerMockito.verifyStatic(BlockHandler.class, never());
        BlockHandler.isTracked(event.getBlock().getType());
    }

}
