package mods.clayium.machine.ClayContainer;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.item.filter.IFilter;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTier;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collector;

public class TileEntityClayContainer extends TileGeneric implements ISidedInventory, ITickable {
    private boolean isLoaded;

    protected NonNullList<ItemStack> containerItemStacks;
    protected String customName;
    protected int clayEnergyStorageSize = 1;
    protected int[] slotsDrop;

    protected boolean isPipe = false;

    /**
     * -2: energy
     * -1: none,
     * 0: white
     */
    public Map<EnumFacing, Integer> importRoutes = new EnumMap<EnumFacing, Integer>(EnumFacing.class) {{
        put(EnumFacing.UP,    -1);
        put(EnumFacing.DOWN,  -1);
        put(EnumFacing.NORTH, -1);
        put(EnumFacing.SOUTH, -1);
        put(EnumFacing.WEST,  -1);
        put(EnumFacing.EAST,  -1);
    }};
    public List<int[]> listSlotsImport = new ArrayList<>();
    /**
     * -1: none,
     * 0: white
     */
    public Map<EnumFacing, Integer> exportRoutes = new EnumMap<EnumFacing, Integer>(EnumFacing.class) {{
        put(EnumFacing.UP,    -1);
        put(EnumFacing.DOWN,  -1);
        put(EnumFacing.NORTH, -1);
        put(EnumFacing.SOUTH, -1);
        put(EnumFacing.WEST,  -1);
        put(EnumFacing.EAST,  -1);
    }};
    public List<int[]> listSlotsExport = new ArrayList<>();
    public EnumMap<EnumFacing, ItemStack> filters = new EnumMap<EnumFacing, ItemStack>(EnumFacing.class) {{
        put(EnumFacing.UP,    ItemStack.EMPTY);
        put(EnumFacing.DOWN,  ItemStack.EMPTY);
        put(EnumFacing.NORTH, ItemStack.EMPTY);
        put(EnumFacing.SOUTH, ItemStack.EMPTY);
        put(EnumFacing.WEST,  ItemStack.EMPTY);
        put(EnumFacing.EAST,  ItemStack.EMPTY);
    }};

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

    // be careful with ArrayIndexOutOfBoundsException
    public int clayEnergySlot = -1;
    protected int tier = -1;

    public TileEntityClayContainer() {
        initParams();
        registerIOIcons();
    }

    public void initParams() {}

    public int getTier() {
        return tier;
    }

    // TileEntityが作成されるとき、引数無しが適切なので、初期化の関数を分ける
    public void initParamsByTier(int tier) {
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

        this.markDirty();
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
    public void addSpecialDrops(NonNullList<ItemStack> drops) {
        // I hope not to cause IndexOutOfBoundsException
        for (int i : this.slotsDrop) {
            drops.add(this.containerItemStacks.get(i));
        }
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

        initParamsByTier(compound.getInteger("Tier"));

        if (compound.hasKey("Filters", 9)) {
            UtilItemStack.tagList2EnumMap(compound.getTagList("Filters", 10), this.filters);
        }
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

        compound.setTag("Filters", UtilItemStack.enumMap2TagList(this.filters));

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

    public boolean acceptEnergyClay() {
        return true;
    }

    public static boolean hasClayEnergy(ItemStack itemstack) {
        return getClayEnergy(itemstack) > 0L;
    }

    public static long getClayEnergy(ItemStack itemstack) {
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof IClayEnergy) {
            return ((IClayEnergy) itemstack.getItem()).getClayEnergy();
        }

        return 0L;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == this.clayEnergySlot) {
            return acceptEnergyClay() && hasClayEnergy(stack)
                    && (this.containerItemStacks.get(this.clayEnergySlot).isEmpty()
                        || this.containerItemStacks.get(this.clayEnergySlot).getCount() < this.clayEnergyStorageSize);
        }

        for (int[] slots : this.listSlotsImport) {
            if (Arrays.stream(slots).anyMatch(e -> e == index))
                return true;
        }
        return false;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        int route = this.importRoutes.get(direction);
        if (route == -2 && index == this.clayEnergySlot) return isItemValidForSlot(index, itemStackIn);

        if (route >= 0 && route < this.listSlotsImport.size()) {
            return Arrays.stream(this.listSlotsImport.get(route)).anyMatch(e -> e == index);
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        int route = this.exportRoutes.get(direction);
        if (route >= 0 && route < this.listSlotsExport.size()) {
            return Arrays.stream(this.listSlotsExport.get(route)).anyMatch(e -> e == index);
        }

        return false;
    }

    protected boolean checkBlocked(ItemStack itemStackIn, EnumFacing direction) {
        return IFilter.isFilter(this.filters.get(direction)) && !IFilter.match(this.filters.get(direction), itemStackIn);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        Boolean[] flags = new Boolean[this.containerItemStacks.size()];
        switch (this.importRoutes.get(side)) {
            case -2:
                flags[this.clayEnergySlot] = true;
                break;
            case -1:
                break;
            default:
                for (int slot : this.listSlotsImport.get(this.importRoutes.get(side))) {
                    flags[slot] = true;
                }
        }

        if (this.exportRoutes.get(side) != -1) {
            for (int slot : this.listSlotsExport.get(this.exportRoutes.get(side))) {
                flags[slot] = true;
            }
        }

        Collector<Boolean, ArrayList<Integer>, int[]> verifiedIndexJoiner = Collector.of(
                ArrayList::new,
                (list, flag) -> list.add(flag ? list.size() : -1),
                (list, list1) -> { list.addAll(list1); return list; },
                list -> list.stream().mapToInt(e -> e).filter(e -> e != -1).toArray()
        );

        return Arrays.stream(flags).collect(verifiedIndexJoiner);
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

    protected void doAutoTakeIn() {
        int transferred = this.maxAutoExtractDefault;
        int route;
        for (EnumFacing facing : EnumFacing.VALUES) {
            route = this.importRoutes.get(facing);
            if (route != -2 && (0 > route || route >= this.listSlotsImport.size())) {
                if (route != -1)
                    this.importRoutes.replace(facing, -1);
                continue;
            }

            if (route == -2) {
                transferred = UtilTransfer.extract(this, new int[] { this.clayEnergySlot }, facing, clayEnergyStorageSize - this.containerItemStacks.get(this.clayEnergySlot).getCount());
            } else {
                transferred = UtilTransfer.extract(this, this.listSlotsImport.get(route), facing, transferred);
            }

            if (transferred == 0) {
                return;
            }
        }
    }

    protected void doAutoTakeOut() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            int route = this.exportRoutes.get(facing);
            if (0 <= route && route < this.listSlotsExport.size()) {
                UtilTransfer.insert(this, this.listSlotsExport.get(route), facing, this.maxAutoInsertDefault);
            } else {
                this.exportRoutes.replace(facing, -1);
            }
        }
    }

    public boolean relyOnClayEnergy() {
        return !UtilTier.canManufactureCraft(this.tier) && this.clayEnergySlot != -1;
    }

    /* ========== Texture Part ========== */

    @SideOnly(Side.CLIENT)
    @Nullable
    public ResourceLocation getFaceResource() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void registerInsertIcons(String... iconstrs) {
        if (iconstrs != null) {
            this.InsertIcons.clear();
            this.InsertPipeIcons.clear();

            for (String iconStr : iconstrs) {
                this.InsertIcons.add(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/" + iconStr + ".png"));
                this.InsertPipeIcons.add(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/" + iconStr + "_p.png"));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerExtractIcons(String... iconstrs) {
        if (iconstrs != null) {
            this.ExtractIcons.clear();
            this.ExtractPipeIcons.clear();

            for (String iconStr : iconstrs) {
                this.ExtractIcons.add(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/" + iconStr + ".png"));
                this.ExtractPipeIcons.add(new ResourceLocation(ClayiumCore.ModId, "textures/blocks/io/" + iconStr + "_p.png"));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {}

    @SideOnly(Side.CLIENT)
    public final List<ResourceLocation> InsertIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public final List<ResourceLocation> ExtractIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public final List<ResourceLocation> InsertPipeIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public final List<ResourceLocation> ExtractPipeIcons = new ArrayList<>();
}
