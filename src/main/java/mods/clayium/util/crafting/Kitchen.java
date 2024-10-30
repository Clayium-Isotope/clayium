package mods.clayium.util.crafting;

import mods.clayium.component.teField.FieldDelegate;
import mods.clayium.component.teField.FieldLong;
import mods.clayium.component.teField.FieldManager;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * 機械のレシピ処理を担うクラス
 */
public abstract class Kitchen implements INBTSerializable<NBTTagCompound>, FieldDelegate {

    protected final FieldLong timeToCraft = new FieldLong();
    protected final FieldLong craftTime = new FieldLong();
    protected long debtEnergy =  0L;

    protected final FieldManager fm = new FieldManager(this.timeToCraft, this.craftTime);

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
        return this.craftTime.get();
    }

    public final long getTimeToCraft() {
        return this.timeToCraft.get();
    }

    public final void setCraftTime(long craftTime) {
        this.craftTime.set(craftTime);
    }

    public final void setTimeToCraft(long timeToCraft) {
        this.timeToCraft.set(timeToCraft);
    }

    // ===== Internal Methods =====

    protected boolean canProceedCraft() {
        return this.energyConsumer == null ||
                IClayEnergyConsumer.compensateClayEnergy(this.energyConsumer, this.debtEnergy);
    }

    protected final void proceedWork() {
        if (this.energyConsumer != null &&
                !IClayEnergyConsumer.compensateClayEnergy(this.energyConsumer, this.debtEnergy)) {
            return;
        }

        this.craftTime.add(1);
        ClayiumCore.logger.info("[Kitchen] " + this.craftTime + " / " + this.timeToCraft);
        if (this.craftTime.get() >= this.timeToCraft.get()) {
            this.postWork();
        }
    }

    protected void postWork() {
        this.craftTime.set(0);
        this.timeToCraft.set(0);
    }

    protected abstract boolean canCraft();

    protected abstract boolean setNewRecipe();

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.craftTime.deserializeNBT((NBTTagLong) compound.getTag("CraftTime"));
        this.timeToCraft.deserializeNBT((NBTTagLong) compound.getTag("TimeToCraft"));
        this.debtEnergy = compound.getLong("ConsumingEnergy");

        if (this.energyConsumer != null) {
            this.energyConsumer.containEnergy().deserializeNBT((NBTTagIntArray) compound.getTag("ClayEnergy"));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setTag("CraftTime", this.craftTime.serializeNBT());
        compound.setTag("TimeToCraft", this.timeToCraft.serializeNBT());
        compound.setLong("ConsumingEnergy", this.debtEnergy);

        if (this.energyConsumer != null) {
            compound.setTag("ClayEnergy", this.energyConsumer.containEnergy().serializeNBT());
        }

        return compound;
    }

    @Override
    public FieldManager getDelegate() {
        return this.fm;
    }
}
