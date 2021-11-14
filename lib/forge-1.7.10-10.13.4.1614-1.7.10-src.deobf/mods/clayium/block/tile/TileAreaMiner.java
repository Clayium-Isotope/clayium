package mods.clayium.block.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.laser.ClayLaser;
import mods.clayium.block.laser.IClayLaserMachine;
import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidRegistry;

public class TileAreaMiner
        extends TileClayContainerTiered
        implements IClayLaserMachine, IAxisAlignedBBContainer, IExternalControl {
    protected AxisAlignedBB aabb;
    public int boxState = 0;
    public int inventoryX = 0;
    public int inventoryY = 0;
    public int filterHarvestSlot = 37;
    public int filterFortuneSlot = 38;
    public int filterSilktouchSlot = 39;
    public long laserEnergy = 0L;

    public int miningX = 0;
    public int miningY = 0;
    public int miningZ = 0;
    public int state = -1;
    public long progress = 0L;

    public long energyPerTick = 1000L;
    public int progressPerTick = 100;
    public int progressPerJob = 400;

    public int maxJobCount = 1000;
    public int maxJobCountInLoop = 10;

    public boolean replaceMode = false;

    public boolean areaMode = false;

    public boolean placeFlag = false;


    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, 0, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, 0, 0};
        this.autoInsert = true;
        this.autoExtract = true;
        this.clayEnergySlot = 36;
        this.containerItemStacks = new ItemStack[40];
    }

    public void initParamsByTier(int tier) {
        this.inventoryX = this.inventoryY = 3;
        if (tier >= 10) {
            this.inventoryX = 4;
            this.inventoryY = 2;
        }
        switch (tier) {
            case 6:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
                this.autoExtractInterval = this.autoInsertInterval = 2;
                break;
            case 7:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                break;
            case 8:
            case 9:
            case 10:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                break;
        }

        int slotNum = this.inventoryX * this.inventoryY;

        int[] slots = new int[slotNum];
        this.slotsDrop = new int[slotNum + 1];
        int i;
        for (i = 0; i < slots.length; i++) {
            slots[i] = i;
            this.slotsDrop[i] = i;
        }

        this.listSlotsInsert.add(new int[] {this.clayEnergySlot});
        this.listSlotsExtract.add(slots);

        this.slotsDrop[slotNum] = this.clayEnergySlot;

        if (tier >= 7) {
            this.areaMode = true;
        }
        if (tier >= 10) {
            this.replaceMode = true;
        }

        if (!this.areaMode) {


            this.listSlotsInsert.remove(0);
            for (i = 0; i < this.insertRoutes.length; i++) {
                if (this.insertRoutes[i] == 0) this.insertRoutes[i] = -1;

            }
        }
        if (this.replaceMode) {
            slots = new int[slotNum];
            for (i = 0; i < slots.length; i++) {
                slots[i] = i + slotNum;
            }
            this.listSlotsInsert.add(slots);

            this.slotsDrop = new int[slotNum * 2 + 1];
            for (i = 0; i < slotNum * 2; i++) {
                this.slotsDrop[i] = i;
            }
            this.slotsDrop[slotNum * 2] = this.clayEnergySlot;
        } else {

            for (i = 0; i < this.insertRoutes.length; i++) {
                if (this.insertRoutes[i] == 1) this.insertRoutes[i] = 0;

            }
        }
    }


    public void updateEntity() {
        super.updateEntity();

        if (getState() == -1) {
            initState();
        }


        if (!this.worldObj.isRemote) {
            if (!hasAxisAlignedBB()) {
                searchAABBProvider();
            } else {

                doWork();
            }
        }
    }

    public void initState() {
        if (this.areaMode) {
            setState(0);
        } else {
            setState(3);
        }
    }

    public void searchAABBProvider() {
        if (!this.areaMode) {
            UtilDirection back = UtilDirection.getOrientation(getFrontDirection()).getOpposite();
            setAxisAlignedBB(AxisAlignedBB.getBoundingBox((this.xCoord + back.offsetX), (this.yCoord + back.offsetY), (this.zCoord + back.offsetZ), (this.xCoord + 1 + back.offsetX), (this.yCoord + 1 + back.offsetY), (this.zCoord + 1 + back.offsetZ)));
        } else {

            for (int x = this.xCoord - 2; x <= this.xCoord + 2; x++) {
                for (int y = this.yCoord - 2; y <= this.yCoord + 2; y++) {
                    for (int z = this.zCoord - 2; z <= this.zCoord + 2; z++) {
                        TileEntity tile = UtilBuilder.safeGetTileEntity((IBlockAccess) this.worldObj, x, y, z);
                        if (tile instanceof IAxisAlignedBBProvider && ((IAxisAlignedBBProvider) tile).hasAxisAlignedBB()) {
                            setAxisAlignedBB(((IAxisAlignedBBProvider) tile).getAxisAlignedBB());
                            ((IAxisAlignedBBProvider) tile).setAxisAlignedBBToMachine();
                        }
                    }
                }
            }
        }
    }

    public void doWork() {
        long c;
        AxisAlignedBB aabb = getAxisAlignedBB();
        switch (getState()) {
            case 0:
            case 3:
                this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                this.miningY = (int) Math.floor(aabb.maxY - 0.5D);
                this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                setState(getState() + 1);
                break;
            case 1:
            case 4:
                c = this.areaMode ? (long) (this.energyPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L)))) : 0L;
                if (this.miningX > (int) Math.floor(aabb.maxX - 0.5D)) {
                    this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                    this.miningZ++;
                }
                if (this.miningZ > (int) Math.floor(aabb.maxZ - 0.5D)) {
                    this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                    this.miningY--;
                }
                if (this.miningY < (int) Math.floor(aabb.minY + 0.5D)) {
                    if (getState() == 1) {
                        setState(2);
                        break;
                    }
                    this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                    this.miningY = (int) Math.floor(aabb.maxY - 0.5D);
                    this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                }


                if (consumeClayEnergy(c)) {
                    setSyncFlag();
                    this.progress += (long) (this.progressPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L))));
                    this.laserEnergy = 0L;

                    ItemStack filter = this.containerItemStacks[this.filterHarvestSlot];
                    ItemStack filterF = this.containerItemStacks[this.filterFortuneSlot];
                    ItemStack filterS = this.containerItemStacks[this.filterSilktouchSlot];
                    boolean filterFlag = ItemFilterTemp.isFilter(filter);
                    boolean filterFflag = ItemFilterTemp.isFilter(filterF);
                    boolean filterSflag = ItemFilterTemp.isFilter(filterS);

                    int slotNum = this.inventoryX * this.inventoryY;

                    int max = (getState() == 1) ? this.maxJobCount : this.maxJobCountInLoop;
                    for (int count = 0; count < max; count++) {
                        Block block = this.worldObj.getBlock(this.miningX, this.miningY, this.miningZ);
                        boolean isFluid = (FluidRegistry.lookupFluidForBlock(block) != null || block.getMaterial() instanceof net.minecraft.block.material.MaterialLiquid);
                        double hardness = isFluid ? 1.0D : block.getBlockHardness(this.worldObj, this.miningX, this.miningY, this.miningZ);
                        long i = (long) (this.progressPerJob * (0.1D + hardness));
                        if (block == Blocks.air) i = 0L;
                        i += (this.replaceMode ? 1L : 0L);


                        Boolean flag = Boolean.valueOf(true);
                        if (filterFlag) {
                            flag = Boolean.valueOf(ItemFilterTemp.match(filter, this.worldObj, this.miningX, this.miningY, this.miningZ));
                        }
                        if (hardness == -1.0D) flag = Boolean.valueOf(false);

                        if (flag.booleanValue()) {
                            if (i <= this.progress) {
                                this.progress -= i;

                                int fortune = 0;
                                boolean silkTouch = false;
                                if (filterFflag && ItemFilterTemp.match(filterF, this.worldObj, this.miningX, this.miningY, this.miningZ)) {
                                    fortune = 3;
                                }
                                if (filterSflag && ItemFilterTemp.match(filterS, this.worldObj, this.miningX, this.miningY, this.miningZ)) {
                                    silkTouch = true;
                                    fortune = 0;
                                }

                                ItemStack[] items = UtilBuilder.getDropsFromBlock(this.worldObj, this.miningX, this.miningY, this.miningZ, silkTouch, fortune);

                                if (UtilTransfer.canProduceItemStacks(items, this.containerItemStacks, 0, slotNum, getInventoryStackLimit())) {
                                    UtilBuilder.destroyBlock(this.worldObj, this.miningX, this.miningY, this.miningZ, !silkTouch, silkTouch, fortune);
                                    UtilTransfer.produceItemStacks(items, this.containerItemStacks, 0, slotNum, getInventoryStackLimit());
                                    if (this.replaceMode) {
                                        this.placeFlag = true;
                                    }
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (this.replaceMode &&
                                this.placeFlag) {
                            boolean flag2 = false;
                            if (this.worldObj.getBlock(this.miningX, this.miningY, this.miningZ) == Blocks.air)
                                for (int j = slotNum; j < slotNum * 2; j++) {
                                    ItemStack itemblock = this.containerItemStacks[j];
                                    if (itemblock != null && itemblock.getItem() instanceof net.minecraft.item.ItemBlock) {
                                        flag2 = UtilBuilder.placeBlockByItemBlock(itemblock, this.worldObj, this.miningX, this.miningY, this.miningZ);
                                        if (itemblock == null || itemblock.stackSize == 0) {
                                            this.containerItemStacks[j] = null;
                                        }
                                    }
                                    if (flag2)
                                        break;
                                }
                            if (!flag2) {
                                this.progress = 0L;
                                break;
                            }
                            this.placeFlag = false;
                        }

                        this.miningX++;
                        if (this.miningX > (int) Math.floor(aabb.maxX - 0.5D)) {
                            this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                            this.miningZ++;
                        }
                        if (this.miningZ > (int) Math.floor(aabb.maxZ - 0.5D)) {
                            this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                            this.miningY--;
                        }
                        if (this.miningY < (int) Math.floor(aabb.minY + 0.5D)) {
                            if (getState() == 1) {
                                setState(2);
                                break;
                            }
                            this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                            this.miningY = (int) Math.floor(aabb.maxY - 0.5D);
                            this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                        }


                        if (count == max - 1)
                            this.progress = 0L;
                    }
                }
                break;
        }

    }

    public void pushButton(EntityPlayer player, int action) {
        int b;
        switch (action) {
            case 0:
                setState(0);
                break;
            case 1:
                setState(2);
                break;
            case 2:
                setState(3);
                break;
            case 3:
                b = getBoxState();
                if (++b > 2) b = 0;
                setBoxState(b);
                break;
        }

    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        setSyncFlag();
        this.state = state;
    }

    public int getBoxState() {
        return this.boxState;
    }

    public void setBoxState(int boxState) {
        setSyncFlag();
        this.boxState = boxState;
    }


    public void openInventory() {}


    public void closeInventory() {}

    public boolean irradiateClayLaser(ClayLaser laser, UtilDirection direction) {
        this.laserEnergy = (long) (this.laserEnergy + laser.getEnergy());
        return true;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.boxState = tagCompound.getByte("BoxState");
        TileClayMarker.readAxisAlignedBBFromNBT(tagCompound, this);
        readWorkdataFromNBT(tagCompound);
    }

    public void readWorkdataFromNBT(NBTTagCompound tagCompound) {
        this.laserEnergy = tagCompound.getLong("LaserEnergy");
        this.miningX = tagCompound.getInteger("MiningX");
        this.miningY = tagCompound.getInteger("MiningY");
        this.miningZ = tagCompound.getInteger("MiningZ");
        this.state = tagCompound.getByte("State");
        this.progress = tagCompound.getLong("Progress");
        this.placeFlag = tagCompound.getBoolean("PlaceFlag");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setByte("BoxState", (byte) this.boxState);
        TileClayMarker.writeAxisAlignedBBToNBT(tagCompound, this);
        writeWorkdataToNBT(tagCompound);
    }

    public void writeWorkdataToNBT(NBTTagCompound tagCompound) {
        tagCompound.setLong("LaserEnergy", this.laserEnergy);
        tagCompound.setInteger("MiningX", this.miningX);
        tagCompound.setInteger("MiningY", this.miningY);
        tagCompound.setInteger("MiningZ", this.miningZ);
        tagCompound.setByte("State", (byte) this.state);
        tagCompound.setLong("Progress", this.progress);
        tagCompound.setBoolean("PlaceFlag", this.placeFlag);
    }


    public AxisAlignedBB getAxisAlignedBB() {
        return this.aabb;
    }


    public void setAxisAlignedBB(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }


    public boolean hasAxisAlignedBB() {
        return (this.aabb != null);
    }


    public int getBoxAppearance() {
        return this.boxState;
    }


    public boolean shouldRenderInPass(int pass) {
        return (pass == 1);
    }


    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Double.POSITIVE_INFINITY;
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot == this.filterHarvestSlot || slot == this.filterFortuneSlot || slot == this.filterSilktouchSlot) {
            return ItemFilterTemp.isFilter(itemstack);
        }
        return super.isItemValidForSlot(slot, itemstack);
    }

    public void doWorkOnce() {
        setState(0);
    }


    public void startWork() {
        setState(4);
    }


    public void stopWork() {
        setState(2);
    }


    public boolean isScheduled() {
        return true;
    }

    public boolean isDoingWork() {
        return (getState() == 1 || getState() == 4);
    }
}
