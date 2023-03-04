package mods.clayium.machine.SaltExtractor;

import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.ClayContainer.IClayInventory;
import mods.clayium.machine.CobblestoneGenerator.TileEntityCobblestoneGenerator;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileEntitySaltExtractor extends TileEntityCobblestoneGenerator implements IClayInventory, IClayEnergyConsumer {
    private long clayEnergy;
    private static final int energyPerWork = 30;

    public TileEntitySaltExtractor() {
        this.autoInsert = true;
        this.autoExtract = true;

        this.containerItemStacks = NonNullList.withSize(54 + 1, ItemStack.EMPTY);
        this.isCobblestoneGenerator = false;
        this.isBuffer = false;
    }

    @Override
    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        this.listSlotsImport.clear();
        this.listSlotsImport.add(new int[]{ this.getEnergySlot() });
        this.slotsDrop = Stream.concat(
                IntStream.range(0, this.slotsDrop.length).boxed(),
                Stream.of( this.getEnergySlot() ))
            .mapToInt(e -> e).toArray();
    }

    @Override
    public void produce() {
        int count = this.countWater();
        if (this.externalControlState >= 0 && count > 0 && IClayEnergyConsumer.super.compensateClayEnergy((long) this.progressEfficiency * energyPerWork)) {
            this.progress += this.progressEfficiency * count;

            while(this.progress >= progressMax) {
//                this.setSyncFlag();
                ItemStack salt = ClayiumMaterials.get(ClayiumMaterial.salt, ClayiumShape.dust).copy();
                this.progress -= progressMax;
                UtilTransfer.produceItemStack(salt, this.containerItemStacks, 0, this.inventoryX * this.inventoryY, this.getInventoryStackLimit());
                if (this.externalControlState > 0) {
                    --this.externalControlState;
                    if (this.externalControlState == 0) {
                        this.externalControlState = -1;
                    }
                }
            }
        }

    }

    @Override
    public boolean isScheduled() {
        return this.countWater() > 0;
    }

    public int countWater() {
        int count = 0;

        for (EnumFacing side : EnumFacing.VALUES) {
            if (this.world.getBlockState(this.pos.offset(side)).getMaterial() == Material.WATER) {
                count++;
            }
        }

        return count;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import_energy");
        this.registerExtractIcons("export");
    }

    @Override
    public long getContainEnergy() {
        return this.clayEnergy;
    }

    @Override
    public void setContainEnergy(long energy) {
        this.clayEnergy = energy;
    }

    @Override
    public int getEnergySlot() {
        return this.inventoryX * this.inventoryY;
    }

    @Override
    public int getEnergyStorageSize() {
        return this.clayEnergyStorageSize;
    }
}
