package mods.clayium.block.tile;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import mods.clayium.block.ClayContainer;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public abstract class TileGeneric
        extends TileEntity
        implements IGeneralInterface {
    protected boolean syncFlag;
    protected boolean instantSyncFlag;
    protected boolean strongSyncMode = false;
    protected boolean weakSyncMode = false;
    protected boolean renderSyncFlag = false;
    protected boolean strongReRenderFlag = false;
    protected boolean weakReRenderFlag = false;
    protected boolean removeFlag = false;
    private int syncTiming = -1;
    protected static Random random = new Random();


    public void updateEntity() {
        if (this.removeFlag) {
            getWorldObj().removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            return;
        }
        if (this.syncFlag) {
            markDirtyWithoutSync();
            if (this.syncTiming == -1) {
                this.syncTiming = random.nextInt(ClayiumCore.cfgPacketSendingRate);
            }
            if (this.worldObj.getTotalWorldTime() % ClayiumCore.cfgPacketSendingRate == this.syncTiming || (this.instantSyncFlag && ClayiumCore.cfgEnableInstantSync)) {
                this.strongSyncMode = true;
                markForStrongUpdate();

                this.syncFlag = false;
                this.instantSyncFlag = false;
            }
        }
        if (this.worldObj.isRemote && (this.strongReRenderFlag || (this.weakReRenderFlag && this.worldObj.getTotalWorldTime() % ClayiumCore.cfgRenderingRate == 0L))) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.weakReRenderFlag = this.strongReRenderFlag = false;
        }
    }

    public boolean isUsable(ItemStack itemStack, EntityPlayer player, int direction, float hitX, float hitY, float hitZ) {
        return (getItemUseMode(itemStack, player) != -1);
    }

    public void useItem(ItemStack itemStack, EntityPlayer player, int direction, float hitX, float hitY, float hitZ) {
        if (!this.worldObj.isRemote) {
            int side = UtilDirection.direction2Side(getFrontDirection(), direction) - 6;
            useItemFromSide(itemStack, player, side, getItemUseMode(itemStack, player));
        }
        setInstantSyncFlag();
        for (ForgeDirection direction1 : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tile = UtilDirection.getTileEntity((IBlockAccess) this.worldObj, this.xCoord, this.yCoord, this.zCoord, direction1);
            if (tile instanceof IGeneralInterface) {
                ((IGeneralInterface) tile).setInstantSyncFlag();
            }
        }
    }


    public void setRemoveFlag() {
        this.removeFlag = true;
    }

    public boolean getRemoveFlag() {
        return this.removeFlag;
    }


    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
        return ((world.isRemote || shouldRefresh() || newBlock.hasTileEntity(newMeta)) && oldBlock != newBlock);
    }

    public boolean shouldRefresh() {
        return true;
    }


    public boolean hasSpecialDrops() {
        return false;
    }


    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, Block block, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ItemStack itemstack = getNormalDrop(world, block, metadata, fortune);
        writeTileEntityTagToItemStack(itemstack, this);
        ret.add(itemstack);
        return ret;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        readTileEntityTagFromItemStack(itemstack, this, x, y, z);
    }

    public static ItemStack getNormalDrop(World world, Block block, int metadata, int fortune) {
        Item item = block.getItemDropped(metadata, world.rand, fortune);
        return new ItemStack(item, 1, block.damageDropped(metadata));
    }

    public static void writeTileEntityTagToItemStack(ItemStack item, TileEntity tile) {
        if (item != null && tile != null) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound tetag = new NBTTagCompound();
            tile.writeToNBT(tetag);
            tetag.removeTag("x");
            tetag.removeTag("y");
            tetag.removeTag("z");
            tag.setTag("TileEntityNBTTag", (NBTBase) tetag);
            item.setTagCompound(tag);
        }
    }

    public static void readTileEntityTagFromItemStack(ItemStack item, TileEntity tile, int x, int y, int z) {
        NBTTagCompound tetag = getTileEntityTag(item);
        if (tetag != null) {
            tetag.setInteger("x", x);
            tetag.setInteger("y", y);
            tetag.setInteger("z", z);
            tile.readFromNBT(tetag);
        }
    }

    public static NBTTagCompound getTileEntityTag(ItemStack item) {
        if (item != null && item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("TileEntityNBTTag")) {
                return (NBTTagCompound) tag.getTag("TileEntityNBTTag");
            }
        }
        return null;
    }

    private int oldMetadata = -1;
    private int oldFrontDirection = -1;

    public int getFrontDirection() {
        if (this.worldObj == null) {
            return -1;
        }
        int newMetadata = getBlockMetadata();
        if (this.oldMetadata != newMetadata || this.oldFrontDirection == -1) {
            this.oldMetadata = newMetadata;
            this.oldFrontDirection = refreshFrontDirection();
        }
        return this.oldFrontDirection;
    }

    public int refreshFrontDirection() {
        Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        if (block instanceof ClayContainer) {
            return ((ClayContainer) block).getFront(this);
        }
        return getBlockMetadata();
    }

    public boolean canGetFrontDirection() {
        return (this.worldObj != null && this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) instanceof ClayContainer);
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        return -1;
    }


    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, int side, int mode) {}


    protected NBTTagCompound oldSendedTag = null;


    public Packet getDescriptionPacket() {
        boolean fullTag = (this.strongSyncMode || !this.weakSyncMode);
        this.strongSyncMode = false;
        this.weakSyncMode = false;

        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);


        if (fullTag) {
            this.oldSendedTag = (NBTTagCompound) nbtTagCompound.copy();
            nbtTagCompound.setBoolean("__FullTag", true);
            if (this.renderSyncFlag) {
                nbtTagCompound.setBoolean("__ReRender", true);
                this.renderSyncFlag = false;
            }
            return (Packet) new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
        }


        NBTTagCompound tagToSend = new NBTTagCompound();
        NBTTagList deletedList = null;
        if (this.oldSendedTag == null) {
            tagToSend = nbtTagCompound;
        } else {
            Set keys = this.oldSendedTag.func_150296_c();
            for (Object o : keys) {
                if (o instanceof String) {
                    String key = (String) o;
                    NBTBase oldTag = this.oldSendedTag.getTag(key);
                    if (nbtTagCompound.hasKey(key)) {
                        NBTBase newTag = nbtTagCompound.getTag(key);
                        if (!newTag.equals(oldTag))
                            tagToSend.setTag(key, newTag);
                        continue;
                    }
                    if (deletedList == null)
                        deletedList = new NBTTagList();
                    deletedList.appendTag((NBTBase) new NBTTagString(key));
                }
            }


            if (deletedList != null) {
                tagToSend.setTag("__DeletedTags", (NBTBase) deletedList);
            }
        }
        this.oldSendedTag = (NBTTagCompound) nbtTagCompound.copy();

        if (this.renderSyncFlag) {
            tagToSend.setBoolean("__ReRender", true);
            this.renderSyncFlag = false;
        }
        return (Packet) new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagToSend);
    }

    protected NBTTagCompound oldReceivedTag = null;

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        markDirty();

        NBTTagCompound tagReceived = pkt.func_148857_g();

        if (tagReceived.hasKey("__ReRender")) {
            tagReceived.removeTag("__ReRender");
            this.strongReRenderFlag = true;
        }

        if (tagReceived.hasKey("__FullTag")) {
            tagReceived.removeTag("__FullTag");
            this.oldReceivedTag = (NBTTagCompound) tagReceived.copy();
            readFromNBT(tagReceived);
            this.weakReRenderFlag = true;

            return;
        }
        NBTTagCompound tagToRead = new NBTTagCompound();

        if (this.oldReceivedTag == null) {
            tagToRead = tagReceived;

            return;
        }
        writeToNBT(tagToRead);

        if (tagReceived.hasKey("__DeletedTags")) {
            NBTTagList deletedList = tagReceived.getTagList("__DeletedTags", 8);
            for (int i = 0; i < deletedList.tagCount(); i++) {
                tagToRead.removeTag(deletedList.getStringTagAt(i));
            }
        }

        Set keys = tagReceived.func_150296_c();
        for (Object o : keys) {
            if (o instanceof String) {
                String key = (String) o;
                NBTBase newTag = tagReceived.getTag(key);
                tagToRead.setTag(key, newTag);
            }
        }


        this.oldReceivedTag = (NBTTagCompound) tagToRead.copy();
        readFromNBT(tagToRead);
    }


    public void markDirty() {
        markDirtyWithoutSync();
        setSyncFlag();
    }


    public void markDirtyWithoutSync() {
        super.markDirty();
    }


    public void markForStrongUpdate() {
        if (this.worldObj != null && !this.worldObj.isRemote) {

            this.strongSyncMode = true;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }


    public void markForWeakUpdate() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            this.weakSyncMode = true;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void setSyncFlag() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            this.syncFlag = true;
        }
    }


    public void setInstantSyncFlag() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            this.syncFlag = true;
            this.instantSyncFlag = true;
            setRenderSyncFlag();
        }
    }

    public void setRenderSyncFlag() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            this.renderSyncFlag = true;
        }
    }

    public void readCoordFromNBT(NBTTagCompound tagCompound) {
        readFromNBT(tagCompound);
    }

    public void writeCoordToNBT(NBTTagCompound tagCompound) {
        writeToNBT(tagCompound);
    }

    public void pushButton(EntityPlayer player, int action) {}
}
