package mods.clayium.machine.ClayiumMachine;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClaySidedContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClayiumMachine extends ClaySidedContainer {
    private final EnumMachineKind machineKind;

    public ClayiumMachine(EnumMachineKind kind, String suffix, int tier) {
        super(Material.IRON, TileEntityClayiumMachine.class,
                suffix.isEmpty()
                        ? TierPrefix.get(tier).getPrefix() + "_" + kind.getRegisterName()
                        : kind.getRegisterName() + "_" + suffix,
                GuiHandler.clayBendingMachineGuiID, tier);
        this.machineKind = kind;
    }

    public ClayiumMachine(EnumMachineKind kind, int tier) {
        this(kind, "", tier);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityClayiumMachine tecm = new TileEntityClayiumMachine();
        tecm.initByTier(this.tier);
        tecm.setKind(this.machineKind);
        return tecm;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState east = worldIn.getBlockState(pos.east());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            EnumFacing face = state.getValue(ClaySidedContainer.ClaySidedContainerState.FACING);

            if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) face = EnumFacing.SOUTH;
            else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) face = EnumFacing.NORTH;
            else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) face = EnumFacing.WEST;
            else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) face = EnumFacing.EAST;

            worldIn.setBlockState(pos, state.withProperty(ClaySidedContainer.ClaySidedContainerState.FACING, face), 2);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(ClaySidedContainer.ClaySidedContainerState.FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(ClaySidedContainer.ClaySidedContainerState.FACING, placer.getHorizontalFacing().getOpposite()), 3);

        if (worldIn.getTileEntity(pos) instanceof TileEntityClayiumMachine) {
            TileEntityClayiumMachine tecm = (TileEntityClayiumMachine) worldIn.getTileEntity(pos);
            tecm.importRoutes.replace(EnumFacing.UP, 0);
            tecm.importRoutes.replace(placer.getHorizontalFacing(), -2);
            tecm.exportRoutes.replace(EnumFacing.DOWN, 0);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing face = EnumFacing.getFront(meta);
        if (face.getAxis() == EnumFacing.Axis.Y) face = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(ClaySidedContainer.ClaySidedContainerState.FACING, face);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ClaySidedContainer.ClaySidedContainerState.FACING).getIndex();
    }

    public static void updateBlockState(World world, BlockPos pos) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public EnumMachineKind getMachineKind() {
        return machineKind;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        UtilLocale.localizeTooltip(tooltip, machineKind.getRegisterName());
    }
}
