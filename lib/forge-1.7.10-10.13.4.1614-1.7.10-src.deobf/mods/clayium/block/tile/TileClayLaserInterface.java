package mods.clayium.block.tile;

import mods.clayium.block.laser.ClayLaser;
import mods.clayium.block.laser.IClayLaserMachine;
import mods.clayium.item.CItems;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
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

public class TileClayLaserInterface extends TileClayContainerTiered implements IClayLaserMachine, ISynchronizedInterface {
    protected int coreBlockX = 0;
    protected int coreBlockY = 0;
    protected int coreBlockZ = 0;
    protected boolean synced = false;

    public void initParams() {
        super.initParams();
        this.containerItemStacks = new ItemStack[1];

        this.insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.slotsDrop = new int[] {0};
        this.autoInsert = false;
        this.autoExtract = false;
    }

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

    protected boolean syncCoreBlock(int x, int y, int z) {
        int coreX = this.xCoord + x;
        int coreY = this.yCoord + y;
        int coreZ = this.zCoord + z;
        TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
        if (getCoreWorld() != null &&

                te instanceof IClayLaserMachine && (x != 0 || y != 0 || z != 0)) {

            this.synced = true;
            return true;
        }

        this.coreBlockX = this.coreBlockY = this.coreBlockZ = 0;
        this.synced = false;

        return false;
    }


    public void updateEntity() {
        syncCoreBlock();
        super.updateEntity();
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
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        if (itemStack == null)
            return -1;
        if (UtilItemStack.areItemDamageEqual(itemStack, CItems.itemMisc.get("SynchronousParts"))) {
            return 91;
        }
        return super.getItemUseMode(itemStack, player);
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
                player.addChatMessage((IChatComponent) new ChatComponentText("Set Core block Coord (" + cx + "," + cy + "," + cz + ") in dim " + cd));
            }
        } else if (syncCoreBlock()) {
            super.useItemFromSide(itemStack, player, side, mode);
        }
        setSyncFlag();
    }

    public boolean consumeClayEnergy(long energy) {
        return false;
    }

    public boolean produceClayEnergy() {
        return false;
    }


    public void openInventory() {}


    public void closeInventory() {}


    public void initParamsByTier(int tier) {}


    public boolean irradiateClayLaser(ClayLaser laser, UtilDirection direction) {
        if (syncCoreBlock()) {
            IClayLaserMachine te = (IClayLaserMachine) UtilBuilder.safeGetTileEntity((IBlockAccess) getWorldObj(), this.xCoord + this.coreBlockX, this.yCoord + this.coreBlockY, this.zCoord + this.coreBlockZ);
            return te.irradiateClayLaser(laser, direction);
        }
        return false;
    }


    protected boolean loop = false;

    public void setSyncFlag() {
        if (!this.worldObj.isRemote && !this.loop) {
            int coreX = this.xCoord + this.coreBlockX;
            int coreY = this.yCoord + this.coreBlockY;
            int coreZ = this.zCoord + this.coreBlockZ;
            if (getCoreWorld() != null) {
                TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) getCoreWorld(), coreX, coreY, coreZ);
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
                TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) getCoreWorld(), coreX, coreY, coreZ);
                if (te instanceof IGeneralInterface && te != this) {
                    this.loop = true;
                    ((IGeneralInterface) te).setInstantSyncFlag();
                    this.loop = false;
                }
            }
        }
        super.setInstantSyncFlag();
    }

    public boolean acceptCoordChanger = false;
    protected boolean interDimensional = false;
    protected int dimentionId = 0;

    public World getCoreWorld() {
        if (this.interDimensional) {
            return (World) DimensionManager.getWorld(this.dimentionId);
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
