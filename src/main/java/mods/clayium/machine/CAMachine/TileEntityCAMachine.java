package mods.clayium.machine.CAMachine;

import mods.clayium.block.ICAMachine;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.RecipeProvider;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.ContainClayEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public abstract class TileEntityCAMachine extends TileEntityClayContainer implements ITickable, IClayEnergyConsumer, ClayiumRecipeProvider<RecipeElement>, ICAMachine {
    private final ContainClayEnergy containEnergy = new ContainClayEnergy();
    protected RecipeElement doingRecipe = RecipeElement.flat();
    protected long craftTime = 0;
    protected long timeToCraft = 0;
    protected long debtEnergy = 0;

    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public float initCraftTime = 1.0F;

    @Override
    public void update() {
        super.update();

        RecipeProvider.update(this);
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.containEnergy;
    }

    @Override
    public int getClayEnergyStorageSize() {
        return 1;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {

    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }


    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return (int) this.timeToCraft;
            case 1:
                return (int) this.craftTime;
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
                this.timeToCraft = value;
                return;
            case 1:
                this.craftTime = value;
                return;
            case 2:
                this.containEnergy().set(value);
                return;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }


    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.craftTime = tagCompound.getLong("CraftTime");
        this.timeToCraft = tagCompound.getLong("TimeToCraft");
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");

        this.containEnergy().set(tagCompound.getLong("ClayEnergy"));

        this.doingRecipe = this.getRecipe(tagCompound.getInteger("RecipeHash"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setLong("CraftTime", this.craftTime);
        tagCompound.setLong("TimeToCraft", this.timeToCraft);
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);

        tagCompound.setLong("ClayEnergy", this.containEnergy.get());

        tagCompound.setInteger("RecipeHash", this.doingRecipe.hashCode());

        return tagCompound;
    }
}
