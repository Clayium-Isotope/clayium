package mods.clayium.block.tile;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.entity.RayTraceMemory;
import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilPlayer;
import mods.clayium.util.UtilTransfer;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;


public class TileAreaActivator
        extends TileAreaMiner
        implements IRayTracer {
    public RayTraceMemory rayTraceMemory;
    public int target = 0;

    public boolean enableRayTrace = false;

    public boolean sneak = false;
    public boolean scanningBlock = true;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, 0, -1, 1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, 0, 0};
        this.clayEnergySlot = 49;
        this.containerItemStacks = new ItemStack[53];
    }


    public void initParamsByTier(int tier) {
        this.inventoryX = this.inventoryY = 3;

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
        this.listSlotsInsert.add(slots);
        this.listSlotsInsert.add(new int[] {this.clayEnergySlot});
        this.listSlotsExtract.add(slots);

        this.slotsDrop[slotNum] = this.clayEnergySlot;

        if (tier >= 7) {
            this.areaMode = true;
        }

        if (!this.areaMode) {


            this.listSlotsInsert.remove(1);
            for (i = 0; i < this.insertRoutes.length; i++) {
                if (this.insertRoutes[i] == 1) this.insertRoutes[i] = -1;

            }
        }
        this.filterHarvestSlot = 50;
        this.filterFortuneSlot = 51;
        this.filterSilktouchSlot = 52;
    }


    public void initState() {
        setState(3);
    }

    public List<Entity> scannedEntityList = new ArrayList<Entity>();

    public void doWork() {
        long c;
        if (this.rayTraceMemory == null && this.worldObj != null) {
            if (!this.areaMode) {
                int m = getFrontDirection();
                if (m >= 0 && m < 6) {
                    this.rayTraceMemory = RayTraceMemory.getStandardMemory(ForgeDirection.getOrientation(m).getOpposite());
                }
            } else {
                this.rayTraceMemory = RayTraceMemory.getStandardMemory(ForgeDirection.DOWN);
            }
        }

        if (this.rayTraceMemory == null) {
            return;
        }
        AxisAlignedBB aabb = getAxisAlignedBB();
        if (this.target == 1 && !this.enableRayTrace) {
            this.scanningBlock = false;
        }
        if (this.target == 0 || this.enableRayTrace) {
            this.scanningBlock = true;
        }
        switch (getState()) {
            case 0:
            case 3:
                if (this.scanningBlock) {
                    this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                    this.miningY = (int) Math.floor(aabb.minY + 0.5D);
                    this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                }
                this.scannedEntityList.clear();
                setState(getState() + 1);
                break;
            case 1:
            case 4:
                if (this.scanningBlock) {
                    long l = this.areaMode ? (long) (this.energyPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L)))) : 0L;
                    if (this.miningX > (int) Math.floor(aabb.maxX - 0.5D)) {
                        this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                        this.miningZ++;
                    }
                    if (this.miningZ > (int) Math.floor(aabb.maxZ - 0.5D)) {
                        this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                        this.miningY++;
                    }
                    if (this.miningY > (int) Math.floor(aabb.maxY - 0.5D)) {
                        if (getState() == 1) {
                            setState(2);
                            break;
                        }
                        this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                        this.miningY = (int) Math.floor(aabb.minY + 0.5D);
                        this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                    }


                    if (consumeClayEnergy(l)) {
                        setSyncFlag();
                        this.progress += (long) (this.progressPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L))));
                        this.laserEnergy = 0L;

                        ItemStack filter = this.containerItemStacks[this.filterHarvestSlot];


                        boolean filterFlag = ItemFilterTemp.isFilter(filter);


                        int slotNum = this.inventoryX * this.inventoryY;

                        int max = (getState() == 1) ? this.maxJobCount : this.maxJobCountInLoop;
                        long i = (long) (this.progressPerJob * 1.0D);
                        boolean flag = true;
                        for (int count = 0; count < max; count++) {
                            if (isCrowded()) {
                                this.progress = 0L;
                            }
                            if (this.progress < i) {
                                flag = false;
                                break;
                            }
                            if (this.enableRayTrace || this.worldObj.getBlock(this.miningX, this.miningY, this.miningZ) == Blocks.air) {
                                ItemStack itemstack = null;
                                int j;
                                for (j = 0; j < 9; j++) {
                                    if (this.containerItemStacks[j] != null) {
                                        itemstack = this.containerItemStacks[j];
                                        break;
                                    }
                                }
                                InventoryPlayer inv = null;
                                if (!this.enableRayTrace) {
                                    inv = this.rayTraceMemory.interactWithBlockFrom(itemstack, this.worldObj, this.miningX, this.miningY, this.miningZ, this.sneak);
                                } else {
                                    MovingObjectPosition mopBlock = null;
                                    MovingObjectPosition mopEntity = null;
                                    if (this.target == 0 || this.target == 2)
                                        mopBlock = this.rayTraceMemory.rayTraceBlockFrom(this.worldObj, this.miningX, this.miningY, this.miningZ, Math.max(this.rayTraceMemory.getLook().lengthVector(), 3.0D), false, false, false);
                                    if (this.target == 1 || this.target == 2)
                                        mopEntity = this.rayTraceMemory.rayTraceEntityFrom(this.worldObj, this.miningX, this.miningY, this.miningZ, Math.max(this.rayTraceMemory.getLook().lengthVector(), 3.0D), Entity.class, IEntitySelector.selectAnything);
                                    Vec3 pos = this.rayTraceMemory.getPos().addVector(this.miningX, this.miningY, this.miningZ);
                                    MovingObjectPosition mop = (mopBlock == null) ? mopEntity : ((mopEntity == null) ? mopBlock : ((pos.distanceTo(mopBlock.hitVec) < pos.distanceTo(mopEntity.hitVec)) ? mopBlock : mopEntity));
                                    if (!filterFlag || mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || ItemFilterTemp.match(filter, this.worldObj, mop.blockX, mop.blockY, mop.blockZ)) {
                                        inv = this.rayTraceMemory.useItemFrom(itemstack, this.worldObj, this.miningX, this.miningY, this.miningZ, mop, this.sneak);
                                    }
                                }
                                if (inv != null)
                                    this.containerItemStacks[j] = inv.getCurrentItem();
                                toMachineInventory(inv);

                                this.progress -= i;
                            }

                            if (flag) {
                                this.miningX++;
                                if (this.miningX > (int) Math.floor(aabb.maxX - 0.5D)) {
                                    this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                                    this.miningZ++;
                                }
                                if (this.miningZ > (int) Math.floor(aabb.maxZ - 0.5D)) {
                                    this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                                    this.miningY++;
                                }
                                if (this.miningY > (int) Math.floor(aabb.maxY - 0.5D)) {
                                    if (this.target == 2 && !this.enableRayTrace) {
                                        this.scanningBlock = false;
                                        this.scannedEntityList.clear();
                                    } else if (getState() == 1) {
                                        setState(2);
                                        break;
                                    }
                                    this.miningX = (int) Math.floor(aabb.minX + 0.5D);
                                    this.miningY = (int) Math.floor(aabb.minY + 0.5D);
                                    this.miningZ = (int) Math.floor(aabb.minZ + 0.5D);
                                }
                            }

                            if (count == max - 1)
                                this.progress = 0L;
                        }
                    }
                    break;
                }
                c = (long) (this.energyPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L))));
                if (consumeClayEnergy(c)) {
                    setSyncFlag();
                    this.progress += (long) (this.progressPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L))));
                    this.laserEnergy = 0L;
                    for (int count = 0; count < this.maxJobCount; count++) {
                        long i = (long) (this.progressPerJob * 1.0D);
                        if (i > this.progress) {
                            break;
                        }
                        List list = this.worldObj.selectEntitiesWithinAABB(Entity.class, this.aabb, IEntitySelector.selectAnything);
                        ItemStack filter = this.containerItemStacks[this.filterHarvestSlot];
                        boolean flag = true;
                        for (Object a : list) {
                            if (isCrowded()) {
                                this.progress = 0L;
                            }
                            if (i > this.progress) {
                                flag = false;
                                break;
                            }
                            if (a instanceof Entity && !this.scannedEntityList.contains(a)) {
                                this.scannedEntityList.add((Entity) a);
                                ItemStack itemstack = null;
                                int j;
                                for (j = 0; j < 9; j++) {
                                    if (this.containerItemStacks[j] != null) {
                                        itemstack = this.containerItemStacks[j];
                                        break;
                                    }
                                }
                                EntityPlayer player = UtilPlayer.getFakePlayerWithItem(null, itemstack);
                                RayTraceMemory.interactWithEntity(player, (Entity) a);
                                InventoryPlayer inv = player.inventory;

                                this.containerItemStacks[j] = inv.getCurrentItem();
                                toMachineInventory(inv);

                                this.progress -= i;
                            }
                        }
                        if (flag) {
                            if (getState() == 1) {
                                this.progress = 0L;
                                setState(2);
                            } else if (this.target == 2 && !this.enableRayTrace) {
                                this.scanningBlock = true;
                            }
                            this.scannedEntityList.clear();
                        }
                    }
                }
                break;
        }

    }


    public void pushButton(EntityPlayer player, int action) {
        super.pushButton(player, action);
        switch (action) {
            case 4:
                if (++this.target >= 3) this.target = 0;
                break;
            case 5:
                this.enableRayTrace = !this.enableRayTrace;
                break;
            case 6:
                this.sneak = !this.sneak;
                break;
        }

    }

    public void readWorkdataFromNBT(NBTTagCompound tagCompound) {
        super.readWorkdataFromNBT(tagCompound);
        this.target = tagCompound.getByte("Target");
        this.enableRayTrace = tagCompound.getBoolean("EnableRayTrace");
        this.sneak = tagCompound.getBoolean("Sneak");
        this.scanningBlock = tagCompound.getBoolean("IsScanningBlock");
        if (tagCompound.hasKey("RayTraceMemory", 10)) {
            if (this.rayTraceMemory == null) {
                this.rayTraceMemory = RayTraceMemory.getFromNBT(tagCompound.getCompoundTag("RayTraceMemory"));
            } else {
                this.rayTraceMemory.readFromNBT(tagCompound.getCompoundTag("RayTraceMemory"));
            }
        }
    }

    public void writeWorkdataToNBT(NBTTagCompound tagCompound) {
        super.writeWorkdataToNBT(tagCompound);
        tagCompound.setByte("Target", (byte) this.target);
        tagCompound.setBoolean("EnableRayTrace", this.enableRayTrace);
        tagCompound.setBoolean("Sneak", this.sneak);
        tagCompound.setBoolean("IsScanningBlock", this.scanningBlock);
        if (this.rayTraceMemory != null) {
            NBTTagCompound tag = new NBTTagCompound();
            this.rayTraceMemory.writeToNBT(tag);
            tagCompound.setTag("RayTraceMemory", (NBTBase) tag);
        }
    }


    public void setRayTraceMemory(RayTraceMemory memory) {
        this.rayTraceMemory = memory;
        markDirty();
    }


    public RayTraceMemory getRayTraceMemory() {
        return this.rayTraceMemory;
    }


    public boolean acceptRayTraceMemory(RayTraceMemory memory) {
        return this.areaMode;
    }

    protected void toMachineInventory(InventoryPlayer inv) {
        if (inv == null)
            return;
        inv.setInventorySlotContents(inv.currentItem, null);
        inv.mainInventory = UtilTransfer.produceItemStacks(inv.mainInventory, this.containerItemStacks, 9, 49, getInventoryStackLimit());
        inv.armorInventory = UtilTransfer.produceItemStacks(inv.armorInventory, this.containerItemStacks, 9, 49, getInventoryStackLimit());
    }

    protected boolean isCrowded() {
        int j = 0;
        boolean ret = false;
        for (int i = 9; i < 49; i++) {
            this.containerItemStacks[i] = UtilTransfer.produceItemStack(this.containerItemStacks[i], this.containerItemStacks, 0, 9, getInventoryStackLimit());
            j = (ret ? 1 : 0) | ((this.containerItemStacks[i] != null) ? 1 : 0);
        }
        return j == 1;
    }
}
