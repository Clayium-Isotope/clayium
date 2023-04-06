package mods.clayium.machine.QuartzCrucible;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityQuartzCrucible extends TileEntityGeneric implements ITickable {
    public int craftTime;
    public static int timeToCraft = 600;

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.craftTime = tagCompound.getShort("CraftTime");
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setShort("CraftTime", (short) this.craftTime);
        return tagCompound;
    }

    @Override
    public void update() {
        if (this.world.getBlockState(this.pos).getValue(BlockStateQuartzCrucible.LEVEL) > 0) {
            this.craftTime++;
        }
    }

    public boolean putIngot() {
        if (!this.world.isRemote && this.world.getBlockState(this.pos).getValue(BlockStateQuartzCrucible.LEVEL) < 9) {
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).cycleProperty(BlockStateQuartzCrucible.LEVEL), 3);
            return true;
        } else {
            return false;
        }
    }

    public boolean consumeString() {
        int ingotQuantity = this.world.getBlockState(this.pos).getValue(BlockStateQuartzCrucible.LEVEL);
        if (this.world != null && !this.world.isRemote && this.craftTime >= ingotQuantity * timeToCraft) {
            EntityItem entityitem = new EntityItem(this.world, (float)this.getPos().getX() + 0.4F, (float)this.getPos().getY() + 0.4F, (float)this.getPos().getZ() + 0.4F, ClayiumMaterials.get(ClayiumMaterial.silicon, ClayiumShape.ingot, ingotQuantity));
            this.world.spawnEntity(entityitem);
            this.craftTime = 0;
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(BlockStateQuartzCrucible.LEVEL, 0), 3);
            return true;
        } else {
            return false;
        }
    }
}
