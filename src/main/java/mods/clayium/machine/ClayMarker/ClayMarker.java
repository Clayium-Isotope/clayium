package mods.clayium.machine.ClayMarker;

import com.google.common.collect.ImmutableMap;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.client.render.HasOriginalState;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@HasOriginalState
public class ClayMarker extends BlockContainer implements ITieredBlock {
    protected final TierPrefix tier;
    protected final Supplier<? extends TileEntity> teSupplier;
    protected final MarkerExtent extentMode;

    public ClayMarker(String modelPath, TierPrefix tier, Supplier<? extends TileEntity> teSupplier, MarkerExtent extentMode) {
        super(Material.CLAY);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5f);
        this.setResistance(5.0f);
        this.setHarvestLevel("shovel", 0);

        setUnlocalizedName(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);

        this.setDefaultState(this.getDefaultState().withProperty(AABBHolder.APPEARANCE, AABBHolder.Appearance._0));

        this.tier = tier;
        this.teSupplier = teSupplier;
        this.extentMode = extentMode;
    }

    @Override
    public TierPrefix getTier(ItemStack stackIn) {
        return this.tier;
    }

    @Override
    public TierPrefix getTier(IBlockAccess access, BlockPos posIn) {
        return this.tier;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//        world.func_147453_f(x, y, z, block);
        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return this.teSupplier.get();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityClayMarker te = (TileEntityClayMarker) worldIn.getTileEntity(pos);
        if (te == null) return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);

        te.activate(worldIn, pos, state, this.extentMode);
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(UtilLocale.localizeAndFormat(this.getUnlocalizedName() + ".tooltip"));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerClayMarker(this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AABBHolder.APPEARANCE, AABBHolder.Appearance.fromMeta((byte) meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AABBHolder.APPEARANCE).toMeta();
    }

    private static class BlockStateContainerClayMarker extends BlockStateContainer {
        public BlockStateContainerClayMarker(Block blockIn) {
            super(blockIn, AABBHolder.APPEARANCE);
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new BlockStateClayMarker(block, properties);
        }
    }
}
