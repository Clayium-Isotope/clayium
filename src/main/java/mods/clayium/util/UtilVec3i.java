package mods.clayium.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class UtilVec3i {
    /**
     * Creates a <b>Vec3i</b> object from the data stored in the passed NBTTagCompound.
     * @see net.minecraft.nbt.NBTUtil#getPosFromTag(NBTTagCompound)
     */
    public static Vec3i getVec3iFromTag(NBTTagCompound tag)
    {
        return new Vec3i(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    /**
     * Creates a new NBTTagCompound from a <b>Vec3i</b>.
     * @see net.minecraft.nbt.NBTUtil#createPosTag(BlockPos)
     */
    public static NBTTagCompound createVec3iTag(Vec3i pos)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setInteger("x", pos.getX());
        nbttagcompound.setInteger("y", pos.getY());
        nbttagcompound.setInteger("z", pos.getZ());
        return nbttagcompound;
    }

    public static Vec3i add(Vec3i a, Vec3i b) {
        return new Vec3i(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }
}
