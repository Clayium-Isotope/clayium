package mods.clayium.machine.ClayMarker;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class TileEntityClayMarker extends TileEntityGeneric implements IAABBProvider {
    @Nonnull
    protected AxisAlignedBB aabb = NULL_AABB;
    public Appearance state = Appearance._0;
    public static final int maxRange = 64;
    protected int xx = 0;
    protected int yy = 0;
    protected int zz = 0;

    public void activate(World world, BlockPos pos, IBlockState state, MarkerExtent extentMode) {
        if (world.isRemote) {
            return;
        }
//            this.setInstantSyncFlag();
        this.xx = this.yy = this.zz = 0;

        int i;
        for(i = 1; i < maxRange; ++i) {
            if (isMarker(world, pos.add(i, 0, 0))) {
                this.xx = i;
                break;
            }

            if (isMarker(world, pos.add(-i, 0, 0))) {
                this.xx = -i;
                break;
            }
        }

        for(i = 1; i < maxRange; ++i) {
            if (isMarker(world, pos.add(0, i, 0))) {
                this.yy = i;
                break;
            }

            if (isMarker(world, pos.add(0, -i, 0))) {
                this.yy = -i;
                break;
            }
        }

        for(i = 1; i < maxRange; ++i) {
            if (isMarker(world, pos.add(0, 0, i))) {
                this.zz = i;
                break;
            }

            if (isMarker(world, pos.add(0, 0, -i))) {
                this.zz = -i;
                break;
            }
        }

        this.state = state.getValue(AABBHolder.APPEARANCE);
        if (this.xx == 0 && this.yy == 0 && this.zz == 0 && !Appearance._1.equals(this.state)) {
            this.aabb = NULL_AABB;
            this.state = Appearance._0.equals(this.state) ? Appearance._1 : Appearance._0;
        } else {
            this.aabb = extentMode.apply(
                    new AxisAlignedBB(pos, pos.add(this.xx, this.yy, this.zz)).offset(0.5d, 0.5d, 0.5d).grow(0.5d)
            );
            this.state = Appearance._1.compareTo(this.state) >= 0 ? Appearance._3 : Appearance.increment(this.state, Appearance._2);
        }
        world.setBlockState(pos, state.withProperty(AABBHolder.APPEARANCE, this.state));
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
//        this.state = Appearance.fromMeta(tagCompound.getByte("State"));
        readAxisAlignedBBFromNBT(tagCompound, this);
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
//        tagCompound.setByte("State", (byte) this.state.toMeta());
        writeAxisAlignedBBToNBT(tagCompound, this);
        return tagCompound;
    }

    protected static boolean isMarker(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof ClayMarker;
    }

    @Override
    public AxisAlignedBB getAxisAlignedBB() {
        return this.aabb;
    }

    @Override
    public void setAxisAlignedBB(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    @Override
    public boolean hasAxisAlignedBB() {
        return this.aabb != NULL_AABB;
    }

    @Override
    public Appearance getBoxAppearance() {
        return Appearance.back2(this.state);
    }

    @Override
    public void setAxisAlignedBBToMachine() {
        this.markDirty();
        this.breakMarker(this.xx, 0, 0);
        this.breakMarker(0, this.yy, 0);
        this.breakMarker(0, 0, this.zz);
        this.breakMarker(0, 0, 0);
    }

    protected void breakMarker(int xx, int yy, int zz) {
        Block block = this.getWorld().getBlockState(this.getPos().add(xx, yy, zz)).getBlock();
        if (block instanceof ClayMarker) {
            UtilBuilder.dropItems(this.getWorld(), this.getPos(), UtilBuilder.harvestBlock(this.getWorld(), this.getPos().add(xx, yy, zz), false, false, 0, true));
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0;
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Double.POSITIVE_INFINITY;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public static void writeAxisAlignedBBToNBT(NBTTagCompound tagCompound, AABBHolder tile) {
        if (tile.hasAxisAlignedBB()) {
            tagCompound.setBoolean("hasAABB", true);
            tagCompound.setDouble("AABBMinX", tile.getAxisAlignedBB().minX);
            tagCompound.setDouble("AABBMinY", tile.getAxisAlignedBB().minY);
            tagCompound.setDouble("AABBMinZ", tile.getAxisAlignedBB().minZ);
            tagCompound.setDouble("AABBMaxX", tile.getAxisAlignedBB().maxX);
            tagCompound.setDouble("AABBMaxY", tile.getAxisAlignedBB().maxY);
            tagCompound.setDouble("AABBMaxZ", tile.getAxisAlignedBB().maxZ);
        } else {
            tagCompound.setBoolean("hasAABB", false);
        }
    }

    public static void readAxisAlignedBBFromNBT(NBTTagCompound tagCompound, AABBHolder tile) {
        boolean hasAABB = tagCompound.getBoolean("hasAABB");
        if (hasAABB) {
            double minX = tagCompound.getDouble("AABBMinX");
            double minY = tagCompound.getDouble("AABBMinY");
            double minZ = tagCompound.getDouble("AABBMinZ");
            double maxX = tagCompound.getDouble("AABBMaxX");
            double maxY = tagCompound.getDouble("AABBMaxY");
            double maxZ = tagCompound.getDouble("AABBMaxZ");

            AxisAlignedBB aabb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
            if (!tile.hasAxisAlignedBB() || !tile.getAxisAlignedBB().equals(aabb)) {
                tile.setAxisAlignedBB(aabb);
            }
        }
    }
}
