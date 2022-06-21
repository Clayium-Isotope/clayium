package mods.clayium.machine.ClayContainer;

//import cofh.redstoneflux.api.IEnergyConnection;

import com.google.common.collect.ImmutableMap;
import mods.clayium.block.tile.TileGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ClayContainer extends BlockContainer {
    private final Class<? extends TileEntity> teClass;
    private final int guiId;
    private final int tier;

    public ClayContainer(Material material, Class<? extends TileEntity> teClass, String modelPath, int guiId, int tier) {
        super(material);
        this.teClass = teClass;
        this.guiId = guiId;
        this.tier = tier;

        setUnlocalizedName(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//        TileEntityClayContainer te = (TileEntityClayContainer) worldIn.getTileEntity(pos);
//        assert te != null;
//        InventoryHelper.dropInventoryItems(worldIn, pos, te);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (this.teClass == null) return null;

        try {
            // TileEntity Constructor mustn't have any arguments
            return teClass.newInstance();
        } catch (Exception exception) {
            ClayiumCore.logger.catching(exception);
            return null;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        for (ItemStack stack : playerIn.getHeldEquipment()) {
            if (stack.getItem() instanceof IModifyCC) return false;
        }

        return onBlockRightClicked(worldIn, pos, playerIn, facing, hitX, hitY, hitZ);
    }

    protected void openGui(int guiId, World world, BlockPos pos, EntityPlayer player) {
        player.openGui(ClayiumCore.instance(), guiId, world, pos.getX(), pos.getY(), pos.getZ());
    }

    protected void openGui(World world, BlockPos pos, EntityPlayer player) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer && world.getTileEntity(pos) instanceof TileGeneric)
            openGui(((ClayContainer) block).guiId, world, pos, player);
    }

    protected boolean onBlockRightClicked(World world, BlockPos pos, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.addBlockEvent(pos, this, 0, 0);
        openGui(world, pos, player);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(ClayContainerState.IS_PIPE, false), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityClayContainer) {
                ((TileEntityClayContainer)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (world.getTileEntity(pos) instanceof TileGeneric && ((TileGeneric) world.getTileEntity(pos)).hasSpecialDrops())
            ((TileGeneric) world.getTileEntity(pos)).getDrops(drops, world, pos, state, fortune);
        else
            super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean canBePipe() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, this.getUnlocalizedName());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (canBePipe())
            return new ClayContainerStateContainer(this);
        return super.createBlockState();
    }

    public int getTier() {
        return tier;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(ClayContainerState.IS_PIPE))
            return 1;
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ClayContainerState.IS_PIPE, meta == 1);
    }



    private static class ClayContainerStateContainer extends BlockStateContainer {
        public ClayContainerStateContainer(Block blockIn) {
            super(blockIn, ClayContainerState.getPropertyList().toArray(new IProperty[0]));
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new ClayContainerState(block, properties);
        }
    }

    public static class ClayContainerState extends BlockStateContainer.StateImplementation {
        private static final float pipeWidth = 0.1875F;
        protected static final PropertyBool IS_PIPE = PropertyBool.create("is_pipe");

        /* these Boolean Properties knows their own arm is activated */
        protected static final PropertyBool ARM_UP    = PropertyBool.create("arm_up");
        protected static final PropertyBool ARM_DOWN  = PropertyBool.create("arm_down");
        protected static final PropertyBool ARM_NORTH = PropertyBool.create("arm_north");
        protected static final PropertyBool ARM_SOUTH = PropertyBool.create("arm_south");
        protected static final PropertyBool ARM_WEST  = PropertyBool.create("arm_west");
        protected static final PropertyBool ARM_EAST  = PropertyBool.create("arm_east");

        private AxisAlignedBB aabb = fullBB();

        protected ClayContainerState(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
            super(blockIn, propertiesIn);
        }

        public static List<IProperty<?>> getPropertyList() {
            return Arrays.asList(
                    IS_PIPE,
                    ARM_UP, ARM_DOWN, ARM_NORTH, ARM_SOUTH, ARM_WEST, ARM_EAST
            );
        }

        @Override
        public ClayContainer getBlock() {
            return (ClayContainer) super.getBlock();
        }

        @Override
        public AxisAlignedBB getBoundingBox(IBlockAccess source, BlockPos pos) {
            return aabb;
        }

        private static AxisAlignedBB fullBB() {
            return new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 1.0d);
        }

        private static AxisAlignedBB pipeBB() {
            return new AxisAlignedBB(0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d - pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth, 0.5d + pipeWidth);
        }

        private static AxisAlignedBB pipeArmBB(EnumFacing facing) {
            return new AxisAlignedBB(
                    facing == EnumFacing.WEST  ? 0.0d : 0.5d - ( facing == EnumFacing.EAST  ? - pipeWidth : + pipeWidth ),
                    facing == EnumFacing.DOWN  ? 0.0d : 0.5d - ( facing == EnumFacing.UP    ? - pipeWidth : + pipeWidth ),
                    facing == EnumFacing.NORTH ? 0.0d : 0.5d - ( facing == EnumFacing.SOUTH ? - pipeWidth : + pipeWidth ),
                    facing == EnumFacing.EAST  ? 1.0d : 0.5d + ( facing == EnumFacing.WEST  ? - pipeWidth : + pipeWidth ),
                    facing == EnumFacing.UP    ? 1.0d : 0.5d + ( facing == EnumFacing.DOWN  ? - pipeWidth : + pipeWidth ),
                    facing == EnumFacing.SOUTH ? 1.0d : 0.5d + ( facing == EnumFacing.NORTH ? - pipeWidth : + pipeWidth )
            );

//            return pipeBB().offset(
//                    (facing.getFrontOffsetX() == 1 ? pipeWidth * 2.0d : 0.0d) - (facing.getFrontOffsetX() == -1 ? 0.5d - pipeWidth : 0.0d),
//                    (facing.getFrontOffsetY() == 1 ? pipeWidth * 2.0d : 0.0d) - (facing.getFrontOffsetY() == -1 ? 0.5d - pipeWidth : 0.0d),
//                    (facing.getFrontOffsetZ() == 1 ? pipeWidth * 2.0d : 0.0d) - (facing.getFrontOffsetZ() == -1 ? 0.5d - pipeWidth : 0.0d)
//            );
        }

        public static boolean renderAsPipe(IBlockAccess world, BlockPos pos) {
            if (!(world.getBlockState(pos).getBlock() instanceof ClayContainer)
                    || !((ClayContainer) world.getBlockState(pos).getBlock()).canBePipe()) return false;

//            return world.getBlockState(pos).getValue(IS_PIPE);
            return world.getTileEntity(pos) instanceof TileEntityClayContainer
                    && ((TileEntityClayContainer) world.getTileEntity(pos)).isPipe();
        }

        @Override
        public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
            if (!renderAsPipe(worldIn, pos)) {
//                super.setBlockBoundsBasedOnState((IBlockAccess) world, x, y, z);
                this.aabb = fullBB();
                return super.collisionRayTrace(worldIn, pos, start, end);
            }

            this.aabb = pipeBB();
            RayTraceResult rtr = super.collisionRayTrace(worldIn, pos, start, end);

            RayTraceResult rtr1;
            for (EnumFacing facing : EnumFacing.VALUES) {
                if (worldIn.getBlockState(pos) instanceof ClayContainerState
                        && ((ClayContainerState) worldIn.getBlockState(pos)).isTheFacingActivated(facing)) {
                    this.aabb = pipeArmBB(facing);
                    rtr1 = super.collisionRayTrace(worldIn, pos, start, end);
                    if (rtr1 != null && (
                            rtr == null || rtr1.hitVec.distanceTo(start) < rtr.hitVec.distanceTo(start)
                    )) return rtr1;
                }
            }

            this.aabb = pipeBB();
            return rtr;
        }

        @Override
        public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185908_6_) {
            if (!renderAsPipe(worldIn, pos)) {
                if (entityBox.intersects(fullBB().offset(pos))) {
                    collidingBoxes.add(fullBB().offset(pos));
                }
                return;
            }

            if (entityBox.intersects(pipeBB().offset(pos))) {
                collidingBoxes.add(pipeBB().offset(pos));
            }

            for (EnumFacing facing : EnumFacing.VALUES) {
                if (worldIn.getBlockState(pos).getValue(getFacingProperty(facing))) {
                    if (entityBox.intersects(pipeArmBB(facing).offset(pos)))
                        collidingBoxes.add(pipeArmBB(facing).offset(pos));
                }
            }
        }

        @Override
        public boolean isFullCube() {
            return false;
        }

        @Override
        public boolean isFullBlock() {
            return false;
        }

        @Override
        public boolean isOpaqueCube() {
            return false;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean hasCustomBreakingProgress() {
            return true;
        }

        @Override
        public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
            if (!(worldIn.getTileEntity(pos) instanceof TileEntityClayContainer))
                return false;

            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(IS_PIPE, renderAsPipe(worldIn, pos)));
            updateSurroundConnection(worldIn, pos);

            return true;
        }

        private IProperty<Boolean> getFacingProperty(EnumFacing facing) {
            switch (facing) {
                case UP: return ARM_UP;
                case DOWN: return ARM_DOWN;
                case NORTH: return ARM_NORTH;
                case SOUTH: return ARM_SOUTH;
                case WEST: return ARM_WEST;
                case EAST: return ARM_EAST;
            }
            return ARM_DOWN;
        }

        public boolean isTheFacingActivated(EnumFacing facing) {
            return this.getValue(getFacingProperty(facing));
        }

        public void updateSurroundConnection(World world, BlockPos pos) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                changeConnectionState(world, pos, facing);
            }
        }

        public void changeConnectionState(World world, BlockPos pos, EnumFacing facing) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(
                    getFacingProperty(facing), checkPipeConnection(world.getTileEntity(pos), world.getTileEntity(pos.offset(facing)), facing)
            ));

            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }

        @Override
        public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
            changeConnectionState(worldIn, pos, EnumFacing.getFacingFromVector(fromPos.getX() - pos.getX(), fromPos.getY() - pos.getY(), fromPos.getZ() - pos.getZ()));
        }

        public static boolean checkPipeConnection(@Nullable TileEntity te1, @Nullable TileEntity te2, EnumFacing direction) {
            if (PipeConnectType.checkConnection(getConnectionAsImport(te1, direction, te2), getConnectionAsExport(te2, direction.getOpposite(), te1))) return true;
            if (PipeConnectType.checkConnection(getConnectionAsExport(te1, direction, te2), getConnectionAsImport(te2, direction.getOpposite(), te1))) return true;

            return false;
        }

        public static PipeConnectType getConnectionAsImport(TileEntity from, EnumFacing side, TileEntity to) {
//            if (!(from instanceof net.minecraft.inventory.IInventory)) return PipeConnectType.Impossible;
//
//            if (from instanceof IEnergyConnection && ((IEnergyConnection) from).canConnectEnergy(side)
//                    && to instanceof IEnergyConnection && ((IEnergyConnection) to).canConnectEnergy(side.getOpposite()))
//                return PipeConnectType.Forced;
//
//            if (!(from instanceof TileEntityClayContainer)) return PipeConnectType.Trying;
//            TileEntityClayContainer container = (TileEntityClayContainer) from;
//
//            if (container instanceof mods.clayium.block.tile.TileFluidTranslator && to instanceof net.minecraftforge.fluids.IFluidHandler) {
//                if (!(to instanceof mods.clayium.block.tile.TileFluidTranslator)) {
//                    return PipeConnectType.Forced;
//                }
//            }
//            if (this.getBlock().insertRoutes.get(side) != -1) {
//                return container.autoExtract ? PipeConnectType.Trying : PipeConnectType.NoIntention;
//            }
//            return (container instanceof mods.clayium.block.tile.TileClayBuffer
//                    || container instanceof mods.clayium.block.tile.TileMultitrackBuffer
//                    || container instanceof mods.clayium.block.tile.TileStorageContainer) ? PipeConnectType.NoIntention : PipeConnectType.Impossible;
            if (from instanceof TileEntityClayContainer && to instanceof TileEntityClayContainer)
                return PipeConnectType.Trying;
            else return PipeConnectType.Impossible;
        }

        public static PipeConnectType getConnectionAsExport(TileEntity from, EnumFacing side, TileEntity to) {
//            if (!(from instanceof net.minecraft.inventory.IInventory)) return PipeConnectType.Impossible;
//
//            if (from instanceof IEnergyConnection && ((IEnergyConnection) from).canConnectEnergy(side)
//                    && to instanceof IEnergyConnection && ((IEnergyConnection) to).canConnectEnergy(side.getOpposite()))
//                return PipeConnectType.Forced;
//
//            if (!(from instanceof TileEntityClayContainer)) return PipeConnectType.Trying;
//            TileEntityClayContainer container = (TileEntityClayContainer) from;
//
//            if (container instanceof mods.clayium.block.tile.TileFluidTranslator && to instanceof net.minecraftforge.fluids.IFluidHandler) {
//                if (!(to instanceof mods.clayium.block.tile.TileFluidTranslator)) {
//                    return PipeConnectType.Forced;
//                }
//            }
//            if (this.getBlock().extractRoutes.get(side) != -1) {
//                return container.autoInsert ? PipeConnectType.Trying : PipeConnectType.NoIntention;
//            }
//            return (container instanceof mods.clayium.block.tile.TileClayBuffer
//                    || container instanceof mods.clayium.block.tile.TileMultitrackBuffer
//                    || container instanceof mods.clayium.block.tile.TileStorageContainer) ? PipeConnectType.NoIntention : PipeConnectType.Impossible;

            if (from instanceof TileEntityClayContainer && to instanceof TileEntityClayContainer)
                return PipeConnectType.Trying;
            else return PipeConnectType.Impossible;
        }

        private enum PipeConnectType {
            Impossible,
            NoIntention,
            Trying,
            Forced;

            public static boolean checkConnection(PipeConnectType from, PipeConnectType to) {
                if (from == Impossible || to == Impossible) return false;

                return !(from == NoIntention && to == NoIntention);
            }
        }
    }
}