package mods.clayium.machine.CAMachine;

import mods.clayium.component.teField.FieldDelegate;
import mods.clayium.component.teField.FieldLong;
import mods.clayium.component.teField.FieldManager;
import mods.clayium.component.value.ContainClayEnergy;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.RecipeProvider;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public abstract class TileEntityCAMachine extends TileEntityClayContainer implements ITickable, IClayEnergyConsumer, ClayiumRecipeProvider<RecipeElement>, FieldDelegate {
    @CapabilityInject(ResonanceHandler.class)
    static Capability<ResonanceHandler> RESONANCE_CAPABILITY = null;

    protected final ContainClayEnergy containEnergy = new ContainClayEnergy();
    protected final ResonanceHandler resonanceHandler = new ResonanceHandler();
    protected RecipeElement doingRecipe = RecipeElement.flat();
    protected final FieldLong craftTime = new FieldLong();
    protected final FieldLong timeToCraft = new FieldLong();
    protected long debtEnergy = 0;

    protected final FieldManager fm = new FieldManager(this.timeToCraft, this.craftTime);

    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public float initCraftTime = 1.0F;

    @Override
    public void update() {
        super.update();

        if (!this.resonanceHandler.isInited) {
            this.resonanceHandler.init(this.getWorld(), this.getPos());
        }

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

    public FieldManager getDelegate() {
        return this.fm;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.craftTime.deserializeNBT((NBTTagLong) tagCompound.getTag("CraftTime"));
        this.timeToCraft.deserializeNBT((NBTTagLong) tagCompound.getTag("TimeToCraft"));
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");

        this.containEnergy().deserializeNBT((NBTTagIntArray) tagCompound.getTag("ClayEnergy"));

        this.doingRecipe = this.getRecipe(tagCompound.getInteger("RecipeHash"));
        this.resonanceHandler.setResonance(tagCompound.getDouble("resonance"));

        RESONANCE_CAPABILITY.readNBT(this.resonanceHandler, null, tagCompound.getCompoundTag("resonance_cap"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setTag("CraftTime", this.craftTime.serializeNBT());
        tagCompound.setTag("TimeToCraft", this.timeToCraft.serializeNBT());
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);

        tagCompound.setTag("ClayEnergy", this.containEnergy.serializeNBT());

        tagCompound.setInteger("RecipeHash", this.doingRecipe.hashCode());
        tagCompound.setDouble("resonance", this.resonanceHandler.getResonance());

        tagCompound.setTag("resonance_cap", RESONANCE_CAPABILITY.writeNBT(this.resonanceHandler, null));

        return tagCompound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == RESONANCE_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == RESONANCE_CAPABILITY)
            return (T) this.resonanceHandler;
        return super.getCapability(capability, facing);
    }
}
