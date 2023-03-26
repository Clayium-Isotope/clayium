package mods.clayium.machine.ClayContainer;

//import cofh.redstoneflux.api.IEnergyConnection;

import com.google.common.collect.ImmutableMap;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.block.tile.TileGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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
    private final Class<? extends TileGeneric> teClass;
    private final int guiId;
    protected final int tier;

    public ClayContainer(Material material, Class<? extends TileGeneric> teClass, String modelPath, int guiId, int tier) {
        super(material);
        this.teClass = teClass;
        this.guiId = guiId;
        this.tier = tier;

        setUnlocalizedName(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);

        if (this.getDefaultState() instanceof BlockStateClayContainer) {
            setDefaultState(this.getDefaultState()
                    .withProperty(BlockStateClayContainer.ARM_UP, false)
                    .withProperty(BlockStateClayContainer.ARM_DOWN, false)
                    .withProperty(BlockStateClayContainer.ARM_NORTH, false)
                    .withProperty(BlockStateClayContainer.ARM_SOUTH, false)
                    .withProperty(BlockStateClayContainer.ARM_WEST, false)
                    .withProperty(BlockStateClayContainer.ARM_EAST, false)
                    .withProperty(BlockStateClayContainer.IS_PIPE, false)
                    .withProperty(BlockStateClayContainer.FACING, EnumFacing.NORTH)
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
    public TileGeneric createNewTileEntity(World worldIn, int meta) {
        if (this.teClass == null) return null;

        try {
            TileGeneric tecc = teClass.newInstance();
            if (tecc instanceof TileEntityClayContainer) {
                ((TileEntityClayContainer) tecc).initParamsByTier(this.tier);
            }
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
        BlockStateClayContainer.checkSurroundConnection(worldIn, pos, worldIn.getTileEntity(pos));

        openGui(worldIn, pos, playerIn);
        return true;
    }

    protected void openGui(int guiId, World world, BlockPos pos, EntityPlayer player) {
        player.openGui(ClayiumCore.instance(), guiId, world, pos.getX(), pos.getY(), pos.getZ());
    }

    protected void openGui(World world, BlockPos pos, EntityPlayer player) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer && world.getTileEntity(pos) instanceof TileGeneric && ((ClayContainer) block).guiId != -1)
            openGui(((ClayContainer) block).guiId, world, pos, player);
    }

    /**
     * Modifying Place States -> getStateForPlacement
     * Modifying Place Fields -> onBlockPlacedBy
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityClayContainer) {
            if (stack.hasDisplayName()) {
                ((TileEntityClayContainer) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return state.withProperty(BlockStateClayContainer.FACING, UtilDirection.getBetterFront(state, pos, placer));
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return new EnumFacing[0];
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote) return false;

        EnumFacing[] axes = getValidRotations(world, pos);
        if (axes == null || axes.length == 0) return false;
        EnumFacing candidacy = Arrays.stream(axes).anyMatch(_axis -> axis == _axis) ? axis : axes[0];

        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockStateClayContainer.FACING, candidacy));

        return true;
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

    /**
     * PFFF<br>
     * F: Facing, P: IsPipe
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        if (!(state instanceof BlockStateClayContainer)) return 0;

        int meta = 0;
        meta |= state.getValue(BlockStateClayContainer.IS_PIPE) ? 1 : 0;
        meta <<= 3;
        meta |= state.getValue(BlockStateClayContainer.FACING).getIndex();

        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (this.getDefaultState().getPropertyKeys().isEmpty()) return this.getDefaultState();

        return this.getDefaultState()
                .withProperty(BlockStateClayContainer.IS_PIPE, (meta >> 3) == 1)
                .withProperty(BlockStateClayContainer.FACING, EnumFacing.getFront(meta & 0b0111));
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
        public ClayContainerStateContainer(ClayContainer blockIn) {
            super(blockIn, BlockStateClayContainer.getPropertyList().toArray(new IProperty[0]));
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            if (block instanceof ClaySidedContainer)
                return new BlockStateClaySidedContainer(block, properties);
            if (block instanceof ClayDirectionalContainer)
                return new BlockStateClayDirectionalContainer(block, properties);
            return new BlockStateClayContainer(block, properties);
        }
    }
}