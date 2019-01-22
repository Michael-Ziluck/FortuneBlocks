package com.doctorzee.fortuneblocks.utils.items;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionData;

public class ItemData
{
    private final Material material;

    private PotionData potionData = null;

    private EntityType entity = null;

    public ItemData(Material material)
    {
        this.material = material;
    }

    public ItemData(Material material, PotionData potionData)
    {
        this.material = material;
        this.potionData = potionData;
    }

    public ItemData(Material material, EntityType entity)
    {
        this.material = material;
        this.entity = entity;
    }

    public Material getMaterial()
    {
        return material;
    }

    public PotionData getPotionData()
    {
        return this.potionData;
    }

    public EntityType getEntity()
    {
        return this.entity;
    }

    @Override
    public int hashCode()
    {
        return (31 * material.hashCode()) ^ potionData.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (!(o instanceof ItemData))
        {
            return false;
        }
        ItemData that = (ItemData) o;
        return this.material == that.getMaterial() && potionDataEquals(that) && entityEquals(that);
    }

    private boolean potionDataEquals(ItemData o)
    {
        if (this.potionData == null && o.getPotionData() == null)
        {
            return true;
        }
        else if (this.potionData != null && o.getPotionData() != null)
        {
            return this.potionData.equals(o.getPotionData());
        }
        else
        {
            return false;
        }
    }

    private boolean entityEquals(ItemData o)
    {
        if (this.entity == null && o.getEntity() == null)
        { // neither have an entity
            return true;
        }
        else if (this.entity != null && o.getEntity() != null)
        { // both have an entity; check if it's the same one
            return this.entity.equals(o.getEntity());
        }
        else
        { // one has an entity but the other doesn't, so they can't be equal
            return false;
        }
    }
}