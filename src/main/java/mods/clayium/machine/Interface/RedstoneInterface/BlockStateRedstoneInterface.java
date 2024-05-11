package mods.clayium.machine.Interface.RedstoneInterface;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStateRedstoneInterface extends BlockStateContainer.StateImplementation {

    public static final PropertyEnum<EnumControlState> CONTROL_STATE = PropertyEnum.create("control_state",
            EnumControlState.class);

    protected BlockStateRedstoneInterface(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileEntityRedstoneInterface te = (TileEntityRedstoneInterface) blockAccess.getTileEntity(pos);
        return te.getProvidingWeakPower();
    }
}
