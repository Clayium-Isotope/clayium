package mods.clayium.block.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.CBlocks;
import mods.clayium.block.ITieredBlock;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilLocale;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;

public class TileCAReactor
        extends TileMultiblockMachines {
    public int reactorTier = 0;
    public double reactorRank = 0.0D;
    public int reactorHullNum = 0;
    public int reactorHullMaxRank = 0;

    public static double efficiency = 0.2D;
    public static double efficiencyBasePerNum = 1.02D;
    public static double efficiencyBasePerRank = 7.5D;
    public static double energyBase = 1.01D;

    public static int reactorHullMinNum = 50;

    public List<int[]> coilCoords;
    public List<int[]> hullCoords;
    public static final int[][] offsets = new int[][] {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
    public static final int[][] offsetsd = new int[][] {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}, {1, 1, 0}, {1, -1, 0}, {-1, 1, 0}, {-1, -1, 0}, {1, 0, 1}, {1, 0, -1}, {-1, 0, 1}, {-1, 0, -1}, {0, 1, 1}, {0, 1, -1}, {0, -1, 1}, {0, -1, -1}};


    public static int maxLength = 128;

    public String errorMessage = "";

    public double getEfficiency() {
        return efficiency * Math.pow(efficiencyBasePerNum, this.reactorHullNum) * Math.pow(efficiencyBasePerRank, this.reactorRank);
    }


    public boolean isConstructed() {
        this.errorMessage = "";
        this.reactorHullNum = 0;
        this.reactorRank = 0.0D;
        int xx0 = 0, yy0 = 0, zz0 = 0;
        int xx1 = 0, yy1 = 0, zz1 = 0;
        boolean res = true;

        if (this.coilCoords == null || this.coilCoords.size() == 0) {
            if (this.coilCoords == null) this.coilCoords = new ArrayList<int[]>();
            xx0 = 0;
            yy0 = 0;
            zz0 = 0;
            xx1 = 0;
            yy1 = 0;
            zz1 = 0;
            int[][] offsets0 = {{0, 0, 1}};

            for (int i = 0; ; i++) {
                int offset1[] = null, count = 0;
                for (int[] offset : offsets0) {
                    if (getBlock(xx1 + offset[0], yy1 + offset[1], zz1 + offset[2]) == CBlocks.blockCAReactorCoil) {
                        if (xx0 != xx1 + offset[0] || yy0 != yy1 + offset[1] || zz0 != zz1 + offset[2]) {
                            offset1 = offset;
                        }
                        count++;
                    }
                }
                if (count > 2 || offset1 == null || i == maxLength + 1) {
                    int[] rel = getRelativeCoord(xx1, yy1, zz1);
                    this.errorMessage = "Coil [" + rel[0] + "," + rel[1] + "," + rel[2] + "] is invalid.";
                    if (UtilLocale.canLocalize("message.CAReactor.invalidCoil"))
                        this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidCoil", new Object[] {Integer.valueOf(rel[0]), Integer.valueOf(rel[1]), Integer.valueOf(rel[2])});
                    if (i == maxLength + 1) {
                        this.errorMessage = "The number of coils must not be higher than " + maxLength + ".";
                        if (UtilLocale.canLocalize("message.CAReactor.tooManyCoils"))
                            this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.tooManyCoils", new Object[] {Integer.valueOf(maxLength)});
                    }
                    res = false;
                    this.coilCoords = null;
                    break;
                }
                xx0 = xx1;
                yy0 = yy1;
                zz0 = zz1;
                xx1 += offset1[0];
                yy1 += offset1[1];
                zz1 += offset1[2];
                if (xx0 != 0 || yy0 != 0 || zz0 != 0)
                    this.coilCoords.add(new int[] {xx0, yy0, zz0});
                offsets0 = offsetsd;
                if (this.coilCoords.size() != 0 && ((int[]) this.coilCoords
                        .get(0))[0] == xx1 && ((int[]) this.coilCoords.get(0))[1] == yy1 && ((int[]) this.coilCoords.get(0))[2] == zz1) {
                    break;
                }
            }
        }

        if (this.coilCoords != null && this.coilCoords.size() != 0) {


            int minTier = 99;
            for (int[] coilCoord : this.coilCoords) {
                Block block = getBlock(coilCoord[0], coilCoord[1], coilCoord[2]);
                int meta = getBlockMetadata(coilCoord[0], coilCoord[1], coilCoord[2]);
                if (block == CBlocks.blockCAReactorCoil) {
                    String id = CBlocks.blockCAReactorCoil.getBlockName(meta);
                    if (id == "antimatter") {
                        minTier = Math.min(minTier, 10);
                    } else if (id == "pureantimatter") {
                        minTier = Math.min(minTier, 11);
                    } else if (id == "oec") {
                        minTier = Math.min(minTier, 12);
                    } else if (id == "opa") {
                        minTier = Math.min(minTier, 13);
                    }
                    if (minTier < this.reactorTier) {
                        int[] arrayOfInt = getRelativeCoord(coilCoord[0], coilCoord[1], coilCoord[2]);
                        this.errorMessage = "Coil [" + arrayOfInt[0] + "," + arrayOfInt[1] + "," + arrayOfInt[2] + "]'s tier is insufficient.";
                        if (UtilLocale.canLocalize("message.CAReactor.insufficientTierCoil"))
                            this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.insufficientTierCoil", new Object[] {Integer.valueOf(arrayOfInt[0]), Integer.valueOf(arrayOfInt[1]), Integer.valueOf(arrayOfInt[2])});
                        res = false;
                        break;
                    }
                    continue;
                }
                int[] rel = getRelativeCoord(coilCoord[0], coilCoord[1], coilCoord[2]);
                this.errorMessage = "Coil [" + rel[0] + "," + rel[1] + "," + rel[2] + "] is invalid.";
                if (UtilLocale.canLocalize("message.CAReactor.invalidCoil"))
                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidCoil", new Object[] {Integer.valueOf(rel[0]), Integer.valueOf(rel[1]), Integer.valueOf(rel[2])});
                res = false;
                this.coilCoords = null;
            }


            if (this.coilCoords != null && this.coilCoords.size() != 0 && res) {

                if (this.hullCoords == null || this.hullCoords.size() == 0) {
                    if (this.hullCoords == null) this.hullCoords = new ArrayList<int[]>();
                    for (int[] coilCoord : this.coilCoords) {
                        for (int[] offset : offsets) {
                            Block block = getBlock(coilCoord[0] + offset[0], coilCoord[1] + offset[1], coilCoord[2] + offset[2]);
                            if (block == CBlocks.blockCAReactorHull || block instanceof mods.clayium.block.ClayInterface || block instanceof mods.clayium.block.RedstoneInterface) {


                                boolean flag = true;
                                for (int[] hullCoord : this.hullCoords) {
                                    if (hullCoord[0] == coilCoord[0] + offset[0] && hullCoord[1] == coilCoord[1] + offset[1] && hullCoord[2] == coilCoord[2] + offset[2])
                                        flag = false;
                                }
                                if (flag)
                                    this.hullCoords.add(new int[] {coilCoord[0] + offset[0], coilCoord[1] + offset[1], coilCoord[2] + offset[2]});
                            } else if (block != CBlocks.blockCAReactorCoil && (coilCoord[0] + offset[0] != 0 || coilCoord[1] + offset[1] != 0 || coilCoord[2] + offset[2] != 0)) {


                                int[] rel = getRelativeCoord(coilCoord[0] + offset[0], coilCoord[1] + offset[1], coilCoord[2] + offset[2]);
                                this.errorMessage = "Hull [" + rel[0] + "," + rel[1] + "," + rel[2] + "] is invalid.";
                                if (UtilLocale.canLocalize("message.CAReactor.invalidHull"))
                                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidHull", new Object[] {Integer.valueOf(rel[0]), Integer.valueOf(rel[1]), Integer.valueOf(rel[2])});
                                res = false;
                                this.hullCoords = null;

                                // Byte code: goto -> 1595
                            }
                        }
                    }
                }
                if (this.hullCoords != null && this.hullCoords.size() != 0) {


                    int numberOfHulls = 0;
                    int hullRankSum = 0;
                    for (int[] hullCoord : this.hullCoords) {
                        Block block = getBlock(hullCoord[0], hullCoord[1], hullCoord[2]);
                        int meta = getBlockMetadata(hullCoord[0], hullCoord[1], hullCoord[2]);
                        if (block == CBlocks.blockCAReactorHull) {
                            numberOfHulls++;
                            hullRankSum += meta;
                            if (meta > this.reactorHullMaxRank) {
                                int[] arrayOfInt = getRelativeCoord(hullCoord[0], hullCoord[1], hullCoord[2]);
                                this.errorMessage = "Hull [" + arrayOfInt[0] + "," + arrayOfInt[1] + "," + arrayOfInt[2] + "]'s tier is too high.";
                                if (UtilLocale.canLocalize("message.CAReactor.tooHighTierHull"))
                                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.tooHighTierHull", new Object[] {Integer.valueOf(arrayOfInt[0]), Integer.valueOf(arrayOfInt[1]), Integer.valueOf(arrayOfInt[2])});
                                res = false;
                                break;
                            }
                            continue;
                        }
                        if (block instanceof mods.clayium.block.ClayInterface || block instanceof mods.clayium.block.RedstoneInterface) {


                            int[] coord = getRelativeCoord(0, 0, 0);
                            int[] arrayOfInt1 = getRelativeCoord(hullCoord[0], hullCoord[1], hullCoord[2]);

                            int tier = ((ITieredBlock) block).getTier((IBlockAccess) this.worldObj, arrayOfInt1[0], arrayOfInt1[1], arrayOfInt1[2]);
                            if (tier < this.reactorTier) {
                                this.errorMessage = "Interface [" + arrayOfInt1[0] + "," + arrayOfInt1[1] + "," + arrayOfInt1[2] + "]'s tier is insufficient.";
                                if (UtilLocale.canLocalize("message.CAReactor.insufficientTierInterface")) {
                                    this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.insufficientTierInterface", new Object[] {Integer.valueOf(arrayOfInt1[0]), Integer.valueOf(arrayOfInt1[1]), Integer.valueOf(arrayOfInt1[2])});
                                }
                            }
                            ((ISynchronizedInterface) getTileEntity(hullCoord[0], hullCoord[1], hullCoord[2]))
                                    .setCoreBlockCoord(coord[0] - arrayOfInt1[0], coord[1] - arrayOfInt1[1], coord[2] - arrayOfInt1[2]);
                            continue;
                        }
                        int[] rel = getRelativeCoord(hullCoord[0], hullCoord[1], hullCoord[2]);
                        this.errorMessage = "Hull [" + rel[0] + "," + rel[1] + "," + rel[2] + "] is invalid.";
                        if (UtilLocale.canLocalize("message.CAReactor.invalidHull"))
                            this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidHull", new Object[] {Integer.valueOf(rel[0]), Integer.valueOf(rel[1]), Integer.valueOf(rel[2])});
                        res = false;
                        this.hullCoords = null;
                    }


                    if (numberOfHulls < reactorHullMinNum && res) {
                        this.errorMessage = "Reactor size(" + numberOfHulls + ") is less than minimum size(" + reactorHullMinNum + ").";
                        if (UtilLocale.canLocalize("message.CAReactor.invalidReactorSize"))
                            this.errorMessage = UtilLocale.localizeAndFormat("message.CAReactor.invalidReactorSize", new Object[] {Integer.valueOf(numberOfHulls), Integer.valueOf(reactorHullMinNum)});
                        res = false;
                    }
                    if (res) {
                        this.reactorHullNum = numberOfHulls;
                        this.reactorRank = hullRankSum / numberOfHulls;
                    }
                }
            }
        }


        return res;
    }


    protected void onConstruction() {
        setRenderSyncFlag();
    }


    protected void onDestruction() {
        setRenderSyncFlag();
        this.machineCraftTime = 0L;
        this.containerItemStacks[2] = null;
    }

    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        this.reactorTier = tier;
        switch (tier) {
            case 10:
                this.reactorHullMaxRank = 1;
                return;
            case 11:
                this.reactorHullMaxRank = 5;
                return;
        }
        this.reactorHullMaxRank = 9;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);


        this.reactorTier = tagCompound.getShort("ReactorTier");
        this.reactorRank = tagCompound.getDouble("ReactorRank");
        this.reactorHullNum = tagCompound.getShort("ReactorHullNum");

        if (tagCompound.getBoolean("HasCoilCoords")) {
            NBTTagList tagList = tagCompound.getTagList("CoilCoords", 11);
            this.coilCoords = new ArrayList<int[]>();
            for (int i = 0; i < tagList.tagCount(); i++) {
                this.coilCoords.add(tagList.func_150306_c(i));
            }
        } else {
            this.coilCoords = null;
        }
        if (tagCompound.getBoolean("HasHullCoords")) {
            NBTTagList tagList = tagCompound.getTagList("HullCoords", 11);
            this.hullCoords = new ArrayList<int[]>();
            for (int i = 0; i < tagList.tagCount(); i++) {
                this.hullCoords.add(tagList.func_150306_c(i));
            }
        } else {
            this.hullCoords = null;
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setShort("ReactorTier", (short) this.reactorTier);
        tagCompound.setDouble("ReactorRank", this.reactorRank);
        tagCompound.setShort("ReactorHullNum", (short) this.reactorHullNum);

        tagCompound.setBoolean("HasCoilCoords", (this.coilCoords != null));
        if (this.coilCoords != null) {
            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < this.coilCoords.size(); i++) {
                tagList.appendTag((NBTBase) new NBTTagIntArray(this.coilCoords.get(i)));
            }
            tagCompound.setTag("CoilCoords", (NBTBase) tagList);
        }

        tagCompound.setBoolean("HasHullCoords", (this.hullCoords != null));
        if (this.hullCoords != null) {
            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < this.hullCoords.size(); i++) {
                tagList.appendTag((NBTBase) new NBTTagIntArray(this.hullCoords.get(i)));
            }
            tagCompound.setTag("HullCoords", (NBTBase) tagList);
        }
    }

    public boolean canOverclock() {
        return (this.reactorTier >= 13);
    }


    public void pushButton(EntityPlayer player, int action) {
        if (action == 1 && !(player.getEntityWorld()).isRemote) {
            player.addChatMessage((IChatComponent) new ChatComponentText(this.errorMessage));
        }
    }


    public boolean canProceedCraftWhenConstructed() {
        if (this.containerItemStacks[2] != null) {
            return true;
        }
        int rank = Math.min((int) this.reactorRank, 8);

        return (UtilItemStack.areTypeEqual(this.containerItemStacks[0], CMaterials.get(CMaterials.ANTIMATTER, CMaterials.GEM)) &&
                UtilTransfer.canProduceItemStack(CMaterials.get(CMaterials.COMPRESSED_PURE_ANTIMATTER[rank], CMaterials.GEM), this.containerItemStacks, 1,
                        getInventoryStackLimit()) >= 1);
    }

    public void proceedCraft() {
        if (this.containerItemStacks[2] == null) {
            this.machineConsumingEnergy = (long) ((float) this.recipe.getEnergy(this.containerItemStacks[0], this.baseTier) * this.multConsumingEnergy);
            this.machineConsumingEnergy = (long) (this.machineConsumingEnergy * getConsumingEnergyBaseMultiplier());
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {

            this.machineTimeToCraft = (long) ((float) (1000L * this.recipe.getTime(this.containerItemStacks[0], this.baseTier)) * this.multCraftTime);
            int rank = getPureAntimatterRank();
            this.machineTimeToCraft = (long) (this.machineTimeToCraft * getCraftTimePureAntimatterMultiplier());
            this.containerItemStacks[2] = getResultPureAntimatter();
            if (this.containerItemStacks[2] == null && --(this.containerItemStacks[0]).stackSize <= 0)
                this.containerItemStacks[0] = null;

            this.machineCraftTime = (long) (this.machineCraftTime + 1000.0D * getEfficiency());
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                UtilTransfer.produceItemStack(this.containerItemStacks[2], this.containerItemStacks, 1, getInventoryStackLimit());
                this.containerItemStacks[2] = null;

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }

    public int getPureAntimatterRank() {
        return Math.min((int) this.reactorRank, 8);
    }

    public double getCraftTimeTotalMultiplier() {
        return getCraftTimePureAntimatterMultiplier() * (long) getCraftTimeBaseMultiplier() / getEfficiency();
    }

    public double getCraftTimePureAntimatterMultiplier() {
        return Math.pow(9.0D, getPureAntimatterRank());
    }

    public float getCraftTimeBaseMultiplier() {
        return 1.0F;
    }

    public double getConsumingEnergyBaseMultiplier() {
        return Math.pow(energyBase, this.reactorHullNum * this.reactorRank);
    }

    public double getConsumingEnergyTotalMultiplier() {
        return getConsumingEnergyBaseMultiplier() * this.multConsumingEnergy;
    }

    public ItemStack getResultPureAntimatter() {
        int rank = getPureAntimatterRank();
        return (rank >= 0 && rank < CMaterials.COMPRESSED_PURE_ANTIMATTER.length) ? CMaterials.get(CMaterials.COMPRESSED_PURE_ANTIMATTER[rank], CMaterials.GEM) : null;
    }


    public boolean shouldRenderInPass(int pass) {
        return (pass == 1);
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }
}
