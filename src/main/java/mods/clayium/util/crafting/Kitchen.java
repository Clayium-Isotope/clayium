package mods.clayium.util.crafting;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.IClayEnergyConsumer;

/**
 * 機械のレシピ処理を担うクラス
 */
public abstract class Kitchen implements INBTSerializable<NBTTagCompound> {

    protected long debtEnergy = 0L;
    protected long craftTime = 0L;
    protected long timeToCraft = 0L;

    @Nullable
    protected final IClayEnergyConsumer energyConsumer;

    protected Kitchen() {
        this(null);
    }

    protected Kitchen(IClayEnergyConsumer ceConsumer) {
        this.energyConsumer = ceConsumer;
    }

    // ===== Public Methods =====

    abstract public boolean isDoingWork();

    /**
     * {@link net.minecraft.world.World#isRemote} などで場合分け
     */
    // TODO @SideOnly(Side.SERVER) // 蔵でおこなう必要は無さそうなので。
    public final void work() {
        if (this.isDoingWork() || this.setNewRecipe()) {
            if (this.canProceedCraft()) {
                this.proceedWork();
            }
        }
    }

    public final long getCraftTime() {
        return this.craftTime;
    }

    public final long getTimeToCraft() {
        return this.timeToCraft;
    }

    public final void setCraftTime(long craftTime) {
        this.craftTime = craftTime;
    }

    public final void setTimeToCraft(long timeToCraft) {
        this.timeToCraft = timeToCraft;
    }

    // ===== Internal Methods =====

    protected boolean canProceedCraft() {
        return this.energyConsumer == null ||
                IClayEnergyConsumer.compensateClayEnergy(this.energyConsumer, this.debtEnergy, false);
    }

    protected final void proceedWork() {
        if (this.energyConsumer != null &&
                !IClayEnergyConsumer.compensateClayEnergy(this.energyConsumer, this.debtEnergy)) {
            return;
        }

        this.craftTime++;
        ClayiumCore.logger.info("[Kitchen] " + this.craftTime + " / " + this.timeToCraft);
        if (this.craftTime >= this.timeToCraft) {
            this.postWork();
        }
    }

    protected void postWork() {
        this.craftTime = 0L;
        this.timeToCraft = 0L;
    }

    abstract protected boolean canCraft();

    abstract protected boolean setNewRecipe();

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.craftTime = compound.getLong("CraftTime");
        this.timeToCraft = compound.getLong("TimeToCraft");
        this.debtEnergy = compound.getLong("ConsumingEnergy");

        if (this.energyConsumer != null) {
            this.energyConsumer.containEnergy().set(compound.getLong("ClayEnergy"));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setLong("CraftTime", this.craftTime);
        compound.setLong("TimeToCraft", this.timeToCraft);
        compound.setLong("ConsumingEnergy", this.debtEnergy);

        if (this.energyConsumer != null) {
            compound.setLong("ClayEnergy", this.energyConsumer.containEnergy().get());
        }

        return compound;
    }
}
