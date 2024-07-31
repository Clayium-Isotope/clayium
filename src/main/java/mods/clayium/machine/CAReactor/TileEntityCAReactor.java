package mods.clayium.machine.CAReactor;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.Interface.ClayInterface.ClayInterface;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.machine.MultiblockMachine.TileEntityMultiblockMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileEntityCAReactor extends TileEntityMultiblockMachine {
    private static final Vec3i[] offsetsd = new Vec3i[]{
            new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(0, 1, 0), new Vec3i(0, -1, 0), new Vec3i(0, 0, 1), new Vec3i(0, 0, -1),
            new Vec3i(1, 1, 0), new Vec3i(1, -1, 0), new Vec3i(-1, 1, 0), new Vec3i(-1, -1, 0),
            new Vec3i(1, 0, 1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(-1, 0, -1),
            new Vec3i(0, 1, 1), new Vec3i(0, 1, -1), new Vec3i(0, -1, 1), new Vec3i(0, -1, -1)
    };
    private static final double efficiency = 0.2;
    private static final double efficiencyBasePerNum = 1.02;
    private static final double efficiencyBasePerRank = 7.5;
    private static final double energyBase = 1.01;
    private static final int reactorHullMinNum = 50;
    private static final int maxLength = 128;

    public TierPrefix reactorTier;
    public double reactorRank = 0.0;
    public int reactorHullNum = 0;
    public int reactorHullMaxRank = 0;
    public final List<Vec3i> coilCoords = new ArrayList<>();
    public final List<Vec3i> hullCoords = new ArrayList<>();
    public String errorMessage = "";

    public double getEfficiency() {
        return efficiency * Math.pow(efficiencyBasePerNum, this.reactorHullNum) * Math.pow(efficiencyBasePerRank, this.reactorRank);
    }

    @Override
    public boolean isConstructed() {
        this.errorMessage = "";
        this.reactorHullNum = 0;
        this.reactorRank = 0.0;

        if (this.coilCoords.isEmpty()) {
            int coilCount = 0;
            WrapMutableBlockPos lastCoil = new WrapMutableBlockPos();
            WrapMutableBlockPos nowCoil = new WrapMutableBlockPos();

            if (!ClayiumBlocks.CAReactorCoil.contains(this.getBlock(EnumFacing.SOUTH.getDirectionVec()))) {
                BlockPos hullCoord = this.getRelativeCoord(EnumFacing.SOUTH.getDirectionVec());
                this.errorMessage = "Coil [" + hullCoord + "] is invalid.";
                if (UtilLocale.canLocalize("message.CAReactor.invalidCoil")) {
                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidCoil", hullCoord.getX(), hullCoord.getY(), hullCoord.getZ());
                }
                return false;
            }
            nowCoil.addAndSet(EnumFacing.SOUTH.getDirectionVec());
            this.coilCoords.add(nowCoil.toImmutable());

            Vec3i offset1;
            ++coilCount;

            do {
                offset1 = Vec3i.NULL_VECTOR;
                int count = 0;
                for (Vec3i rel : offsetsd) {
                    if (ClayiumBlocks.CAReactorCoil.contains(this.getBlock(nowCoil.add(rel)))) {
                        if (!lastCoil.equals(nowCoil.add(rel))) {
                            offset1 = rel;
                        }

                        ++count;
                    }
                }

                if (count > 2 || coilCount == maxLength + 1) {
                    BlockPos hullCoord = this.getRelativeCoord(nowCoil);
                    this.errorMessage = "Coil [" + hullCoord + "] is invalid.";
                    if (UtilLocale.canLocalize("message.CAReactor.invalidCoil")) {
                        this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidCoil", hullCoord.getX(), hullCoord.getY(), hullCoord.getZ());
                    }

                    if (coilCount == maxLength + 1) {
                        this.errorMessage = "The number of coils must not be higher than " + maxLength + ".";
                        if (UtilLocale.canLocalize("message.CAReactor.tooManyCoils")) {
                            this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.tooManyCoils", maxLength);
                        }
                    }

                    this.coilCoords.clear();
                    return false;
                }

                lastCoil.setPos(nowCoil);
                nowCoil.addAndSet(offset1);
                this.coilCoords.add(lastCoil.toImmutable());

                ++coilCount;
            } while (!this.coilCoords.get(0).equals(nowCoil));
            this.hullCoords.clear();
        }

        TierPrefix minTier = TierPrefix.unknown;

        for (Vec3i coilCoord : this.coilCoords) {
            Block block = this.getBlock(coilCoord);
            if (!ClayiumBlocks.CAReactorCoil.contains(block)) {
                BlockPos rel = this.getRelativeCoord(coilCoord);
                this.errorMessage = "Coil [" + rel + "] is invalid.";
                if (UtilLocale.canLocalize("message.CAReactor.invalidCoil")) {
                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidCoil", rel.getX(), rel.getY(), rel.getZ());
                }

                this.coilCoords.clear();
                return false;
            }

            minTier = TierPrefix.min(minTier, ClayiumBlocks.CAReactorCoil.getTier(block));

            if (minTier.compareTo(this.reactorTier) < 0) {
                BlockPos rel = this.getRelativeCoord(coilCoord);
                this.errorMessage = "Coil [" + rel + "]'s tier is insufficient.";
                if (UtilLocale.canLocalize("message.CAReactor.insufficientTierCoil")) {
                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.insufficientTierCoil", rel.getX(), rel.getY(), rel.getZ());
                }

                this.coilCoords.clear();
                return false;
            }
        }

        if (this.hullCoords.isEmpty()) {
            for (Vec3i coilCoord : this.coilCoords) {
                for (EnumFacing facing : EnumFacing.VALUES) {
                    Vec3i offset = UtilVec3i.add(coilCoord, facing.getDirectionVec());
                    Block block = this.getBlock(offset);

                    if (ClayiumBlocks.CAReactorHull.contains(block) || block instanceof ClayInterface) {
                        if (!this.hullCoords.contains(offset)) {
                            this.hullCoords.add(offset);
                        }
                    } else if (!ClayiumBlocks.CAReactorCoil.contains(block) && !BlockPos.ORIGIN.equals(offset)) {
                        // ORIGIN は Core のこと
                        BlockPos rel = this.getRelativeCoord(offset);
                        this.errorMessage = "Hull [" + rel + "] is invalid.";
                        if (UtilLocale.canLocalize("message.CAReactor.invalidHull")) {
                            this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidHull", rel.getX(), rel.getY(), rel.getZ());
                        }

                        this.hullCoords.clear();
                        return false;
                    }
                }
            }
        }

        int numberOfHulls = 0;
        int hullRankSum = 0;

        for (Vec3i hullCoord : this.hullCoords) {
            Block block = this.getBlock(hullCoord);
            int meta = this.getBlockMetadata(hullCoord);

            if (ClayiumBlocks.CAReactorHull.contains(block)) {
                ++numberOfHulls;
                hullRankSum += meta;
                if (meta > this.reactorHullMaxRank) {
                    BlockPos rel = this.getRelativeCoord(hullCoord);
                    this.errorMessage = "Hull [" + rel + "]'s tier is too high.";
                    if (UtilLocale.canLocalize("message.CAReactor.tooHighTierHull")) {
                        this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.tooHighTierHull", rel.getX(), rel.getY(), rel.getZ());
                    }

                    this.hullCoords.clear();
                    return false;
                }
            } else if (block instanceof ClayInterface) {
                BlockPos rel = this.getRelativeCoord(hullCoord);
                TierPrefix tier = ((ITieredBlock) block).getTier(this.world, rel);
                if (tier.compareTo(this.reactorTier) < 0) {
                    this.errorMessage = "Interface [" + rel + "]'s tier is insufficient.";
                    if (UtilLocale.canLocalize("message.CAReactor.insufficientTierInterface")) {
                        this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.insufficientTierInterface", rel.getX(), rel.getY(), rel.getZ());
                    }
                }

                ((ISynchronizedInterface) this.getTileEntity(hullCoord)).setCoreBlock(this);
            } else {
                BlockPos rel = this.getRelativeCoord(hullCoord);
                this.errorMessage = "Hull [" + rel + "] is invalid.";
                if (UtilLocale.canLocalize("message.CAReactor.invalidHull")) {
                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidHull", rel.getX(), rel.getY(), rel.getZ());
                }

                this.hullCoords.clear();
                return false;
            }
        }

        if (numberOfHulls < reactorHullMinNum) {
            this.errorMessage = "Reactor size(" + numberOfHulls + ") is less than minimum size(" + reactorHullMinNum + ").";
            if (UtilLocale.canLocalize("message.CAReactor.invalidReactorSize")) {
                this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidReactorSize", numberOfHulls, reactorHullMinNum);
            }

            return false;
        }

        this.reactorHullNum = numberOfHulls;
        this.reactorRank = (double)hullRankSum / (double)numberOfHulls;

        return true;
    }

    @Override
    protected void onConstruction() {
        this.markDirty(); // this.setRenderSyncFlag();
    }

    @Override
    protected void onDestruction() {
        this.markDirty(); // this.setRenderSyncFlag();
        this.craftTime = 0L;
        this.setInventorySlotContents(3, ItemStack.EMPTY);
    }

    @Override
    public void initParams() {
        super.initParams();
        this.containerItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);
        this.reactorTier = tier;
        switch (tier) {
            case antimatter:
                this.reactorHullMaxRank = 1;
                break;
            case pureAntimatter:
                this.reactorHullMaxRank = 5;
                break;
            default:
                this.reactorHullMaxRank = 9;
        }
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.reactorTier = TierPrefix.get(tagCompound.getShort("ReactorTier"));
        this.reactorRank = tagCompound.getDouble("ReactorRank");
        this.reactorHullNum = tagCompound.getShort("ReactorHullNum");

        if (tagCompound.getBoolean("HasCoilCoords")) {
            NBTTagList tagList = tagCompound.getTagList("CoilCoords", Constants.NBT.TAG_COMPOUND);
            this.coilCoords.clear();

            for (NBTBase tagCpd : tagList) {
                this.coilCoords.add(UtilVec3i.getVec3iFromTag((NBTTagCompound) tagCpd));
            }
        } else {
            this.coilCoords.clear();
        }

        if (tagCompound.getBoolean("HasHullCoords")) {
            NBTTagList tagList = tagCompound.getTagList("HullCoords", Constants.NBT.TAG_COMPOUND);
            this.hullCoords.clear();

            for (NBTBase tagCpd : tagList) {
                this.hullCoords.add(UtilVec3i.getVec3iFromTag((NBTTagCompound) tagCpd));
            }
        } else {
            this.hullCoords.clear();
        }
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setShort("ReactorTier", (short)this.reactorTier.meta());
        tagCompound.setDouble("ReactorRank", this.reactorRank);
        tagCompound.setShort("ReactorHullNum", (short)this.reactorHullNum);
        tagCompound.setBoolean("HasCoilCoords", !this.coilCoords.isEmpty());

        if (!this.coilCoords.isEmpty()) {
            NBTTagList tagList = new NBTTagList();

            for (Vec3i coilCoord : this.coilCoords) {
                tagList.appendTag(UtilVec3i.createVec3iTag(coilCoord));
            }

            tagCompound.setTag("CoilCoords", tagList);
        }

        tagCompound.setBoolean("HasHullCoords", !this.hullCoords.isEmpty());
        if (!this.hullCoords.isEmpty()) {
            NBTTagList tagList = new NBTTagList();

            for (Vec3i hullCoord : this.hullCoords) {
                tagList.appendTag(UtilVec3i.createVec3iTag(hullCoord));
            }

            tagCompound.setTag("HullCoords", tagList);
        }

        return tagCompound;
    }

    public boolean canOverclock() {
        return TierPrefix.OPA.compareTo(this.reactorTier) <= 0;
    }

    @Override
    public void pushButton(EntityPlayer player, int action) {
        if (action == 1 && !player.getEntityWorld().isRemote) {
            player.sendMessage(new TextComponentString(this.errorMessage));
        }
    }

    @Override
    public boolean canProceedCraftWhenConstructed() {
        return super.canProceedCraftWhenConstructed();
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.consumeClayEnergy(this, this.debtEnergy)) {
            return;
        }

        this.craftTime = (long)((double)this.craftTime + 1000.0 * this.getEfficiency());
        if (this.craftTime < this.timeToCraft) {
            return;
        }
        this.craftTime = 0L;
        this.debtEnergy = 0L;
        UtilTransfer.produceItemStack(this.getStackInSlot(3), this.containerItemStacks, 1, this.getInventoryStackLimit());
        this.setInventorySlotContents(3, ItemStack.EMPTY);
//                if (this.externalControlState > 0) {
//                    --this.externalControlState;
//                    if (this.externalControlState == 0) {
//                        this.externalControlState = -1;
//                    }
//                }
    }

    @Override
    public boolean setNewRecipe() {
        int rank = Math.min((int)this.reactorRank, 8);
        if (!UtilItemStack.areItemDamageTagEqual(this.getStackInSlot(0), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem))
                || UtilTransfer.canProduceItemStack(ClayiumMaterials.get(ClayiumMaterial.compressedPureAntimatter.get(rank), ClayiumShape.gem), this.containerItemStacks, 1, this.getInventoryStackLimit()) < 1) {
            return false;
        }
        // レシピが一意に定まるはずだが、リセットされる
        this.doingRecipe = ClayiumRecipes.CAReactor.getRecipe(r -> !r.isFlat(), RecipeElement.flat());

        this.debtEnergy = (long)((double) this.doingRecipe.getEnergy() * this.multConsumingEnergy * this.getConsumingEnergyBaseMultiplier());

        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) {
            return false;
        }

        this.timeToCraft = (long)((double) ((1000L * this.doingRecipe.getTime()) * this.multCraftTime) * this.getCraftTimePureAntimatterMultiplier());
        this.setInventorySlotContents(3, this.getResultPureAntimatter());
        this.getStackInSlot(0).shrink(1);
        return true;
    }

    @Override
    public boolean isDoingWork() {
        return !this.getStackInSlot(3).isEmpty();
    }

    public int getPureAntimatterRank() {
        return Math.min((int)this.reactorRank, 8);
    }

    public double getCraftTimeTotalMultiplier() {
        return this.getCraftTimePureAntimatterMultiplier() * (double)((long)this.getCraftTimeBaseMultiplier()) / this.getEfficiency();
    }

    public double getCraftTimePureAntimatterMultiplier() {
        return Math.pow(9.0, this.getPureAntimatterRank());
    }

    public float getCraftTimeBaseMultiplier() {
        return 1.0F;
    }

    public double getConsumingEnergyBaseMultiplier() {
        return Math.pow(energyBase, (double)this.reactorHullNum * this.reactorRank);
    }

    public double getConsumingEnergyTotalMultiplier() {
        return this.getConsumingEnergyBaseMultiplier() * (double)this.multConsumingEnergy;
    }

    public ItemStack getResultPureAntimatter() {
        int rank = this.getPureAntimatterRank();
        return rank >= 0 && rank < ClayiumMaterial.compressedPureAntimatter.size() ? ClayiumMaterials.get(ClayiumMaterial.compressedPureAntimatter.get(rank), ClayiumShape.gem) : ItemStack.EMPTY;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public int getResultSlotCount() {
        return 1;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return EnumMachineKind.CAReactorCore.getFaceResource();
    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }
}
