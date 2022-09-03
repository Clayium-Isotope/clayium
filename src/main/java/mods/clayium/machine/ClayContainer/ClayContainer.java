package mods.clayium.machine.ClayContainer;

//import cofh.redstoneflux.api.IEnergyConnection;

import com.google.common.collect.ImmutableMap;
import mods.clayium.block.common.ITieredBlock;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public abstract class ClayContainer extends BlockContainer implements ITieredBlock {
    private final Class<? extends TileEntityClayContainer> teClass;
    private final int guiId;
    protected final int tier;

    public ClayContainer(Material material, Class<? extends TileEntityClayContainer> teClass, String modelPath, int guiId, int tier) {
        super(material);
        this.teClass = teClass;
        this.guiId = guiId;
        this.tier = tier;

        setUnlocalizedName(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);

        if (this.getDefaultState() instanceof ClayContainerState) {
            setDefaultState(this.getDefaultState()
                    .withProperty(ClayContainerState.ARM_UP, false)
                    .withProperty(ClayContainerState.ARM_DOWN, false)
                    .withProperty(ClayContainerState.ARM_NORTH, false)
                    .withProperty(ClayContainerState.ARM_SOUTH, false)
                    .withProperty(ClayContainerState.ARM_WEST, false)
                    .withProperty(ClayContainerState.ARM_EAST, false)
                    .withProperty(ClayContainerState.IS_PIPE, false)
            );
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getTileEntity(pos) instanceof IInventory)
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) worldIn.getTileEntity(pos));

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (this.teClass == null) return null;

        try {
            TileEntityClayContainer tecc = teClass.newInstance();
            tecc.initByTier(this.tier);
            return tecc;
        } catch (Exception exception) {
            ClayiumCore.logger.catching(exception);
            return null;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        // the used item which implements IModifyCC calls own Item#onItemUse
        if (playerIn.getHeldItem(hand).getItem() instanceof IModifyCC) return false;

        worldIn.markBlockRangeForRenderUpdate(pos, pos);

        // for looks like incomplete
        ClayContainerState.checkSurroundConnection(worldIn, pos, worldIn.getTileEntity(pos));

        openGui(worldIn, pos, playerIn);
        return true;
    }

    protected void openGui(int guiId, World world, BlockPos pos, EntityPlayer player) {
        player.openGui(ClayiumCore.instance(), guiId, world, pos.getX(), pos.getY(), pos.getZ());
    }

    protected void openGui(World world, BlockPos pos, EntityPlayer player) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer && world.getTileEntity(pos) instanceof TileGeneric)
            openGui(((ClayContainer) block).guiId, world, pos, player);
    }

    /**
     * Modifying Place States -> getStateForPlacement
     * Modifying Place Fields -> onBlockPlacedBy
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
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

    @Override
    public int getTier(ItemStack stackIn) {
        return this.tier;
    }

    @Override
    public int getTier(IBlockAccess access, BlockPos posIn) {
        return this.tier;
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

        public static boolean renderAsPipe(@Nullable TileEntity tile) {
            if (tile == null) return false;

            if (!(tile.getBlockType() instanceof ClayContainer)
                    || !((ClayContainer) tile.getBlockType()).canBePipe()) return false;

            return tile instanceof TileEntityClayContainer
                    && ((TileEntityClayContainer) tile).isPipe();
        }

        @Override
        public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
            if (!renderAsPipe(worldIn.getTileEntity(pos))) {
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
            if (!renderAsPipe(worldIn.getTileEntity(pos))) {
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

        public static IBlockState checkSurroundConnection(World worldIn, BlockPos pos, TileEntity here) {
            if (!(worldIn.getBlockState(pos) instanceof ClayContainerState))
                return worldIn.getBlockState(pos);

            NBTTagCompound tag = here.writeToNBT(new NBTTagCompound());
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(IS_PIPE, renderAsPipe(here)));
            for (EnumFacing facing : EnumFacing.VALUES) {
                changeConnectionState(worldIn, pos, here, facing);
            }
            worldIn.getTileEntity(pos).readFromNBT(tag);

            return worldIn.getBlockState(pos);
        }

        private static IProperty<Boolean> getFacingProperty(EnumFacing facing) {
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

        public static void changeConnectionState(World world, BlockPos pos, TileEntity here, EnumFacing facing) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(
                    getFacingProperty(facing), checkPipeConnection(here, world.getTileEntity(pos.offset(facing)), facing)
            ), 2);
        }

        @Override
        public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
            EnumFacing facing = EnumFacing.getFacingFromVector(fromPos.getX() - pos.getX(), fromPos.getY() - pos.getY(), fromPos.getZ() - pos.getZ());

            NBTTagCompound tag = worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound());
            changeConnectionState(worldIn, pos, worldIn.getTileEntity(pos), facing);
            worldIn.getTileEntity(pos).readFromNBT(tag);
        }

        private static boolean checkPipeConnection(TileEntity myself, TileEntity customer, EnumFacing direction) {
            if (!(myself instanceof TileEntityClayContainer) || !(customer instanceof IInventory)) return false;

            if (((TileEntityClayContainer) myself).importRoutes.get(direction) != -1 || ((TileEntityClayContainer) myself).exportRoutes.get(direction) != -1)
                return (((TileEntityClayContainer) myself).importRoutes.get(direction) != -1 && ((TileEntityClayContainer) myself).autoExtract)
                    || (((TileEntityClayContainer) myself).exportRoutes.get(direction) != -1 && ((TileEntityClayContainer) myself).autoInsert);

            if (!(customer instanceof TileEntityClayContainer)) return false;

            return (((TileEntityClayContainer) customer).importRoutes.get(direction.getOpposite()) != -1 && ((TileEntityClayContainer) customer).autoExtract)
                    || (((TileEntityClayContainer) customer).exportRoutes.get(direction.getOpposite()) != -1 && ((TileEntityClayContainer) customer).autoInsert);
        }
    }
}