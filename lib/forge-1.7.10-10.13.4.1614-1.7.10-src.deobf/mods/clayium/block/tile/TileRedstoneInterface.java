package mods.clayium.block.tile;

import mods.clayium.item.CItems;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class TileRedstoneInterface extends TileGeneric implements ISynchronizedInterface {
    protected int coreBlockX = 0;
    protected int coreBlockY = 0;
    protected int coreBlockZ = 0;
    protected boolean synced = false;
    protected int lastSignal = 0;
    protected int lastPower = 0;

    public static String[] stateSequence = new String[] {"None", "Emit if idle", "Emit if work scheduled", "Emit if doing work", "Do work", "Do not work", "Start work", "Stop work", "Do work once"};


    protected String state = "";

    public boolean setCoreBlockCoord(int x, int y, int z) {
        setSyncFlag();
        this.coreBlockX = x;
        this.coreBlockY = y;
        this.coreBlockZ = z;
        return syncCoreBlock();
    }

    public int getCoreBlockXCoord() {
        return this.coreBlockX;
    }

    public int getCoreBlockYCoord() {
        return this.coreBlockY;
    }

    public int getCoreBlockZCoord() {
        return this.coreBlockZ;
    }

    protected boolean syncCoreBlock() {
        return syncCoreBlock(this.coreBlockX, this.coreBlockY, this.coreBlockZ);
    }

    public boolean isSynced() {
        return this.synced;
    }


    public void updateEntity() {
        syncCoreBlock();
        super.updateEntity();

        int signal = getSignal();
        int power = getProvidingWeakPower();
        if (isSynced()) {
            IExternalControl te = getCore();

            if (this.state.equals("Do work")) {
                if (this.lastSignal <= 0 && signal > 0) {
                    te.startWork();
                }
                if (this.lastSignal != 0 && signal == 0) {
                    te.stopWork();
                }
            }
            if (this.state.equals("Do not work")) {
                if (this.lastSignal <= 0 && signal > 0) {
                    te.stopWork();
                }
                if (this.lastSignal != 0 && signal == 0) {
                    te.startWork();
                }
            }
            if (this.state.equals("Start work") &&
                    this.lastSignal <= 0 && signal > 0) {
                te.startWork();
            }

            if (this.state.equals("Stop work") &&
                    this.lastSignal <= 0 && signal > 0) {
                te.stopWork();
            }

            if (this.state.equals("Do work once") &&
                    this.lastSignal <= 0 && signal > 0) {
                te.doWorkOnce();
            }
        }


        if (this.lastPower != power || this.lastSignal != signal) {
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        this.lastSignal = signal;
        this.lastPower = power;
    }


    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        if (itemStack == null)
            return -1;
        if (itemStack.getItem() == CItems.itemSynchronizer) {
            return 90;
        }
        if (UtilItemStack.areItemDamageEqual(itemStack, CItems.itemMisc.get("SynchronousParts"))) {
            return 91;
        }
        return -1;
    }

    public boolean isUsable(ItemStack itemStack, EntityPlayer player, int direction, float hitX, float hitY, float hitZ) {
        if (getItemUseMode(itemStack, player) == 90 || getItemUseMode(itemStack, player) == 91)
            return true;
        return (syncCoreBlock() && super.isUsable(itemStack, player, direction, hitX, hitY, hitZ));
    }


    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, int side, int mode) {
        this.acceptCoordChanger = true;
        if (mode == 91 && !this.acceptCoordChanger && --(player.getCurrentEquippedItem()).stackSize == 0)
            player.inventory.mainInventory[player.inventory.currentItem] = null;

        if (mode == 90 && itemStack.hasTagCompound() && this.acceptCoordChanger) {
            NBTTagCompound tag = itemStack.getTagCompound();
            int cx = tag.getInteger("CoordMemoryX");
            int cy = tag.getInteger("CoordMemoryY");
            int cz = tag.getInteger("CoordMemoryZ");
            int cd = tag.getInteger("CoordMemoryDimID");
            if (setCoreBlockDimension(cd) &&
                    setCoreBlockCoord(cx - this.xCoord, cy - this.yCoord, cz - this.zCoord)) {
                player.addChatMessage(new ChatComponentText("Set Core block Coord (" + cx + "," + cy + "," + cz + ") in dim " + cd));
            }
        } else if (syncCoreBlock()) {
            super.useItemFromSide(itemStack, player, side, mode);
        }
        setSyncFlag();
    }

    public int getProvidingWeakPower() {
        if (isSynced()) {
            IExternalControl te = getCore();
            if (this.state.equals("Emit if work scheduled")) {
                return te.isScheduled() ? 15 : 0;
            }
            if (this.state.equals("Emit if doing work")) {
                return te.isDoingWork() ? 15 : 0;
            }
            if (this.state.equals("Emit if idle")) {
                return te.isDoingWork() ? 0 : 15;
            }
        }
        return 0;
    }


    public IExternalControl getCore() {
        if (isSynced()) {
            int coreX = this.xCoord + getCoreBlockXCoord();
            int coreY = this.yCoord + getCoreBlockYCoord();
            int coreZ = this.zCoord + getCoreBlockZCoord();
            return (IExternalControl) UtilBuilder.safeGetTileEntity(this.worldObj, coreX, coreY, coreZ);
        }
        return null;
    }

    public int getSignal() {
        return getWorldObj().getStrongestIndirectPower(this.xCoord, this.yCoord, this.zCoord);
    }


    public String changeState() {
        for (int i = 0; i < stateSequence.length; i++) {
            if (stateSequence[i].equals(this.state)) {
                int j = (i == stateSequence.length - 1) ? 0 : (i + 1);
                changeState(stateSequence[j]);
                return stateSequence[j];
            }
        }
        changeState(stateSequence[0]);
        return stateSequence[0];
    }

    public void changeState(String state) {
        this.lastSignal = -1;
        for (String s : stateSequence) {
            if (s.equals(state)) this.state = state;
        }
        setInstantSyncFlag();
    }

    public String getState() {
        return this.state;
    }

    protected boolean syncCoreBlock(int x, int y, int z) {
        int coreX = this.xCoord + x;
        int coreY = this.yCoord + y;
        int coreZ = this.zCoord + z;
        TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
        if (getCoreWorld() != null &&

                te instanceof IExternalControl && (x != 0 || y != 0 || z != 0)) {

            this.synced = true;
            return true;
        }

        this.coreBlockX = this.coreBlockY = this.coreBlockZ = 0;
        this.synced = false;

        return false;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        syncCoreBlock();
        readCoordFromNBT(tagCompound);

        this.coreBlockX = tagCompound.getInteger("CoreBlockX");
        this.coreBlockY = tagCompound.getInteger("CoreBlockY");
        this.coreBlockZ = tagCompound.getInteger("CoreBlockZ");

        this.acceptCoordChanger = tagCompound.getBoolean("AcceptCoordChanger");
        this.interDimensional = tagCompound.getBoolean("InterDimensional");
        this.dimentionId = tagCompound.getInteger("CoreBlockDimentionId");

        this.state = tagCompound.getString("State");
        this.lastSignal = tagCompound.getInteger("LastSignal");
        this.lastPower = tagCompound.getInteger("LastPower");
    }


    public void writeToNBT(NBTTagCompound tagCompound) {
        syncCoreBlock();
        writeCoordToNBT(tagCompound);

        tagCompound.setInteger("CoreBlockX", this.coreBlockX);
        tagCompound.setInteger("CoreBlockY", this.coreBlockY);
        tagCompound.setInteger("CoreBlockZ", this.coreBlockZ);

        tagCompound.setBoolean("AcceptCoordChanger", this.acceptCoordChanger);
        tagCompound.setBoolean("InterDimensional", this.interDimensional);
        tagCompound.setInteger("CoreBlockDimentionId", this.dimentionId);

        tagCompound.setString("State", this.state);
        tagCompound.setInteger("LastSignal", this.lastSignal);
        tagCompound.setInteger("LastPower", this.lastPower);
    }

    protected boolean loop = false;

    public void setSyncFlag() {
        if (!this.worldObj.isRemote && !this.loop) {
            int coreX = this.xCoord + this.coreBlockX;
            int coreY = this.yCoord + this.coreBlockY;
            int coreZ = this.zCoord + this.coreBlockZ;
            if (getCoreWorld() != null) {
                TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
                if (te instanceof IGeneralInterface && te != this) {
                    this.loop = true;
                    ((IGeneralInterface) te).setSyncFlag();
                    this.loop = false;
                }
            }
        }
        super.setSyncFlag();
    }


    public void setInstantSyncFlag() {
        if (!this.worldObj.isRemote && !this.loop) {
            int coreX = this.xCoord + this.coreBlockX;
            int coreY = this.yCoord + this.coreBlockY;
            int coreZ = this.zCoord + this.coreBlockZ;
            if (getCoreWorld() != null) {
                TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
                if (te instanceof IGeneralInterface && te != this) {
                    this.loop = true;
                    ((IGeneralInterface) te).setInstantSyncFlag();
                    this.loop = false;
                }
            }
        }
        super.setInstantSyncFlag();
    }

    public boolean acceptCoordChanger = true;
    protected boolean interDimensional = false;
    protected int dimentionId = 0;

    public World getCoreWorld() {
        if (this.interDimensional) {
            return DimensionManager.getWorld(this.dimentionId);
        }
        return this.worldObj;
    }

    public boolean setCoreBlockDimension(int d) {
        this.dimentionId = d;
        this.interDimensional = true;
        if (this.worldObj != null && this.worldObj.provider.dimensionId == d) {
            this.interDimensional = false;
            return true;
        }
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (getCoreWorld() == null) {
                this.interDimensional = false;
                return false;
            }
            if (getCoreWorld() == this.worldObj) {
                this.interDimensional = false;
                return true;
            }
        }
        return true;
    }


    public boolean shouldRenderInPass(int pass) {
        return (pass == 1);
    }


    public boolean acceptCoordChanger() {
        return this.acceptCoordChanger;
    }
}
