package mods.clayium.machine.ClayContainer;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.*;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TileEntityClayContainer extends TileGeneric implements IClayInventory, ITickable, IInterfaceCaptive {
    protected boolean isLoaded;

    protected NonNullList<ItemStack> containerItemStacks;
    protected String customName;
    protected int[] slotsDrop;

    /**
     * -2: energy
     * -1: none,
     * 0: white
     */
    private final Map<EnumSide, Integer> importRoutes = new EnumMap<EnumSide, Integer>(EnumSide.class) {{
        put(EnumSide.UP,    -1);
        put(EnumSide.DOWN,  -1);
        put(EnumSide.FRONT, -1);
        put(EnumSide.BACK,  -1);
        put(EnumSide.LEFT,  -1);
        put(EnumSide.RIGHT, -1);
    }};
    protected final List<int[]> listSlotsImport = new ArrayList<>();
    /**
     * -1: none,
     * 0: white
     */
    private final Map<EnumSide, Integer> exportRoutes = new EnumMap<EnumSide, Integer>(EnumSide.class) {{
        put(EnumSide.UP,    -1);
        put(EnumSide.DOWN,  -1);
        put(EnumSide.FRONT, -1);
        put(EnumSide.BACK,  -1);
        put(EnumSide.LEFT,  -1);
        put(EnumSide.RIGHT, -1);
    }};
    protected final List<int[]> listSlotsExport = new ArrayList<>();
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
        this.setDefaultTransportation(tier);
    }

    protected void setDefaultTransportation(int tier) {
        UtilTier.MachineTransport config = UtilTier.MachineTransport.getByTier(tier);
        if (config != null) {
            this.autoInsertInterval = config.autoInsertInterval;
            this.autoExtractInterval = config.autoExtractInterval;
            this.maxAutoInsertDefault = config.maxAutoInsertDefault;
            this.maxAutoExtractDefault = config.maxAutoExtractDefault;
        }
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public int getSizeInventory() {
        return this.getContainerItemStacks().size();
    }

    public ItemStack getStackInSlot(int index) {
        return this.getContainerItemStacks().get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.getContainerItemStacks(), index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.getContainerItemStacks(), index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.getContainerItemStacks().set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.getContainerItemStacks())
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

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

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.getWorld().getTileEntity(this.getPos()) == this)
            return player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        return false;
    }

    @Override
    public void addSpecialDrops(NonNullList<ItemStack> drops) {
        // I hope not to cause IndexOutOfBoundsException
        for (int i : this.slotsDrop) {
            drops.add(this.getContainerItemStacks().get(i));
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
            this.customName = compound.getString("CustomName");
        }

        loadIORoutes(compound);

        this.autoInsertCount = compound.getInteger("AutoInsertCount");
        this.autoExtractCount = compound.getInteger("AutoExtractCount");

        initParamsByTier(compound.getInteger("Tier"));

        if (compound.hasKey("Filters", Constants.NBT.TAG_LIST)) {
            UtilCollect.tagList2EnumMap(compound.getTagList("Filters", Constants.NBT.TAG_COMPOUND), this.filters);
        }
    }

    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        ItemStackHelper.saveAllItems(compound, this.getContainerItemStacks());

        if (hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        saveIORoutes(compound);

        compound.setInteger("AutoInsertCount", this.autoInsertCount);
        compound.setInteger("AutoExtractCount", this.autoExtractCount);

        compound.setInteger("Tier", this.tier);

        compound.setTag("Filters", UtilCollect.enumMap2TagList(this.filters));

        return compound;
    }

    protected void loadIORoutes(NBTTagCompound compound) {
        int[] temp;
        temp = compound.getIntArray("InsertRoutes");
        if (temp.length >= EnumSide.VALUES.length) {
            for (int i = 0; i < EnumSide.VALUES.length; i++) {
                this.setImportRoute(EnumSide.VALUES[i], temp[i]);
            }
        }

        temp = compound.getIntArray("ExtractRoutes");
        if (temp.length >= EnumSide.VALUES.length) {
            for (int i = 0; i < EnumSide.VALUES.length; i++) {
                this.setExportRoute(EnumSide.VALUES[i], temp[i]);
            }
        }
    }

    protected void saveIORoutes(NBTTagCompound compound) {
        compound.setIntArray("InsertRoutes", this.importRoutes.values().stream().mapToInt(e -> e).toArray());
        compound.setIntArray("ExtractRoutes", this.exportRoutes.values().stream().mapToInt(e -> e).toArray());
    }

    @Override
    public void onLoad() {
        this.isLoaded = false;
//        ClayContainer.ClayContainerState.checkSurroundConnection(this.getWorld(), this.getPos(), this);
//        this.getWorld().addBlockEvent(this.getPos(), this.getBlockType(), 0, 0);
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    /*
     * TESR のために、TE と Blockstate / Block の上手い橋渡しについて、模索中。
     */
    public BlockStateClayContainer getBlockState() {
        if (this.getWorld().getBlockState(this.getPos()) instanceof BlockStateClayContainer)
            return (BlockStateClayContainer) this.getWorld().getBlockState(this.getPos());
        return null;
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

    /**
     * Referred by {@link net.minecraft.tileentity.TileEntityHopper}#update()
     */
    @Override
    public void update() {
        if (!this.getWorld().isRemote) return;

        // on Tick Loop
        this.doTransfer();

        if (!this.isLoaded) {
            this.isLoaded = true;
            updateEntity();
        }
    }

    public void doTransfer() {
        if (!this.getWorld().isRemote) {
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
            this.getWorld().notifyBlockUpdate(this.getPos(), this.getWorld().getBlockState(this.getPos()), this.getWorld().getBlockState(this.getPos()), 3);

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
    }

    @Override
    public void updateEntity() {
        BlockStateClayContainer.checkSurroundConnection(this.getWorld(), this.getPos(), this);

        super.updateEntity();
    }

    protected void doAutoTakeIn() {
        int transferred = this.maxAutoExtractDefault;
        int route;
        for (EnumSide facing : EnumSide.VALUES) {
            route = this.getImportRoute(facing);
            if (route != -2 && (0 > route || route >= this.getListSlotsImport().size())) {
//                if (route != -1)
//                    this.setImportRoute(facing, -1);
                continue;
            }

            if (this instanceof IClayEnergyConsumer && route == -2) {
                transferred = UtilTransfer.extract(this, new int[] { ((IClayEnergyConsumer) this).getEnergySlot() }, UtilDirection.getSideOfDirection(this.getFront(), facing), ((IClayEnergyConsumer) this).getClayEnergyStorageSize() - ((IClayEnergyConsumer) this).getEnergyStack().getCount());
            } else {
                transferred = UtilTransfer.extract(this, this.getListSlotsImport().get(route), UtilDirection.getSideOfDirection(this.getFront(), facing), transferred);
            }

            if (transferred == 0) {
                return;
            }
        }
    }

    protected void doAutoTakeOut() {
        for (EnumSide facing : EnumSide.VALUES) {
            int route = this.getExportRoute(facing);
            if (0 <= route && route < this.getListSlotsExport().size()) {
                UtilTransfer.insert(this, this.getListSlotsExport().get(route), UtilDirection.getSideOfDirection(this.getFront(), facing), this.maxAutoInsertDefault);
//            } else {
//                this.setExportRoute(facing, -1);
            }
        }
    }

    @Override
    public List<int[]> getListSlotsImport() {
        return this.listSlotsImport;
    }

    @Override
    public List<int[]> getListSlotsExport() {
        return this.listSlotsExport;
    }

    @Override
    public EnumFacing getFront() {
        return this.getWorld().getBlockState(this.getPos()).getValue(BlockStateClayContainer.FACING);
    }

    @Override
    public int getImportRoute(EnumSide side) {
        return this.importRoutes.get(side);
    }

    @Override
    public int getExportRoute(EnumSide side) {
        return this.exportRoutes.get(side);
    }

    @Override
    public void setImportRoute(EnumSide side, int route) {
        this.importRoutes.replace(side, route);
    }

    @Override
    public void setExportRoute(EnumSide side, int route) {
        this.exportRoutes.replace(side, route);
    }

    @Override
    public Map<EnumFacing, ItemStack> getFilters() {
        return this.filters;
    }

    @Override
    public NonNullList<ItemStack> getContainerItemStacks() {
        return this.containerItemStacks;
    }

    public int getGuiId() {
        return ((ClayContainer) this.getBlockType()).guiId;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return IClayInventory.isItemValidForSlot(this, index, stack);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return IClayInventory.canInsertItem(this, index, itemStackIn, direction);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return IClayInventory.canExtractItem(this, index, itemStackIn, direction);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return IClayInventory.getSlotsForFace(this, side);
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
    private final List<ResourceLocation> InsertIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public List<ResourceLocation> getInsertIcons() {
        return InsertIcons;
    }

    @SideOnly(Side.CLIENT)
    private final List<ResourceLocation> ExtractIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public List<ResourceLocation> getExtractIcons() {
        return ExtractIcons;
    }

    @SideOnly(Side.CLIENT)
    private final List<ResourceLocation> InsertPipeIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public List<ResourceLocation> getInsertPipeIcons() {
        return InsertPipeIcons;
    }

    @SideOnly(Side.CLIENT)
    private final List<ResourceLocation> ExtractPipeIcons = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    public List<ResourceLocation> getExtractPipeIcons() {
        return ExtractPipeIcons;
    }
}
