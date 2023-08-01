package mods.clayium.machine.ClayContainer;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TileEntityClayContainer extends TileEntityGeneric implements IClayInventory, ITickable, IInterfaceCaptive {
    protected boolean isLoaded;

    private final Map<EnumSide, Integer> importRoutes = UtilCollect.enumMapWithFill(EnumSide.VALUES, -1);
    protected final List<int[]> listSlotsImport = new ArrayList<>();

    private final Map<EnumSide, Integer> exportRoutes = UtilCollect.enumMapWithFill(EnumSide.VALUES, -1);
    protected final List<int[]> listSlotsExport = new ArrayList<>();

    public EnumMap<EnumFacing, ItemStack> filters = UtilCollect.enumMapWithFill(EnumFacing.VALUES, ItemStack.EMPTY);

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

    protected TierPrefix tier = TierPrefix.none;

    public TileEntityClayContainer() {
        super();
        initParams();
        registerIOIcons();
    }

    @Override
    public void initParams() {}

    public TierPrefix getRecipeTier() {
        return this.tier;
    }

    // TileEntityが作成されるとき、引数無しが適切なので、初期化の関数を分ける
    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;
        this.setDefaultTransportation(tier);
    }

    protected void setDefaultTransportation(TierPrefix tier) {
        UtilTier.MachineTransport config = UtilTier.MachineTransport.getByTier(tier);
        if (config != null) {
            this.autoInsertInterval = config.autoInsertInterval;
            this.autoExtractInterval = config.autoExtractInterval;
            this.maxAutoInsertDefault = config.maxAutoInsertDefault;
            this.maxAutoExtractDefault = config.maxAutoExtractDefault;
        }
    }

    @Override
    public boolean acceptInterfaceSync() {
        return true;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        super.readMoreFromNBT(compound);

        loadIORoutes(compound);

        this.autoInsertCount = compound.getInteger("AutoInsertCount");
        this.autoExtractCount = compound.getInteger("AutoExtractCount");

        initParamsByTier(TierPrefix.get(compound.getInteger("Tier")));

        if (compound.hasKey("Filters", Constants.NBT.TAG_LIST)) {
            UtilCollect.tagList2EnumMap(compound.getTagList("Filters", Constants.NBT.TAG_COMPOUND), this.filters);
        }
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        super.writeMoreToNBT(compound);

        saveIORoutes(compound);

        compound.setInteger("AutoInsertCount", this.autoInsertCount);
        compound.setInteger("AutoExtractCount", this.autoExtractCount);

        compound.setInteger("Tier", this.tier.meta());

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

    /**
     * Referred by {@link net.minecraft.tileentity.TileEntityHopper}#update()
     */
    @Override
    public void update() {
        if (this.getWorld().isRemote) return;

        // on Tick Loop
        this.doTransfer();

        if (!this.isLoaded) {
            this.isLoaded = true;
            this.markDirty();
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
    public void markDirty() {
        BlockStateClayContainer.checkSurroundConnection(this.getWorld(), this.getPos(), this);

        super.markDirty();
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
    public boolean getAutoInsert() {
        return this.autoInsert;
    }

    @Override
    public boolean getAutoExtract() {
        return this.autoExtract;
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

    public TierPrefix getHullTier() {
        return this.getRecipeTier();
    }

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
