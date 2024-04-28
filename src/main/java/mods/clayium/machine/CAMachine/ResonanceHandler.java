package mods.clayium.machine.CAMachine;

import mods.clayium.block.CAResonator;
import mods.clayium.util.LateInit;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;

public class ResonanceHandler {
    public static final int RESONATE_RANGE = 2;

    public boolean isInited = false;
    protected final LateInit<World> world = new LateInit<>();
    protected final LateInit<BlockPos> pos = new LateInit<>();
    protected double resonance = 1.0;

    public ResonanceHandler() {}

    public void init(World world, BlockPos pos) {
        this.isInited = true;
        this.world.set(world);
        this.pos.set(pos);
        this.resonance = getResonance(world, pos);
    }

    static double getResonance(World world, BlockPos caMachine) {
        double totalResonance = 1.0;

        for (BlockPos pos : BlockPos.getAllInBox(-RESONATE_RANGE, -RESONATE_RANGE, -RESONATE_RANGE, RESONATE_RANGE, RESONATE_RANGE, RESONATE_RANGE)) {
            Block block = world.getBlockState(caMachine.add(pos)).getBlock();
            if (block instanceof CAResonator) {
                totalResonance *= ((CAResonator) block).getResonance(world, caMachine.add(pos));
            }
        }

        return totalResonance;
    }

    public void markRecalcResonance() {
        if (isInited) {
            this.resonance = getResonance(world.get(), pos.get());
            world.get().markBlockRangeForRenderUpdate(pos.get(), pos.get());
        }
    }

    public double getResonance() {
        return this.resonance;
    }

    public void setResonance(double resonance) {
        this.resonance = resonance;
    }

    private static class Storage implements Capability.IStorage<ResonanceHandler> {
        @Override
        public NBTBase writeNBT(Capability<ResonanceHandler> capability, ResonanceHandler instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setDouble("resonance", instance.resonance);
            return compound;
        }

        @Override
        public void readNBT(Capability<ResonanceHandler> capability, ResonanceHandler instance, EnumFacing side, NBTBase nbt) {
            if (nbt.getId() == Constants.NBT.TAG_COMPOUND) {
                NBTTagCompound tag = (NBTTagCompound) nbt;
                instance.resonance = tag.getDouble("resonance");
            }
        }
    }

    static {
        CapabilityManager.INSTANCE.register(ResonanceHandler.class, new Storage(), ResonanceHandler::new);
    }
}
