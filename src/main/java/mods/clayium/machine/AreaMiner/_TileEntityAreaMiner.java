package mods.clayium.machine.AreaMiner;

import mods.clayium.component.construction.PosIterator;
import mods.clayium.component.construction.UtilConstructionBot;
import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserMachine;
import mods.clayium.machine.ClayMarker.AABBHolder;
import mods.clayium.machine.ClayMarker.IAABBProvider;
import mods.clayium.machine.ClayMarker.TileEntityClayMarker;
import mods.clayium.machine.Interface.IExternalControl;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.IntStream;

public class _TileEntityAreaMiner extends TileEntityClayContainer implements IClayLaserMachine, AABBHolder, IExternalControl, IClayEnergyConsumer, IButtonProvider {
    protected AxisAlignedBB aabb = NULL_AABB;
    public Appearance boxState = Appearance._0;
    protected PosIterator walker;
    protected final LateInit<Integer> inventoryX = new LateInit<>();
    protected final LateInit<Integer> inventoryY = new LateInit<>();
    public static final int filterHarvestSlot = 37;
    public static final int filterFortuneSlot = 38;
    public static final int filterSilktouchSlot = 39;
    public long laserEnergy = 0L;
    public EnumAreaMinerState state = EnumAreaMinerState.BEFORE_INIT;
    public long progress = 0L;
    public static final long energyPerTick = 1000L;
    public static final int progressPerTick = 100;
    public static final int progressPerJob = 400;
    public static final int maxJobCount = 1000;
    public static final int maxJobCountInLoop = 10;
    public boolean replaceMode = false;
    public boolean areaMode = false;
    public boolean placeFlag = false;
    protected final ContainClayEnergy ce = new ContainClayEnergy();

    public void initParams() {
        super.initParams();
        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, 0, 0);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(40, ItemStack.EMPTY);
    }

    @Override
    public int getClayEnergyStorageSize() {
        return 1;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {

    }

    @Override
    public int getEnergySlot() {
        return 36;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);
        int slotNum;
        if (tier.compareTo(TierPrefix.antimatter) >= 0) {
            this.inventoryX.set(4);
            this.inventoryY.set(2);
            slotNum = 8;
        } else {
            this.inventoryX.set(3);
            this.inventoryY.set(3);
            slotNum = 9;
        }

        switch (tier) {
            case precision:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
                this.autoExtractInterval = this.autoInsertInterval = 2;
                this.areaMode = false;
                break;
            case claySteel:
            case clayium:
            case ultimate:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.areaMode = true;
                break;
            case antimatter:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.areaMode = true;
                this.replaceMode = true;
        }

        this.slotsDrop = IntStream.concat(IntStream.range(0, slotNum), IntStream.of(this.getEnergySlot())).toArray();

        this.listSlotsImport.add(new int[]{this.getEnergySlot()});
        this.listSlotsExport.add(IntStream.range(0, slotNum).toArray());

        if (!this.areaMode) {
            this.listSlotsImport.remove(0);

            for (EnumSide side : EnumSide.VALUES) {
                if (this.getImportRoute(side) == 0) {
                    this.setImportRoute(side, NONE_ROUTE);
                }
            }
        }

        if (this.replaceMode) {
            this.listSlotsImport.add(IntStream.range(slotNum, slotNum * 2).toArray());
            this.slotsDrop = IntStream.concat(IntStream.range(0, slotNum * 2), IntStream.of(this.getEnergySlot())).toArray();
        } else {
            for (EnumSide side : EnumSide.VALUES) {
                if (this.getImportRoute(side) == 1) {
                    this.setImportRoute(side, 0);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.state == EnumAreaMinerState.BEFORE_INIT) {
            this.initState();
        }

        if (!this.world.isRemote) {
            if (!this.hasAxisAlignedBB()) {
                this.searchAABBProvider();
            } else {
                this.doWork();
            }
        }
    }

    public void initState() {
        if (this.areaMode) {
            this.setState(EnumAreaMinerState.OnceMode_Ready);
        } else {
            this.setState(EnumAreaMinerState.LoopMode_Ready);
        }
    }

    public void searchAABBProvider() {
        if (!this.areaMode) {
            this.setAxisAlignedBB(new AxisAlignedBB(this.getPos().offset(this.getFront(), -1)));
        } else {
            for (BlockPos pos : BlockPos.getAllInBox(this.getPos().add(-2, -2, -2), this.getPos().add(2, 2, 2))) {
                TileEntity tile = this.getWorld().getTileEntity(pos);
                if (tile instanceof IAABBProvider && ((IAABBProvider) tile).hasAxisAlignedBB()) {
                    this.setAxisAlignedBB(((IAABBProvider) tile).getAxisAlignedBB());
                    ((IAABBProvider) tile).setAxisAlignedBBToMachine();
                }
            }
        }
    }

    public void doWork() {
        if (this.walker == null) {
            this.setState(EnumAreaMinerState.OnceMode_Ready);
        }
//        EnumBotFail fail = this.bot.workOneStep();
//        switch (fail) {
//
//        }
        switch (this.state) {
            case Idle: break;
            case OnceMode_Ready:
                this.walker = UtilConstructionBot.onetimePosIterator(this.getAxisAlignedBB());
                this.setState(EnumAreaMinerState.OnceMode_Doing);
                break;
            case LoopMode_Ready:
                this.walker = UtilConstructionBot.foreverPosIterator(this.getAxisAlignedBB());
                this.setState(EnumAreaMinerState.LoopMode_Doing);
                break;
            case OnceMode_Doing:
            case LoopMode_Doing:
                double v = 1.0 + 4.0 * Math.log10((double) (this.laserEnergy / 1000L + 1L));

                if (this.areaMode && !IClayEnergyConsumer.compensateClayEnergy(this, (long)((double)energyPerTick * v))) {
                    return;
                }
//                this.setSyncFlag();
                this.progress += (long)((double)progressPerTick * v);
                this.laserEnergy = 0L;
                ItemStack filter = this.getStackInSlot(filterHarvestSlot);
                ItemStack filterF = this.getStackInSlot(filterFortuneSlot);
                ItemStack filterS = this.getStackInSlot(filterSilktouchSlot);
                boolean filterFlag = IFilter.isFilter(filter);
                boolean filterFflag = IFilter.isFilter(filterF);
                boolean filterSflag = IFilter.isFilter(filterS);
                final int slotNum = this.inventoryX.get() * this.inventoryY.get();
                final int maxJob = this.state == EnumAreaMinerState.OnceMode_Doing ? maxJobCount : maxJobCountInLoop;

                for (int i = 0; i < maxJob; i++) {
                    if (this.walker.hasNext()
                            && extracted(filter, filterF, filterS, filterFlag, filterFflag, filterSflag, slotNum, this.walker.next()))
                        break;
                }
        }
    }

    /**
     * auto-extracted method by IDEA
     * @return true when skipped by any reason
     */
    private boolean extracted(ItemStack filter, ItemStack filterF, ItemStack filterS, boolean filterFlag, boolean filterFflag, boolean filterSflag, int slotNum, final BlockPos miningPos) {
        IBlockState blockState = this.world.getBlockState(miningPos);
        Block block = blockState.getBlock();
        boolean isFluid = FluidRegistry.lookupFluidForBlock(block) != null || blockState.getMaterial() instanceof MaterialLiquid;
        double hardness = isFluid ? 1.0 : (double)blockState.getBlockHardness(this.world, miningPos);
        long neededProgress = (long)((double)progressPerJob * (0.1 + hardness));
        if (blockState.getBlock() == Blocks.AIR) {
            neededProgress = 0L;
        }

        neededProgress += this.replaceMode ? 1 : 0;
        boolean willDestroy = true;
        if (filterFlag) {
            willDestroy = IFilter.match(filter, blockState);
        }

        if (hardness == -1.0) {
            willDestroy = false;
        }

        if (willDestroy) {
            if (neededProgress > this.progress) {
                return true;
            }

            this.progress -= neededProgress;
            int fortune = 0;
            boolean silkTouch = false;
            if (filterFflag && IFilter.match(filterF, blockState)) {
                fortune = 3;
            }

            if (filterSflag && IFilter.match(filterS, blockState)) {
                silkTouch = true;
                fortune = 0;
            }

            List<ItemStack> items = UtilBuilder.getDropsFromBlock(this.world, miningPos, silkTouch, fortune);
            if (!UtilTransfer.canProduceItemStacks(items, this.containerItemStacks, 0, slotNum, this.getInventoryStackLimit())) {
                return true;
            }

            UtilBuilder.destroyBlock(this.world, miningPos, !silkTouch, silkTouch, fortune);
            UtilTransfer.produceItemStacks(items, this.containerItemStacks, 0, slotNum, this.getInventoryStackLimit());
            if (this.replaceMode) {
                this.placeFlag = true;
            }
        }

        if (this.replaceMode && this.placeFlag) {
            if (blockState.getBlock() != Blocks.AIR) {
                this.progress = 0L;
                return true;
            }

            boolean successFlag = false;
            for (int j = slotNum; j < slotNum * 2 && !successFlag; ++j) {
                ItemStack itemblock = this.getStackInSlot(j);
                if (!itemblock.isEmpty() && itemblock.getItem() instanceof ItemBlock) {
                    successFlag = UtilBuilder.placeBlockByItemBlock(itemblock, this.world, miningPos);
                    if (itemblock.isEmpty() || itemblock.getCount() == 0) {
                        this.setInventorySlotContents(j, ItemStack.EMPTY);
                    }
                }
            }

            if (!successFlag) {
                this.progress = 0L;
                return true;
            }

            this.placeFlag = false;
        }
        return false;
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        return ButtonProperty.PERMIT;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return true;
    }

    @Override
    public void pushButton(EntityPlayer player, int action) {
        switch (action) {
            case 0:
                this.setState(EnumAreaMinerState.OnceMode_Ready);
                break;
            case 1:
                this.setState(EnumAreaMinerState.Idle);
                break;
            case 2:
                this.setState(EnumAreaMinerState.LoopMode_Ready);
                break;
            case 3:
//                this.setSyncFlag();
                switch (this.boxState) {
                    case _0: this.boxState = Appearance._1; break;
                    case _1: this.boxState = Appearance._2; break;
                    case _2: this.boxState = Appearance._0; break;
                }
        }
    }

    public void setState(EnumAreaMinerState state) {
//        this.setSyncFlag();
        this.state = state;
    }

    @Override
    public boolean irradiateClayLaser(ClayLaser laser, EnumFacing direction) {
        this.laserEnergy = (long)((double)this.laserEnergy + laser.getEnergy());
        return true;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.boxState = Appearance.fromMeta(tagCompound.getByte("BoxState"));
        TileEntityClayMarker.readAxisAlignedBBFromNBT(tagCompound, this);
        this.laserEnergy = tagCompound.getLong("LaserEnergy");
        this.state = EnumAreaMinerState.getByMeta(tagCompound.getByte("State"));

        if (tagCompound.hasKey("walker", Constants.NBT.TAG_COMPOUND)) {
            if (state == EnumAreaMinerState.OnceMode_Doing) {
                this.walker = UtilConstructionBot.onetimePosIterator(this.aabb);
                this.walker.deserializeNBT(tagCompound.getCompoundTag("walker"));
            } else if (state == EnumAreaMinerState.LoopMode_Doing) {
                this.walker = UtilConstructionBot.foreverPosIterator(this.aabb);
                this.walker.deserializeNBT(tagCompound.getCompoundTag("walker"));
            }
        }

        this.progress = tagCompound.getLong("Progress");
        this.placeFlag = tagCompound.getBoolean("PlaceFlag");
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setByte("BoxState", (byte)this.boxState.toMeta());
        TileEntityClayMarker.writeAxisAlignedBBToNBT(tagCompound, this);
        tagCompound.setLong("LaserEnergy", this.laserEnergy);
        tagCompound.setByte("State", (byte)this.state.meta());
        tagCompound.setLong("Progress", this.progress);
        tagCompound.setBoolean("PlaceFlag", this.placeFlag);
        if (this.walker != null)
            tagCompound.setTag("walker", this.walker.serializeNBT());
        return tagCompound;
    }

    public AxisAlignedBB getAxisAlignedBB() {
        return this.aabb;
    }

    public void setAxisAlignedBB(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    public boolean hasAxisAlignedBB() {
        return this.aabb != null;
    }

    public Appearance getBoxAppearance() {
        return this.boxState;
    }

    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Double.POSITIVE_INFINITY;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot != filterHarvestSlot && slot != filterFortuneSlot && slot != filterSilktouchSlot ? super.isItemValidForSlot(slot, itemstack) : IFilter.isFilter(itemstack);
    }

    public void doWorkOnce() {
        this.setState(EnumAreaMinerState.OnceMode_Ready);
    }

    public void startWork() {
        this.setState(EnumAreaMinerState.LoopMode_Doing);
    }

    public void stopWork() {
        this.setState(EnumAreaMinerState.Idle);
    }

    public boolean isScheduled() {
        return true;
    }

    public boolean isDoingWork() {
//        return this.bot.isReadyToWork();
        return true;
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.ce;
    }
}
