package mods.clayium.block.tile;

import mods.clayium.item.CMaterials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;

public class TileQuartzCrucible extends TileGeneric {
    public int craftTime;
    public static int timeToCraft = 600;
    public int ingotQuantity;

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.craftTime = tagCompound.getShort("CraftTime");
        this.ingotQuantity = tagCompound.getShort("IngotQuantity");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setShort("CraftTime", (short) this.craftTime);
        tagCompound.setShort("IngotQuantity", (short) this.ingotQuantity);
    }

    public void updateEntity() {
        super.updateEntity();
        if (this.ingotQuantity > 0) {
            this.craftTime++;
        }
    }


    public boolean putIngot() {
        if (!this.worldObj.isRemote &&
                this.ingotQuantity < 9) {
            this.ingotQuantity++;
            setMetadata(this.ingotQuantity);
            return true;
        }

        return false;
    }

    public boolean consumeString() {
        if (this.worldObj != null && !this.worldObj.isRemote &&
                this.ingotQuantity > 0 && this.craftTime >= this.ingotQuantity * timeToCraft) {

            EntityItem entityitem = new EntityItem(this.worldObj, (this.xCoord + 0.4F), (this.yCoord + 0.4F), (this.zCoord + 0.4F), CMaterials.get(CMaterials.SILICON, CMaterials.INGOT, this.ingotQuantity));
            this.worldObj.spawnEntityInWorld((Entity) entityitem);
            this.ingotQuantity = 0;
            this.craftTime = 0;
            setMetadata(0);
            return true;
        }

        return false;
    }

    public void setMetadata(int meta) {
        if (this.worldObj != null)
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta, 3);
    }
}
