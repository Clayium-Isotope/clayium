package mods.clayium.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TileEntityGeneric extends TileEntity implements IInventory {
    protected static final Random random = new Random();
    protected NonNullList<ItemStack> containerItemStacks = NonNullList.withSize(0, ItemStack.EMPTY);
    private String customName;
    protected int[] slotsDrop;

    public TileEntityGeneric() {}

    protected void initParams() {}

    // TileEntityが作成されるとき、引数無しが適切なので、初期化の関数を分ける
    public void initParamsByTier(int tier) {}

    public boolean hasSpecialDrops() {
        return false;
    }

    public final void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ItemStack stack = getNormalDrop(world.getBlockState(pos).getBlock(), fortune);
        drops.add(stack);
        addSpecialDrops(drops);
    }

    public static ItemStack getNormalDrop(Block blockIn, int fortune) {
        return new ItemStack(blockIn.getItemDropped(blockIn.getDefaultState(), random, fortune));
    }

    public void addSpecialDrops(NonNullList<ItemStack> drops) {
        // I hope not to cause IndexOutOfBoundsException
        for (int i : this.slotsDrop) {
            drops.add(this.getContainerItemStacks().get(i));
        }
    }

    public void markDirty() {
        if (!this.getWorld().isRemote) {
//            world.markBlockRangeForRenderUpdate(pos, pos);
            this.getWorld().notifyBlockUpdate(this.getPos(), this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 3);
        }
    }

    @Override
    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.readMoreFromNBT(compound);
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        return this.writeMoreToNBT(compound);
    }

    public void readMoreFromNBT(NBTTagCompound compound) {
        this.containerItemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.getContainerItemStacks());

        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING)) {
            this.setCustomName(compound.getString("CustomName"));
        }
    }

    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        ItemStackHelper.saveAllItems(compound, this.getContainerItemStacks());

        if (hasCustomName()) {
            compound.setString("CustomName", this.getName());
        }

        return compound;
    }

    public NonNullList<ItemStack> getContainerItemStacks() {
        return this.containerItemStacks;
    }

    @Override
    public int getSizeInventory() {
        return this.getContainerItemStacks().size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.getContainerItemStacks())
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.getContainerItemStacks().get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.getContainerItemStacks(), index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.getContainerItemStacks(), index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.getContainerItemStacks().set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.getWorld().getTileEntity(this.getPos()) == this)
            return player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.getContainerItemStacks().clear();
    }

    public String getName() {
        return hasCustomName() ? this.customName : getBlockType().getLocalizedName();
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName());
    }
}
