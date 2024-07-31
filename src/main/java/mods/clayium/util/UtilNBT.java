package mods.clayium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UtilNBT {
    public static NBTTagCompound getTileEntityTag(ItemStack item) {
        if (!item.isEmpty() && item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("TileEntityNBTTag")) {
                return (NBTTagCompound) tag.getTag("TileEntityNBTTag");
            }
        }

        return new NBTTagCompound();
    }

    public static NBTTagCompound serializeAABB(@Nonnull AxisAlignedBB aabb) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("minX", aabb.minX);
        tag.setDouble("minY", aabb.minY);
        tag.setDouble("minZ", aabb.minZ);
        tag.setDouble("maxX", aabb.maxX);
        tag.setDouble("maxY", aabb.maxY);
        tag.setDouble("maxZ", aabb.maxZ);
        return tag;
    }

    public static AxisAlignedBB deserializeAABB(NBTTagCompound tag) {
        return new AxisAlignedBB(
                tag.getDouble("minX"),
                tag.getDouble("minY"),
                tag.getDouble("minZ"),
                tag.getDouble("maxX"),
                tag.getDouble("maxY"),
                tag.getDouble("maxZ")
        );
    }

    public static void addWorldToTag(NBTTagCompound tag, @Nullable World world) {
        if (world != null) {
            tag.setInteger("world", world.provider.getDimension());
        }
    }

    @Nullable
    public static World getWorldFromTag(NBTTagCompound tag) {
        if (tag.hasKey("world", Constants.NBT.TAG_INT)) {
            return DimensionManager.getWorld(tag.getInteger("world"));
        }
        return null;
    }
}
