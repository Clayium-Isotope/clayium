package mods.clayium.machine.AreaMiner;

import mods.clayium.component.bot.*;
import mods.clayium.component.teField.FieldComponent;
import mods.clayium.component.teField.FieldDelegate;
import mods.clayium.component.teField.FieldManager;
import mods.clayium.component.value.ContainClayEnergy;
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
import mods.clayium.util.LateInit;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Slot ID
 * Block Breaker:
 *  0: Energy
 *  1: harvest filter
 *  2: UNUSED
 *  3: UNUSED
 *  4-12: Output
 * Miner:
 *  0: Energy
 *  1: harvest filter
 *  2: fortune filter
 *  3: silktouch filter
 *  4-12: Output
 *  13-21: Input
 */
public class TileEntityAreaWorker extends TileEntityClayContainer implements IClayLaserMachine, AABBHolder, IExternalControl, IClayEnergyConsumer, IButtonProvider, FieldDelegate {
    public static final int filterHarvestSlot = 1;
    public static final int filterFortuneSlot = 2;
    public static final int filterSilktouchSlot = 3;
    public static final long energyPerTick = 1000L;
    public static final int progressPerTick = 100;
    public static final int progressPerJob = 400;
    public static final int maxJobCount = 1000;
    public static final int maxJobCountInLoop = 10;

    protected AxisAlignedBB aabb = NULL_AABB;
    public Appearance boxState = Appearance.NoRender;
    protected final LateInit<Integer> inventoryX = new LateInit<>();
    protected final LateInit<Integer> inventoryY = new LateInit<>();
    public long laserEnergy = 0L;
    public EnumAreaMinerState state = EnumAreaMinerState.BEFORE_INIT;
    protected final ContainClayEnergy ce = new ContainClayEnergy();
    protected final FieldComponent manager = new FieldManager(this.ce);

    protected final LateInit<LocalBot> localBot = new LateInit<>();
    protected final LateInit<AreaBot> areaBot = new LateInit<>();
    protected final LateInit<IItemHandler> botInput = new LateInit<>();
    protected final LateInit<IItemHandler> botReference = new LateInit<>();
    protected final LateInit<IItemHandler> botOutput = new LateInit<>();
    protected final LateInit<Consumer<TileEntityAreaWorker>> aabbDeterminer = new LateInit<>();

    public void initParams() {
        super.initParams();
        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, 0, 0);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(22, ItemStack.EMPTY);
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
        return 0;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);
        int slotNum;
        if (this.isAreaReplacer()) {
            this.inventoryX.set(4);
            this.inventoryY.set(2);
            slotNum = 8;
        } else {
            this.inventoryX.set(3);
            this.inventoryY.set(3);
            slotNum = 9;
        }

        this.slotsDrop = IntStream.concat(IntStream.rangeClosed(1, slotNum), IntStream.of(this.getEnergySlot())).toArray();

        this.listSlotsImport.add(new int[]{ this.getEnergySlot() });
        this.listSlotsExport.add(IntStream.rangeClosed(1, slotNum).toArray());
        this.botOutput.set(UtilTransfer.getItemHandler(this, null, IntStream.rangeClosed(1, slotNum).toArray()));
        this.botReference.set(UtilTransfer.getItemHandler(this, null, new int[] { filterHarvestSlot, filterFortuneSlot, filterSilktouchSlot }));
        this.botInput.set(UtilTransfer.getItemHandler(this, null, IntStream.range(4 + slotNum, 4 + slotNum * 2).toArray()));

        this.areaBot.set(new AreaBotAreaWorker());
        switch (tier) {
            case precision -> initAsBreaker();
            case clayium -> initAsMiner();
            case ultimate -> initAsAdvMiner();
            case antimatter -> initAsReplacer(slotNum);
        }
    }

    protected void initAsBreaker() {
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
        this.autoExtractInterval = this.autoInsertInterval = 2;
        this.setState(EnumAreaMinerState.LoopMode_Ready);

        // Remove Energy Import
        this.listSlotsImport.remove(0);

        this.setImportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.aabbDeterminer.set(TileEntityAreaWorker::applyBlockAABB);
        this.localBot.set(new LocalBotMiner());
    }

    protected void initAsMiner() {
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.setState(EnumAreaMinerState.OnceMode_Ready);
        this.aabbDeterminer.set(TileEntityAreaWorker::applyAABBProvider);
        this.localBot.set(new LocalBotMiner());
    }

    protected void initAsAdvMiner() {
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.setState(EnumAreaMinerState.OnceMode_Ready);
        this.aabbDeterminer.set(TileEntityAreaWorker::applyAABBProvider);
        this.localBot.set(new LocalBotAdvMiner());
    }

    protected void initAsReplacer(int slotNum) {
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.setState(EnumAreaMinerState.OnceMode_Ready);

        this.listSlotsImport.add(IntStream.range(slotNum, slotNum * 2).toArray());
        this.slotsDrop = IntStream.concat(IntStream.range(0, slotNum * 2), IntStream.of(this.getEnergySlot())).toArray();
        this.aabbDeterminer.set(TileEntityAreaWorker::applyAABBProvider);
        this.localBot.set(new LocalBotReplacer());
    }

    @Override
    public void update() {
        super.update();

        if (!this.world.isRemote) {
            if (!this.hasAxisAlignedBB()) {
                this.aabbDeterminer.get().accept(this);
                this.areaBot.get().setAxisAlignedBB(this.getAxisAlignedBB());
            } else {
                if (!this.isLoaded) {
                    this.areaBot.get().setAxisAlignedBB(this.getAxisAlignedBB());
                    this.localBot.get().setWorld(this.getWorld());
                }
                this.doWork();
            }
        }
    }

    protected static void applyBlockAABB(TileEntityAreaWorker te) {
        te.setAxisAlignedBB(new AxisAlignedBB(te.getPos().offset(te.getFront(), -1)));
    }

    protected static void applyAABBProvider(TileEntityAreaWorker te) {
        for (BlockPos pos : BlockPos.getAllInBox(te.getPos().add(-2, -2, -2), te.getPos().add(2, 2, 2))) {
            TileEntity marker = te.getWorld().getTileEntity(pos);
            if (marker instanceof IAABBProvider && ((IAABBProvider) marker).hasAxisAlignedBB()) {
                te.setAxisAlignedBB(((IAABBProvider) marker).getAxisAlignedBB());
                ((IAABBProvider) marker).postApplyAABBToMachine();
            }
        }
    }

    public void doWork() {
        switch (this.state) {
            case Idle: break;
            case OnceMode_Ready:
                this.areaBot.get().setAxisAlignedBB(this.getAxisAlignedBB());
                this.areaBot.get().setFinite(true);
                this.areaBot.get().reboot();
                this.localBot.get().setWorld(this.getWorld());
                this.setState(EnumAreaMinerState.OnceMode_Doing);
                break;
            case LoopMode_Ready:
                this.areaBot.get().setAxisAlignedBB(this.getAxisAlignedBB());
                this.areaBot.get().setFinite(false);
                this.areaBot.get().reboot();
                this.localBot.get().setWorld(this.getWorld());
                this.setState(EnumAreaMinerState.LoopMode_Doing);
                break;
            case OnceMode_Doing:
            case LoopMode_Doing:
                double v = 1.0 + 4.0 * Math.log10((double) (this.laserEnergy / 1000L + 1L));

                if ((this.isAreaMode() && IClayEnergyConsumer.checkAndConsumeClayEnergy(this, (long)((double)energyPerTick * v)))
                    || !this.isAreaMode()) {
                    this.laserEnergy = 0L;
                    this.areaBot.get().addProgress((long)((double) progressPerTick * v));
                    this.markDirty();
                }
//                this.setSyncFlag();

                final int maxJob = this.state == EnumAreaMinerState.OnceMode_Doing ? maxJobCount : maxJobCountInLoop;
                EnumBotResult workResult = EnumBotResult.Success;
                for (int i = 0; i < maxJob && workResult == EnumBotResult.Success; i++) {
                    workResult = this.areaBot.get().work(this.botInput.get(), this.botReference.get(), this.botOutput.get(), this.localBot.get());
                }

                if (workResult == EnumBotResult.EndOfTerm) {
                    this.setState(EnumAreaMinerState.Idle);
                }
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

    @Override
    public void pushButton(EntityPlayer player, int action) {
        switch (action) {
            case 0 -> this.setState(EnumAreaMinerState.OnceMode_Ready);
            case 1 -> this.setState(EnumAreaMinerState.Idle);
            case 2 -> this.setState(EnumAreaMinerState.LoopMode_Ready);
            case 3 -> {
//                this.setSyncFlag();
                this.boxState = switch (this.boxState) {
                    case NoRender -> Appearance.Box_Worker;
                    case Box_Worker -> Appearance.NoRender;
                    default -> this.boxState;
                };
                this.applyAppearance(this.world, this.pos);
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
        this.containEnergy().deserializeNBT((NBTTagIntArray) tagCompound.getTag("ClayEnergy"));

        this.areaBot.get().deserializeNBT(tagCompound.getCompoundTag("AreaBot"));
        this.localBot.get().deserializeNBT(tagCompound.getCompoundTag("WorkBot"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setByte("BoxState", (byte)this.boxState.toMeta());
        TileEntityClayMarker.writeAxisAlignedBBToNBT(tagCompound, this);
        tagCompound.setLong("LaserEnergy", this.laserEnergy);
        tagCompound.setByte("State", (byte)this.state.meta());
        tagCompound.setTag("ClayEnergy", this.containEnergy().serializeNBT());

        tagCompound.setTag("AreaBot", this.areaBot.get().serializeNBT());
        tagCompound.setTag("WorkBot", this.localBot.get().serializeNBT());
        return tagCompound;
    }

    public AxisAlignedBB getAxisAlignedBB() {
        return this.aabb;
    }

    public void setAxisAlignedBB(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    public boolean hasAxisAlignedBB() {
        return this.aabb != NULL_AABB;
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
        if (slot == filterHarvestSlot || slot == filterFortuneSlot || slot == filterSilktouchSlot)
            return IFilter.isFilter(itemstack);
        if (slot == this.getEnergySlot()) {
            return IClayEnergyConsumer.isItemValidForSlot(this, slot, itemstack);
        }
        return true;
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
        return true;
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.ce;
    }

    public boolean isAreaMode() {
        return this.tier != TierPrefix.precision;
    }

    public boolean hasSomeFilterSlot() {
        return this.tier == TierPrefix.ultimate || this.tier == TierPrefix.antimatter;
    }

    public boolean isAreaReplacer() {
        return this.tier == TierPrefix.antimatter;
    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }

    @Override
    public FieldComponent getDelegate() {
        return this.manager;
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import_energy", "import");
        this.registerExtractIcons("export");
    }
}
