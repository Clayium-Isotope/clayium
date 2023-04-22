package mods.clayium.machine.MultiblockMachine;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityMultiblockMachine extends TileEntityClayiumMachine {
    private int constructionCounter = 0;
    protected int constructionTime = 20;

    public TileEntityMultiblockMachine() {
    }

    public abstract boolean isConstructed();

    protected abstract void onConstruction();

    protected abstract void onDestruction();

    public boolean canProceedCraftWhenConstructed() {
        return super.canProceedCraft();
    }

    @Override
    public void update() {
        ++this.constructionCounter;
        if (this.constructionCounter >= this.constructionTime) {
            this.verifyConstruction();
            this.constructionCounter = 0;
        }

        super.update();
    }

    public BlockPos getRelativeCoord(BlockPos relative) {
//        EnumFacing direction = EnumFacing.getFront(this.getBlockMetadata());
        EnumFacing direction = this.getFront();
        EnumFacing xxVector = UtilDirection.getSideOfDirection(direction, EnumSide.RIGHT);
        assert xxVector != null;
        EnumFacing yyVector = EnumFacing.UP;
        EnumFacing zzVector = direction.getOpposite();
        return this.pos.offset(xxVector, relative.getX()).offset(yyVector, relative.getY()).offset(zzVector, relative.getZ());
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

    protected int getBlockTier(BlockPos relative) {
        Block block = this.getBlock(relative);
        return block instanceof ITieredBlock ? ((ITieredBlock) block).getTier(this.world, this.getRelativeCoord(relative)) : -1;
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
}
