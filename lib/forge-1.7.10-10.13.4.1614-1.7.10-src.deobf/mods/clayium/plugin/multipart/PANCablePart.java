package mods.clayium.plugin.multipart;

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.minecraft.McMetaPart;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.CBlocks;
import mods.clayium.block.PANCable;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;


public class PANCablePart
        extends McMetaPart {
    protected static Cuboid6 bound = null;

    public Cuboid6 getBounds() {
        if (bound == null)
            bound = new Cuboid6(0.5D - PANCable.pipeWidth, 0.5D - PANCable.pipeWidth, 0.5D - PANCable.pipeWidth, 0.5D + PANCable.pipeWidth, 0.5D + PANCable.pipeWidth, 0.5D + PANCable.pipeWidth);
        return bound;
    }


    public Block getBlock() {
        return CBlocks.blockPANCable;
    }

    protected static String type = "clayium|PanCable";

    public String getType() {
        return type;
    }

    public Iterable<Cuboid6> getCollisionBoxes() {
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        getBlock().addCollisionBoxesToList(world(), x(), y(), z(), TileEntity.INFINITE_EXTENT_AABB, aabbs, null);

        List<Cuboid6> ret = new ArrayList<Cuboid6>();
        for (AxisAlignedBB aabb : aabbs)
            ret.add(new Cuboid6(aabb.offset(-x(), -y(), -z())));
        return ret;
    }

    public Iterable<IndexedCuboid6> getSubParts() {
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        getBlock().addCollisionBoxesToList(world(), x(), y(), z(), TileEntity.INFINITE_EXTENT_AABB, aabbs, null);

        List<IndexedCuboid6> ret = new ArrayList<IndexedCuboid6>();
        for (AxisAlignedBB aabb : aabbs)
            ret.add(new IndexedCuboid6(0, new Cuboid6(aabb.offset(-x(), -y(), -z()))));
        return ret;
    }
}
