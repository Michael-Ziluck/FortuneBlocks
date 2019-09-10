package com.ziluck.fortuneblocks.handlers;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Objects;

/**
 * Used to wrap block locations without storing the Block objects themselves.
 *
 * @author Michael Ziluck
 */
public class BlockWrapper {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private int id;
    private int x;
    private int y;
    private int z;
    private String world;

    public BlockWrapper(int id, int x, int y, int z, String world) {
        this(x, y, z, world);
        this.id = id;
    }

    public BlockWrapper(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public BlockWrapper(String line) {
        String[] pieces = line.split(",");
        this.x = Integer.parseInt(pieces[0]);
        this.y = Integer.parseInt(pieces[1]);
        this.z = Integer.parseInt(pieces[2]);
        this.world = pieces[3];
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * @return the world
     */
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, Objects.requireNonNull(Bukkit.getWorld(world)).getUID());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(x).append(",");
        sb.append(y).append(",");
        sb.append(z).append(",");
        sb.append(world);
        return sb.toString();
    }
}
