package mods.clayium.component.bot;

import mods.clayium.component.value.Stockholder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class LocalBotIdle implements LocalBot {
    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

    @Override
    public Stockholder progress() {
        return Stockholder.ZERO;
    }

    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, BlockPos blockPos) {
        return EnumBotResult.NotReady;
    }

    @Override
    public void setWorld(World world) {

    }

    @Override
    public boolean hasWorld() {
        return false;
    }

    @Override
    public void setProgressAccess(GeneralBot<?> other) {

    }
}
