package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.crafting.WeightedList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileEntityChemicalMetalSeparator extends TileEntityClayiumMachine {
    private static final int baseConsumingEnergy = 5000;
    private static final int baseCraftTime = 40;
    private static final WeightedList<ItemStack> results = new WeightedList<>();
    private static final Random random = TileGeneric.random;
    private static final int RESULT_SLOT = 17;

    public TileEntityChemicalMetalSeparator() {}

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(19, ItemStack.EMPTY);
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsImport.add(new int[]{ this.getEnergySlot() });
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

    public String getNEIOutputId() {
        return "ChemicalMetalSeparator";
    }

    // TODO quit hard coding
    protected boolean canCraft(ItemStack material) {
        return !material.isEmpty() && UtilItemStack.areTypeEqual(material, ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust));
    }

    public boolean canProceedCraft() {
        return this.isDoingWork || this.canCraft(this.getStackInSlot(0));
    }

    public void proceedCraft() {
        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        this.craftTime = 0L;
        this.debtEnergy = 0L;
        this.isDoingWork = false;
        UtilTransfer.produceItemStack(this.getStackInSlot(RESULT_SLOT), this.containerItemStacks, 1, 17, this.getInventoryStackLimit());
        this.setInventorySlotContents(RESULT_SLOT, ItemStack.EMPTY);

//                this.syncFlag = true;
//                if (this.externalControlState > 0) {
//                    --this.externalControlState;
//                    if (this.externalControlState == 0) {
//                        this.externalControlState = -1;
//                    }
//                }
    }

    @Override
    protected boolean setNewRecipe() {
        if (!this.canCraft(this.getStackInSlot(0))) return false;

        this.debtEnergy = (long)((float)baseConsumingEnergy * this.multConsumingEnergy);
        this.timeToCraft = (long)((float)baseCraftTime * this.multCraftTime);

        ItemStack result = results.get(random);

        if (result == null
                || UtilTransfer.canProduceItemStack(result, this.containerItemStacks, 1, 17, this.getInventoryStackLimit()) < result.getCount()) {
            return false;
        }

        this.getStackInSlot(0).shrink(1);
        this.setInventorySlotContents(RESULT_SLOT, result);

        return true;
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
