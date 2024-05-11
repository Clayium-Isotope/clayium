package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine1ToSome;
import mods.clayium.util.ContainClayEnergy;
import mods.clayium.util.IllegalTierException;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.crafting.Kitchen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class TileEntityClayCentrifuge extends TileEntityClayContainer implements IClayEnergyConsumer, Machine1ToSome {

    /* package-private */ int resultSlotNum = 4;
    Kitchen kitchen;
    final ContainClayEnergy ce = new ContainClayEnergy();

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(6, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.add(new int[] { Machine1ToSome.MATERIAL });
        this.listSlotsExport.add(new int[] { Machine1ToSome.PRODUCT_1, Machine1ToSome.PRODUCT_2,
                Machine1ToSome.PRODUCT_3, Machine1ToSome.PRODUCT_4 });
        this.maxAutoExtract = new int[] { -1, 1 };
        this.maxAutoInsert = new int[] { -1 };
        this.slotsDrop = new int[] { Machine1ToSome.MATERIAL, Machine1ToSome.PRODUCT_1, Machine1ToSome.PRODUCT_2,
                Machine1ToSome.PRODUCT_3, Machine1ToSome.PRODUCT_4, this.getEnergySlot() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);

        switch (tier) {
            case simple:
                this.resultSlotNum = 1;
                break;
            case basic:
                this.resultSlotNum = 2;
                break;
            case advanced:
                this.resultSlotNum = 3;
                break;
            case precision:
                this.resultSlotNum = 4;
                break;
            default:
                throw new IllegalTierException();
        }

        this.kitchen = new KitchenCentrifuge(this, this.tier);
    }

    @Override
    public void update() {
        super.update();
        if (!this.getWorld().isRemote)
            this.kitchen.work();
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.ce;
    }

    @Override
    public int getClayEnergyStorageSize() {
        return 1;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {}

    @Override
    public int getEnergySlot() {
        return 5;
    }

    @Override
    public EnumMachineKind getKind() {
        return EnumMachineKind.centrifuge;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return (int) this.kitchen.getTimeToCraft();
            case 1:
                return (int) this.kitchen.getCraftTime();
            case 2:
                return (int) this.containEnergy().get();
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.kitchen.setTimeToCraft(value);
                return;
            case 1:
                this.kitchen.setCraftTime(value);
                return;
            case 2:
                this.containEnergy().set(value);
                return;
        }
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.kitchen.deserializeNBT(tagCompound);
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        tagCompound.setTag("kitchen", this.kitchen.serializeNBT());

        return super.writeMoreToNBT(tagCompound);
    }

    @Override
    public final int getResultSlotCount() {
        return this.resultSlotNum;
    }
}
