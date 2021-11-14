package mods.clayium.block.tile;

import java.util.ArrayList;

import mods.clayium.block.MetalChest;
import mods.clayium.item.CMaterial;
import mods.clayium.item.CMaterials;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;


public class TileMetalChest
        extends TileClayContainer
        implements INormalInventory {
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    protected int containerX = 13;
    protected int containerY = 8;
    protected int containerP = 8;

    protected int materialId = 0;
    public CMaterial material = CMaterials.ALUMINIUM;

    public static int maxContainerSize = 936;


    public TileMetalChest() {
        this.insertRoutes = new int[] {0, 0, 0, 0, 0, 0};
        this.extractRoutes = new int[] {0, 0, 0, 0, 0, 0};
        this.autoInsert = false;
        this.autoExtract = false;
        this.containerItemStacks = new ItemStack[maxContainerSize];
        this.slotsDrop = new int[maxContainerSize];
        for (int i = 0; i < maxContainerSize; i++) {
            this.slotsDrop[i] = i;
        }
    }

    public int getSizeInventory() {
        return this.containerX * this.containerY * this.containerP;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
        if (getMaterialId() == 0)
            setMaterialId(itemstack.getItemDamage());
    }

    public boolean shouldRefresh() {
        return false;
    }


    public boolean hasSpecialDrops() {
        return true;
    }


    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, Block block, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ItemStack itemstack = getNormalDrop(world, block, metadata, fortune);

        itemstack.setItemDamage(getMaterialId());
        ret.add(itemstack);
        return ret;
    }

    public int getMaterialId() {
        return this.materialId;
    }

    public void setMaterialId(int id) {
        CMaterial material = CMaterials.getMaterialFromId(id);
        if (material != null) {
            this.materialId = id;
            this.material = material;
            this.containerX = MetalChest.getContainerX(this.material);
            this.containerY = MetalChest.getContainerY(this.material);
            this.containerP = MetalChest.getContainerP(this.material);

            int slotNum = this.containerX * this.containerY * this.containerP;

            int[] slots = new int[slotNum];
            int[] slots2 = new int[slotNum];
            for (int i = 0; i < slots.length; i++) {
                slots[i] = i;
                slots2[i] = slots.length - i - 1;
            }
            this.listSlotsInsert.add(slots);
            this.listSlotsExtract.add(slots2);
        }
    }


    public void updateEntity() {
        super.updateEntity();

        this.prevLidAngle = this.lidAngle;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {

            double d1 = this.xCoord + 0.5D;
            double d2 = this.zCoord + 0.5D;

            this.worldObj.playSoundEffect(d1, this.yCoord + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0F) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0F)) {

            float f1 = this.lidAngle;
            float f = 0.1F;
            if (this.numPlayersUsing > 0) {

                this.lidAngle += f;
            } else {

                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2) {

                double d2 = this.xCoord + 0.5D;
                double d0 = this.zCoord + 0.5D;

                this.worldObj.playSoundEffect(d2, this.yCoord + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("MaterialId", getMaterialId());
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        setMaterialId(tagCompound.getInteger("MaterialId"));
        super.readFromNBT(tagCompound);
    }


    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
        if (p_145842_1_ == 1) {

            this.numPlayersUsing = p_145842_2_;
            return true;
        }


        return super.receiveClientEvent(p_145842_1_, p_145842_2_);
    }


    public void openInventory() {
        if (this.numPlayersUsing < 0) {
            this.numPlayersUsing = 0;
        }

        this.numPlayersUsing++;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, getBlockType());
    }


    public void closeInventory() {
        if (getBlockType() instanceof MetalChest) {

            this.numPlayersUsing--;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, getBlockType());
        }
    }

    public String getDefaultInventoryName() {
        return StatCollector.translateToLocal(MetalChest.getUnlocalizedMetalChestName(getBlockType(), this.material) + ".name");
    }

    public boolean canExtractItemUnsafe(int slot, ItemStack itemstack, int route) {
        return (route == 0);
    }

    public boolean canInsertItemUnsafe(int slot, ItemStack itemstack, int route) {
        return (route == 0);
    }


    public int getInventoryX() {
        return this.containerX;
    }


    public int getInventoryY() {
        return this.containerY;
    }


    public int getInventoryP() {
        return this.containerP;
    }


    public int getInventoryStart() {
        return 0;
    }


    public void overrideTo(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block overriddenBlock, int overriddenMeta, Class overriddenTileEntityClass, NBTTagCompound overriddenTileEntityTag) {
        super.overrideTo(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, overriddenBlock, overriddenMeta, overriddenTileEntityClass, overriddenTileEntityTag);

        NBTTagCompound tagCompound = overriddenTileEntityTag;

        if (tagCompound != null) {
            NBTTagList tagList = tagCompound.getTagList("Items", 10);
            this.containerItemStacks = new ItemStack[getSizeInventory()];

            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
                short byte0 = tagCompound1.getShort("Slot");
                if (byte0 >= 0 && byte0 < this.containerItemStacks.length) {
                    this.containerItemStacks[byte0] = ItemStack.loadItemStackFromNBT(tagCompound1);
                }
            }
        }
    }


    public void onOverridden(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (itemStack != null) {
            CMaterial material = CMaterials.getMaterialFromId(itemStack.getItemDamage());
            int max = MetalChest.getContainerX(material) * MetalChest.getContainerY(material) * MetalChest.getContainerP(material);
            for (int i = 0; i < Math.min(max, this.containerItemStacks.length); i++)
                this.containerItemStacks[i] = null;
        }
    }
}
