package com.ziluck.fortuneblocks.handlers;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Used to wrap block locations without storing the Block objects themselves.
 *
 * @author Michael Ziluck
 */
public class BlockWrapper {
    private int x;
    private int y;
    private int z;
    private World world;

    public BlockWrapper(int x, int y, int z, World world) {
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
        this.world = Bukkit.getWorld(pieces[3]);
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * @param world the world to set
     */
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, world.getUID());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append(y);
        sb.append(z);
        sb.append(world.getName());
        return sb.toString();
    }
}
