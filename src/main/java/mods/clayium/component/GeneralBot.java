package mods.clayium.component;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface GeneralBot<Context> extends INBTSerializable<NBTTagCompound> {

    boolean isReady();

    default void clearProgress() {
        this.progress().clear();
    }
    default void addProgress(long progress) {
        this.progress().add(progress);
    }
    default void declineProgress(long progress) {
        this.progress().sub(progress);
    }
    default boolean hasEnoughProgress(long needed) {
        return this.progress().compareTo(needed) >= 0;
    }
    Stockholder progress();

    default long progressPerJob() {
        return -1;
    }

    EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, Context context);

}
