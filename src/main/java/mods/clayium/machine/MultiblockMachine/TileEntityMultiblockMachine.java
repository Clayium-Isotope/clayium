package mods.clayium.machine.MultiblockMachine;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.machine.common.MachineSomeToSome;
import mods.clayium.util.EnumSide;
import mods.clayium.util.SyncManager;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilDirection;

public abstract class TileEntityMultiblockMachine extends TileEntityClayiumMachine implements MachineSomeToSome {

    private int constructionCounter = 0;
    protected int constructionTime = 20;

    public TileEntityMultiblockMachine() {
        this.isLoaded = false;
    }

    public abstract boolean isConstructed();

    protected abstract void onConstruction();

    protected void onDestruction() {
        // this.setRenderSyncFlag();
        this.craftTime = 0L;
        BlockStateMultiblockMachine.setConstructed(this, false);

        // de-sync the interface around the blast furnace.
        for (BlockPos relative : BlockPos.getAllInBox(-1, 0, 0, 1, 1, 2)) {
            if (relative.equals(BlockPos.ORIGIN)) continue;

            TileEntity te = this.getTileEntity(relative);
            if (te instanceof ISynchronizedInterface) {
                SyncManager.immediateSync(null, (ISynchronizedInterface) te);
            }
        }

        this.markDirty();
    }

    public boolean canProceedCraftWhenConstructed() {
        return super.canProceedCraft();
    }

    @Override
    public void update() {
        if (!this.isLoaded && this.isConstructed()) {
            this.onConstruction();
            this.isLoaded = true;
        }

        ++this.constructionCounter;
        if (this.constructionCounter >= this.constructionTime) {
            this.verifyConstruction();
            this.constructionCounter = 0;
        }

        super.update();
    }

    public BlockPos getRelativeCoord(BlockPos relative) {
        // EnumFacing direction = EnumFacing.getFront(this.getBlockMetadata());
        EnumFacing direction = this.getFront();
        EnumFacing xxVector = UtilDirection.getSideOfDirection(direction, EnumSide.RIGHT);
        assert xxVector != null;
        EnumFacing yyVector = EnumFacing.UP;
        EnumFacing zzVector = direction.getOpposite();
        return this.pos.offset(xxVector, relative.getX()).offset(yyVector, relative.getY()).offset(zzVector,
                relative.getZ());
    }

    protected TileEntity getTileEntity(BlockPos relative) {
        return this.world == null ? null : this.world.getTileEntity(this.getRelativeCoord(relative));
    }

    protected Block getBlock(BlockPos relative) {
        return this.world == null ? null : this.world.getBlockState(this.getRelativeCoord(relative)).getBlock();
    }

    protected IBlockState getBlockState(BlockPos relative) {
        return this.world == null ? null : this.world.getBlockState(this.getRelativeCoord(relative));
    }

    protected int getBlockMetadata(BlockPos relative) {
        return this.world == null ? 0 : this.getBlock(relative).getMetaFromState(this.getBlockState(relative));
    }

    protected TierPrefix getBlockTier(BlockPos relative) {
        Block block = this.getBlock(relative);
        return block instanceof ITieredBlock ?
                ((ITieredBlock) block).getTier(this.world, this.getRelativeCoord(relative)) : TierPrefix.none;
    }

    public void forceVerification() {
        this.verifyConstruction();
        this.constructionCounter = 0;
    }

    @Override
    public MultiblockMachine getBlockType() {
        assert this.blockType instanceof MultiblockMachine;
        return (MultiblockMachine) super.getBlockType();
    }

    public boolean canProceedCraft() {
        return BlockStateMultiblockMachine.isConstructed(this) && this.canProceedCraftWhenConstructed();
    }

    protected void verifyConstruction() {
        boolean newConstructed = this.isConstructed();
        if (newConstructed && !BlockStateMultiblockMachine.isConstructed(this)) {
            this.onConstruction();
        }

        if (!newConstructed && BlockStateMultiblockMachine.isConstructed(this)) {
            this.onDestruction();
        }

        BlockStateMultiblockMachine.setConstructed(this, newConstructed);
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return null;
    }
}
