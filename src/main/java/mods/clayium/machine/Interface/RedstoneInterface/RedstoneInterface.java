package mods.clayium.machine.Interface.RedstoneInterface;

import com.google.common.collect.ImmutableMap;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.client.render.HasOriginalState;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.Interface.ClayInterface.ClayInterface;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.Optional;

@HasOriginalState
public class RedstoneInterface extends ClayInterface implements ITieredBlock {

    public RedstoneInterface(TierPrefix tier) {
        super(EnumMachineKind.redstoneInterface, TileEntityRedstoneInterface::new, tier);

        setDefaultState(
                this.getDefaultState().withProperty(BlockStateRedstoneInterface.CONTROL_STATE, EnumControlState.None));

        // JsonHelper.genItemJsonSimple(TierPrefix.get(tier).getPrefix() + "_" + this.kind.getRegisterName());
        // JsonHelper.genStateJsonSimple(TierPrefix.get(tier).getPrefix() + "_" + this.kind.getRegisterName());
    }

    @Nullable
    @Override
    public TileEntityGeneric createNewTileEntity(World worldIn, int meta) {
        TileEntityRedstoneInterface teri = new TileEntityRedstoneInterface();
        teri.initParamsByTier(this.tier);
        return teri;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        TileEntityRedstoneInterface te1 = (TileEntityRedstoneInterface) worldIn.getTileEntity(pos);
        EnumControlState controlState = playerIn.isSneaking() ? te1.getState() : te1.changeState();
        if (!worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString(controlState.getName()));
        }

        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateRedstoneInterface.CONTROL_STATE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockStateRedstoneInterface.CONTROL_STATE,
                EnumControlState.values()[meta]);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new RedstoneInterfaceStateContainer(this);
    }

    private static class RedstoneInterfaceStateContainer extends BlockStateContainer {

        public RedstoneInterfaceStateContainer(Block blockIn) {
            super(blockIn, BlockStateRedstoneInterface.CONTROL_STATE);
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
                                                  @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new BlockStateRedstoneInterface(block, properties);
        }
    }
}
