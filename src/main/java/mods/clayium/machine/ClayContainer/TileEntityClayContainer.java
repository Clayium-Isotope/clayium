package mods.clayium.machine.ClayContainer;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TileEntityClayContainer extends TileGeneric implements ISidedInventory, ITickable {
    private boolean isLoaded;

    protected NonNullList<ItemStack> containerItemStacks;
    protected String customName;
    protected int clayEnergyStorageSize = 1;

    protected boolean isPipe = false;

    public Map<EnumFacing, Integer> importRoutes = new EnumMap<EnumFacing, Integer>(EnumFacing.class) {{
        put(EnumFacing.UP,    -1);
        put(EnumFacing.DOWN,  -1);
        put(EnumFacing.NORTH, -1);
        put(EnumFacing.SOUTH, -1);
        put(EnumFacing.WEST,  -1);
        put(EnumFacing.EAST,  -1);
    }};
    public List<int[]> listSlotsImport = new ArrayList<>();
    public Map<EnumFacing, Integer> exportRoutes = new EnumMap<EnumFacing, Integer>(EnumFacing.class) {{
        put(EnumFacing.UP,    -1);
        put(EnumFacing.DOWN,  -1);
        put(EnumFacing.NORTH, -1);
        put(EnumFacing.SOUTH, -1);
        put(EnumFacing.WEST,  -1);
        put(EnumFacing.EAST,  -1);
    }};
    public List<int[]> listSlotsExport = new ArrayList<>();
    public EnumFacing cePort;

    public boolean autoExtract = true;
    public boolean autoInsert = true;

    public int[] maxAutoInsert;
    public int maxAutoInsertDefault = 8;
    public int[] maxAutoExtract;
    public int maxAutoExtractDefault = 8;
    public int autoInsertInterval = 20;
    public int autoExtractInterval = 20;
    protected int autoInsertCount = 2;
    protected int autoExtractCount = 0;

    public int clayEnergySlot = -1;
    protected int tier;

    public int getTier() {
        return tier;
    }

    // TileEntityが作成されるとき、引数無しが適切なので、初期化の関数を分ける
    public void initByTier(int tier) {
        this.tier = tier;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public int getSizeInventory() {
        return this.containerItemStacks.size();
    }

    public void growCEStorageSize(int dist) {
        this.clayEnergyStorageSize += dist;
        if (this.clayEnergyStorageSize > 64) {
            this.clayEnergyStorageSize = 64;
        }
    }

    public int getClayEnergyStorageSize() {
        return this.clayEnergyStorageSize;
    }

    public ItemStack getStackInSlot(int index) {
        return this.containerItemStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.containerItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.containerItemStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.containerItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.containerItemStacks)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    public void clear() {
        this.containerItemStacks.clear();
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

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) == this)
            return player.getDistanceSq(this.pos.add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.containerItemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.containerItemStacks);

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        int[] temp;
        temp = compound.getIntArray("InsertRoutes");
        if (temp.length >= EnumFacing.VALUES.length) {
            for (int i = 0; i < EnumFacing.VALUES.length; i++) {
                this.importRoutes.replace(EnumFacing.VALUES[i], temp[i]);
            }
        }

        temp = compound.getIntArray("ExtractRoutes");
        if (temp.length >= EnumFacing.VALUES.length) {
            for (int i = 0; i < EnumFacing.VALUES.length; i++) {
                this.exportRoutes.replace(EnumFacing.VALUES[i], temp[i]);
            }
        }

        this.isPipe = compound.getBoolean("IsPipe");

        this.autoInsertCount = compound.getInteger("AutoInsertCount");
        this.autoExtractCount = compound.getInteger("AutoExtractCount");

        initByTier(compound.getInteger("Tier"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.containerItemStacks);

        if (hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        compound.setIntArray("InsertRoutes", this.importRoutes.values().stream().mapToInt(e -> e).toArray());
        compound.setIntArray("ExtractRoutes", this.exportRoutes.values().stream().mapToInt(e -> e).toArray());

        compound.setBoolean("IsPipe", this.isPipe);

        compound.setInteger("AutoInsertCount", this.autoInsertCount);
        compound.setInteger("AutoExtractCount", this.autoExtractCount);

        compound.setInteger("Tier", this.tier);

        return compound;
    }

    @Override
    public void onLoad() {
        this.isLoaded = false;
//        ClayContainer.ClayContainerState.checkSurroundConnection(this.world, this.pos, this);
//        this.world.addBlockEvent(this.pos, this.getBlockType(), 0, 0);
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    /*
     * TESR のために、TE と Blockstate / Block の上手い橋渡しについて、模索中。
     */
    public ClayContainer.ClayContainerState getBlockState() {
        if (this.world.getBlockState(this.pos) instanceof ClayContainer.ClayContainerState)
            return (ClayContainer.ClayContainerState) this.world.getBlockState(this.pos);
        return null;
    }

    public boolean isPipe() {
        return this.isPipe;
    }

    public void reverseIsPipe() {
        this.isPipe = !this.isPipe;
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (this.importRoutes.get(side) != -1) {
            return this.listSlotsImport.get(this.importRoutes.get(side));
        }

        return new int[0];
    }

    /**
     * Referred by {@link net.minecraft.tileentity.TileEntityHopper}#update()
     */
    @Override
    public void update() {
        // on Tick Loop
        if (!this.world.isRemote) {
            if (this.autoExtract) {
                this.autoExtractCount++;
                if (this.autoExtractCount >= this.autoExtractInterval) {
                    this.autoExtractCount = 0;
                    doAutoTakeIn();
                }
            } else {
                this.autoExtractCount = 0;
            }

            // 挙動を再現するために、なんとなく書いてます。
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);

            if (this.autoInsert) {
                this.autoInsertCount++;
                if (this.autoInsertCount >= this.autoInsertInterval) {
                    this.autoInsertCount = 0;
                    doAutoTakeOut();
                }
            } else {
                this.autoInsertCount = 0;
            }
        }

        if (!this.isLoaded) {
            this.isLoaded = true;
            updateEntity();
        }
    }

    @Override
    public void updateEntity() {
        ClayContainer.ClayContainerState.checkSurroundConnection(this.world, this.pos, this);

        super.updateEntity();
    }

    private void doAutoTakeIn() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            int route = this.importRoutes.get(facing);
            if (0 <= route && route < this.listSlotsImport.size()) {
                UtilTransfer.extract(this, this.listSlotsImport.get(route), facing, this.maxAutoExtractDefault);
            } else {
                this.importRoutes.replace(facing, -1);
            }
        }
    }

    private void doAutoTakeOut() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            int route = this.exportRoutes.get(facing);
            if (0 <= route && route < this.listSlotsExport.size()) {
                UtilTransfer.insert(this, this.listSlotsExport.get(route), facing, this.maxAutoInsertDefault);
            } else {
                this.exportRoutes.replace(facing, -1);
            }
        }
    }
}
