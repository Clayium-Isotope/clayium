package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine1ToSome;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.crafting.WeightedList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileEntityChemicalMetalSeparator extends TileEntityClayiumMachine implements Machine1ToSome {
    private static final int baseConsumingEnergy = 5000;
    private static final int baseCraftTime = 40;
    private static final WeightedList<ItemStack> results = new WeightedList<>(ItemStack.EMPTY);
    private static final int RESULT_SLOT = 17;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(19, ItemStack.EMPTY);
        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsExport.add(IntStream.range(1, 17).toArray());
        this.maxAutoExtract = new int[]{-1, 1};
        this.maxAutoInsert = new int[]{-1};
        this.slotsDrop = Stream.concat(IntStream.range(0, 17).boxed(), Stream.of(this.getEnergySlot())).mapToInt(e -> e).toArray();
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public int getEnergySlot() {
        return 18;
    }

    @Override
    public boolean isDoingWork() {
        return this.timeToCraft > 0;
    }

    public String getNEIOutputId() {
        return "ChemicalMetalSeparator";
    }

    // TODO quit hard coding
    public boolean canCraft(ItemStack material) {
        return !material.isEmpty() && UtilItemStack.areItemDamageTagEqual(material, ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust));
    }

    @Override
    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        this.craftTime = 0L;
        this.timeToCraft = 0L;
        this.debtEnergy = 0L;
        UtilTransfer.produceItemStack(this.getStackInSlot(RESULT_SLOT).copy(), this.containerItemStacks, 1, 17, this.getInventoryStackLimit());

//                this.syncFlag = true;
//                if (this.externalControlState > 0) {
//                    --this.externalControlState;
//                    if (this.externalControlState == 0) {
//                        this.externalControlState = -1;
//                    }
//                }
    }

    @Override
    public boolean setNewRecipe() {
        this.debtEnergy = (long)((float)baseConsumingEnergy * this.multConsumingEnergy);
        if (!this.canCraft(this.getStackInSlot(0)) || !this.canProceedCraft()) return false;

        ItemStack result = results.get(TileEntityGeneric.random);

        if (result == null || result.isEmpty()
                || UtilTransfer.canProduceItemStack(result, this.containerItemStacks, 1, 17, this.getInventoryStackLimit()) < result.getCount()
        ) {
            return false;
        }

        this.timeToCraft = (long)((float)baseCraftTime * this.multCraftTime);

        this.getStackInSlot(0).shrink(1);
        this.setInventorySlotContents(RESULT_SLOT, result);

        return true;
    }

    @Override
    public int getResultSlotCount() {
        return 16;
    }

    static {
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.dust), 200);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureMagnesium, ClayiumShape.dust), 60);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureSodium, ClayiumShape.dust), 40);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureLithium, ClayiumShape.dust), 7);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.zirconium, ClayiumShape.dust), 5);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureZinc, ClayiumShape.dust), 10);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureManganese, ClayiumShape.dust), 80);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureCalcium, ClayiumShape.dust), 20);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impurePotassium, ClayiumShape.dust), 15);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureNickel, ClayiumShape.dust), 13);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureIron, ClayiumShape.dust), 9);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureBeryllium, ClayiumShape.dust), 8);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureLead, ClayiumShape.dust), 6);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureHafnium, ClayiumShape.dust), 4);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureChrome, ClayiumShape.dust), 3);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureTitanium, ClayiumShape.dust), 3);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureStrontium, ClayiumShape.dust), 2);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureBarium, ClayiumShape.dust), 2);
        TileEntityChemicalMetalSeparator.results.add(ClayiumMaterials.get(ClayiumMaterial.impureCopper, ClayiumShape.dust), 1);
    }
}
