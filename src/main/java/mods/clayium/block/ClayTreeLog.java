package mods.clayium.block;

import mods.clayium.block.common.ITieredBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ClayTreeLog extends BlockLog implements ITieredBlock {
    public ClayTreeLog() {
        setTranslationKey("clay_tree_log");
        setRegistryName(ClayiumCore.ModId, "clay_tree_log");
        setCreativeTab(ClayiumCore.tabClayium);
        setHardness(1.5f);
        setSoundType(SoundType.SAND);

        setDefaultState(this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    public TierPrefix getTier(ItemStack stackIn) {
        return TierPrefix.claySteel;
    }

    @Override
    public TierPrefix getTier(IBlockAccess access, BlockPos posIn) {
        return TierPrefix.claySteel;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(LOG_AXIS).build();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta & 12)
        {
            case 0:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        switch (state.getValue(LOG_AXIS))
        {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }

        return i;
    }
}
