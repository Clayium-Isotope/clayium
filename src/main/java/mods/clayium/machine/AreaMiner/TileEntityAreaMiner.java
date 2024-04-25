package mods.clayium.machine.AreaMiner;


import mods.clayium.core.ClayiumCore;
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
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.IntStream;

public class TileEntityAreaMiner extends TileEntityClayContainer implements IClayLaserMachine, AABBHolder, IExternalControl, IClayEnergyConsumer, IButtonProvider {
    protected AxisAlignedBB aabb;
    public Appearance boxState = Appearance._0;
    protected final LateInit<Integer> inventoryX = new LateInit<>();
    protected final LateInit<Integer> inventoryY = new LateInit<>();
    public static final int filterHarvestSlot = 37;
    public static final int filterFortuneSlot = 38;
    public static final int filterSilktouchSlot = 39;
    public long laserEnergy = 0L;
    protected final WrapMutableBlockPos miningPos = new WrapMutableBlockPos();
    public EnumAreaMinerState state = EnumAreaMinerState.BEFORE_INIT;
    public long progress = 0L;
    public static final long energyPerTick = 1000L;
    public static final int progressPerTick = 100;
    public static final int progressPerJob = 400;
    public static final int maxJobCount = 1000;
    public static final int maxJobCountInLoop = 10;
    public final LateInit<Boolean> replaceMode = new LateInit<>();
    public final LateInit<Boolean> areaMode = new LateInit<>();
    public boolean placeFlag = false;
    protected final ContainClayEnergy ce = new ContainClayEnergy();

    public TileEntityAreaMiner() {
    }

    public void initParams() {
        super.initParams();
        this.setImportRoutes(-1, 0, -1, -1, -1, -1);
        this.setExportRoutes(-1, -1, -1, -1, 0, 0);
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
                this.areaMode.set(false);
                this.replaceMode.set(false);
                break;
            case claySteel:
            case clayium:
            case ultimate:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.areaMode.set(true);
                this.replaceMode.set(false);
                break;
            case antimatter:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.areaMode.set(true);
                this.replaceMode.set(true);
        }

        this.slotsDrop = IntStream.concat(IntStream.range(0, slotNum), IntStream.of(this.getEnergySlot())).toArray();

        this.listSlotsImport.add(new int[]{this.getEnergySlot()});
        this.listSlotsExport.add(IntStream.range(0, slotNum).toArray());

        if (!this.areaMode.get()) {
            this.listSlotsImport.remove(0);

            for (EnumSide side : EnumSide.VALUES) {
                if (this.getImportRoute(side) == 0) {
                    this.setImportRoute(side, NONE_ROUTE);
                }
            }
        }

        if (this.replaceMode.get()) {
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
        if (this.getState() == EnumAreaMinerState.BEFORE_INIT) {
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
        if (this.areaMode.get()) {
            this.setState(EnumAreaMinerState.OnceMode_Ready);
        } else {
            this.setState(EnumAreaMinerState.LoopMode_Ready);
        }

    }

    public void searchAABBProvider() {
        if (!this.areaMode.get()) {
            EnumFacing back = this.getFront().getOpposite();
            this.setAxisAlignedBB(new AxisAlignedBB(BlockPos.ORIGIN).offset(this.pos).offset(new Vec3d(back.getDirectionVec())));
        } else {
            for (BlockPos offset : BlockPos.getAllInBox(-2, -2, -2, 2, 2, 2)) {
                TileEntity tile = this.world.getTileEntity(this.pos.add(offset));
                if (tile instanceof IAABBProvider && ((IAABBProvider)tile).hasAxisAlignedBB()) {
                    this.setAxisAlignedBB(((IAABBProvider)tile).getAxisAlignedBB());
                    ((IAABBProvider)tile).setAxisAlignedBBToMachine();
                }
            }
        }

    }

    /**
     * [TODO] レーザー値が0とチカチカするのが解釈不一致なので修正したい。
     * [BUG] 粘土エネルギーが減らない or ようにみえる
     */
    public void doWork() {
        AxisAlignedBB aabb = this.getAxisAlignedBB();
        switch (this.getState()) {
            case OnceMode_Ready:
                this.miningPos.setPos((int)Math.floor(aabb.minX + 0.5), (int)Math.floor(aabb.maxY - 0.5), (int)Math.floor(aabb.minZ + 0.5));
                this.setState(EnumAreaMinerState.OnceMode_Doing);
            case LoopMode_Ready:
                this.miningPos.setPos((int)Math.floor(aabb.minX + 0.5), (int)Math.floor(aabb.maxY - 0.5), (int)Math.floor(aabb.minZ + 0.5));
                this.setState(EnumAreaMinerState.LoopMode_Ready);
                break;
            case OnceMode_Doing:
            case LoopMode_Doing:
                long c = this.areaMode.get() ? (long)((double) energyPerTick * (1.0 + 4.0 * Math.log10((double)(this.laserEnergy / 1000L + 1L)))) : 0L;
                if (this.miningPos.getX() > (int)Math.floor(aabb.maxX - 0.5)) {
                    this.miningPos.setX((int)Math.floor(aabb.minX + 0.5));
                    this.miningPos.incrZ();
                }

                if (this.miningPos.getZ() > (int)Math.floor(aabb.maxZ - 0.5)) {
                    this.miningPos.setZ((int)Math.floor(aabb.minZ + 0.5));
                    this.miningPos.decrY();
                }

                if (this.miningPos.getY() < (int)Math.floor(aabb.minY + 0.5)) {
                    if (this.getState() == EnumAreaMinerState.OnceMode_Doing) {
                        this.setState(EnumAreaMinerState.Idle);
                        return;
                    }

                    this.miningPos.setPos((int)Math.floor(aabb.minX + 0.5), (int)Math.floor(aabb.maxY - 0.5), (int)Math.floor(aabb.minZ + 0.5));
                }

                if (IClayEnergyConsumer.compensateClayEnergy(this, c)) {
//                    this.setSyncFlag();
                    this.progress += (long)((double) progressPerTick * (1.0 + 4.0 * Math.log10((double)(this.laserEnergy / 1000L + 1L))));
                    this.laserEnergy = 0L;
                    ItemStack filter = this.getStackInSlot(TileEntityAreaMiner.filterHarvestSlot);
                    ItemStack filterF = this.getStackInSlot(TileEntityAreaMiner.filterFortuneSlot);
                    ItemStack filterS = this.getStackInSlot(TileEntityAreaMiner.filterSilktouchSlot);
                    boolean filterFlag = IFilter.isFilter(filter);
                    boolean filterFflag = IFilter.isFilter(filterF);
                    boolean filterSflag = IFilter.isFilter(filterS);
                    int slotNum = this.inventoryX.get() * this.inventoryY.get();
                    int max = this.getState() == EnumAreaMinerState.OnceMode_Doing ? maxJobCount : maxJobCountInLoop;

                    for(int count = 0; count < max; ++count) {
                        IBlockState blockstate = this.world.getBlockState(this.miningPos);
                        boolean isFluid = FluidRegistry.lookupFluidForBlock(blockstate.getBlock()) != null || blockstate.getMaterial() instanceof MaterialLiquid;
                        double hardness = isFluid ? 1.0 : (double) blockstate.getBlockHardness(this.world, this.miningPos);
                        long i = (long)((double) progressPerJob * (0.1 + hardness));
                        if (Blocks.AIR.equals(blockstate.getBlock())) {
                            i = 0L;
                        }

                        i += this.replaceMode.get() ? 1 : 0;
                        boolean flag = true;
                        if (filterFlag) {
                            flag = IFilter.match(filter, blockstate);
                        }

                        if (hardness == -1.0) {
                            flag = false;
                        }

                        if (flag) {
                            if (i > this.progress) {
                                break;
                            }

                            this.progress -= i;
                            int fortune = 0;
                            boolean silkTouch = false;
                            if (filterFflag && IFilter.match(filterF, blockstate)) {
                                fortune = 3;
                            }

                            if (filterSflag && IFilter.match(filterS, blockstate)) {
                                silkTouch = true;
                                fortune = 0;
                            }

                            List<ItemStack> items = UtilBuilder.getDropsFromBlock(this.world, this.miningPos, silkTouch, fortune);
                            if (!UtilTransfer.canProduceItemStacks(items, this.containerItemStacks, 0, slotNum, this.getInventoryStackLimit())) {
                                break;
                            }

                            UtilBuilder.destroyBlock(this.world, this.miningPos, !silkTouch, silkTouch, fortune);
                            UtilTransfer.produceItemStacks(items, this.containerItemStacks, 0, slotNum, this.getInventoryStackLimit());
                            if (this.replaceMode.get()) {
                                this.placeFlag = true;
                            }
                        }

                        if (this.replaceMode.get() && this.placeFlag) {
                            boolean flag2 = false;
                            if (Blocks.AIR.equals(this.world.getBlockState(this.miningPos).getBlock())) {
                                for (int j = slotNum; j < slotNum * 2; ++j) {
                                    ItemStack itemblock = this.getStackInSlot(j);
                                    if (itemblock != null && itemblock.getItem() instanceof ItemBlock) {
                                        flag2 = UtilBuilder.placeBlockByItemBlock(itemblock, this.world, this.miningPos);
                                        if (itemblock.isEmpty()) {
                                            this.setInventorySlotContents(j, ItemStack.EMPTY);
                                        }
                                    }

                                    if (flag2) {
                                        break;
                                    }
                                }
                            }

                            if (!flag2) {
                                this.progress = 0L;
                                break;
                            }

                            this.placeFlag = false;
                        }

                        this.miningPos.incrX();
                        if (this.miningPos.getX() > (int)Math.floor(aabb.maxX - 0.5)) {
                            this.miningPos.setX((int)Math.floor(aabb.minX + 0.5));
                            this.miningPos.incrZ();
                        }

                        if (this.miningPos.getZ() > (int)Math.floor(aabb.maxZ - 0.5)) {
                            this.miningPos.setZ((int)Math.floor(aabb.minZ + 0.5));
                            this.miningPos.decrY();
                        }

                        if (this.miningPos.getY() < (int)Math.floor(aabb.minY + 0.5)) {
                            if (this.getState() == EnumAreaMinerState.OnceMode_Doing) {
                                this.setState(EnumAreaMinerState.Idle);
                                break;
                            }

                            this.miningPos.setPos((int)Math.floor(aabb.minX + 0.5), (int)Math.floor(aabb.maxY - 0.5), (int)Math.floor(aabb.minZ + 0.5));
                        }

                        if (count == max - 1) {
                            this.progress = 0L;
                        }
                    }
                }
            case Idle:
        }

    }

    @Override
    public ButtonProperty canPushButton(int button) {
        return ButtonProperty.PERMIT;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return true;
    }

    public void pushButton(EntityPlayer player, int action) {
        ClayiumCore.logger.info("[TEAreaMiner] pushed: " + action);
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
                this.syncBoxState();
        }
    }

    public void syncBoxState() {
        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(AABBHolder.APPEARANCE, this.boxState));
    }

    public EnumAreaMinerState getState() {
        return this.state;
    }

    public void setState(EnumAreaMinerState state) {
//        this.setSyncFlag();
        this.state = state;
    }

    @Override
    public boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing) {
        this.laserEnergy = (long)((double)this.laserEnergy + laser.getEnergy());
        return true;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.boxState = Appearance.fromMeta(tagCompound.getByte("BoxState"));
        this.syncBoxState();
        TileEntityClayMarker.readAxisAlignedBBFromNBT(tagCompound, this);

        this.laserEnergy = tagCompound.getLong("LaserEnergy");
        this.miningPos.setPos(NBTUtil.getPosFromTag(tagCompound.getCompoundTag("MiningPos")));
        this.state = EnumAreaMinerState.getByMeta(tagCompound.getByte("State"));
        this.progress = tagCompound.getLong("Progress");
        this.placeFlag = tagCompound.getBoolean("PlaceFlag");
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setByte("BoxState", (byte)this.boxState.toMeta());
        TileEntityClayMarker.writeAxisAlignedBBToNBT(tagCompound, this);

        tagCompound.setLong("LaserEnergy", this.laserEnergy);
        tagCompound.setTag("MiningPos", NBTUtil.createPosTag(this.miningPos));
        tagCompound.setByte("State", (byte)this.state.meta());
        tagCompound.setLong("Progress", this.progress);
        tagCompound.setBoolean("PlaceFlag", this.placeFlag);

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

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot == filterHarvestSlot || slot == filterFortuneSlot || slot == filterSilktouchSlot ? IFilter.isFilter(itemstack)
                : super.isItemValidForSlot(slot, itemstack);
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
        return this.getState() == EnumAreaMinerState.OnceMode_Doing || this.getState() == EnumAreaMinerState.LoopMode_Doing;
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.ce;
    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import_energy", "import");
        this.registerExtractIcons("export");
    }
}
