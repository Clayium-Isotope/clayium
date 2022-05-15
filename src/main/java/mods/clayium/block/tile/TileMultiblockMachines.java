package mods.clayium.block.tile;

import mods.clayium.block.ITieredBlock;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public abstract class TileMultiblockMachines extends TileClayMachines {
    public boolean constructed = false;
    private int constructionCounter = 0;
    protected int constructionTime = 20;


    public abstract boolean isConstructed();


    protected abstract void onConstruction();


    protected abstract void onDestruction();


    public boolean canProceedCraftWhenConstructed() {
        return super.canProceedCraft();
    }


    public void updateEntity() {
        this.constructionCounter++;
        if (this.constructionCounter >= this.constructionTime) {
            verifyConstruction();
            this.constructionCounter = 0;
        }
        super.updateEntity();
    }


    public int[] getRelativeCoord(int xx, int yy, int zz) {
        UtilDirection direction = UtilDirection.getOrientation(getBlockMetadata());
        UtilDirection xxVector = direction.getSide(UtilDirection.RIGHTSIDE);
        UtilDirection yyVector = UtilDirection.UP;
        UtilDirection zzVector = direction.getOpposite();
        int[] coord = {this.xCoord + xxVector.offsetX * xx + yyVector.offsetX * yy + zzVector.offsetX * zz, this.yCoord + xxVector.offsetY * xx + yyVector.offsetY * yy + zzVector.offsetY * zz, this.zCoord + xxVector.offsetZ * xx + yyVector.offsetZ * yy + zzVector.offsetZ * zz};


        return coord;
    }


    protected TileEntity getTileEntity(int xx, int yy, int zz) {
        int[] coord = getRelativeCoord(xx, yy, zz);
        return (this.worldObj == null) ? null : UtilBuilder.safeGetTileEntity((IBlockAccess) this.worldObj, coord[0], coord[1], coord[2]);
    }

    protected Block getBlock(int xx, int yy, int zz) {
        int[] coord = getRelativeCoord(xx, yy, zz);
        return (this.worldObj == null) ? null : this.worldObj.getBlock(coord[0], coord[1], coord[2]);
    }

    protected int getBlockMetadata(int xx, int yy, int zz) {
        int[] coord = getRelativeCoord(xx, yy, zz);
        return (this.worldObj == null) ? 0 : this.worldObj.getBlockMetadata(coord[0], coord[1], coord[2]);
    }

    protected int getBlockTier(int xx, int yy, int zz) {
        int[] coord = getRelativeCoord(xx, yy, zz);
        Block block = getBlock(xx, yy, zz);
        return (block != null && block instanceof ITieredBlock) ? ((ITieredBlock) block).getTier((IBlockAccess) this.worldObj, coord[0], coord[1], coord[2]) : -1;
    }


    public void forceVerification() {
        verifyConstruction();
        this.constructionCounter = 0;
    }


    public boolean canProceedCraft() {
        return (this.constructed && canProceedCraftWhenConstructed());
    }


    protected void verifyConstruction() {
        boolean newConstructed = isConstructed();
        if (newConstructed && !this.constructed) {
            onConstruction();
        }
        if (!newConstructed && this.constructed) {
            onDestruction();
        }
        this.constructed = newConstructed;
    }
}
