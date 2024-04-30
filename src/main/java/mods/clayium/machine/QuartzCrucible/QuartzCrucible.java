package mods.clayium.machine.QuartzCrucible;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

import com.google.common.collect.ImmutableMap;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilItemStack;

@HasOriginalState
public class QuartzCrucible extends ClayContainer {

    public QuartzCrucible() {
        super(Material.GLASS, TileEntityQuartzCrucible::new, "quartz_crucible", -1, TierPrefix.advanced);
        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.2f);
        this.setResistance(0.2f);
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateQuartzCrucible.LEVEL, 0));
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityItem) {
            ItemStack itemStack = ((EntityItem) entity).getItem();
            ItemStack ingot = ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot);
            ItemStack string = new ItemStack(Items.STRING);
            TileEntityQuartzCrucible tile = (TileEntityQuartzCrucible) worldIn.getTileEntity(pos);
            assert tile != null;
            if (entity.posY - (double) pos.getY() < 0.20000000298023224D) {
                if (UtilItemStack.areItemDamageTagEqual(itemStack, ingot) && tile.putIngot()) {
                    itemStack.shrink(1);
                    if (itemStack.getCount() <= 0) {
                        entity.setDead();
                    }
                }

                if (UtilItemStack.areItemDamageTagEqual(itemStack, string) && tile.consumeString()) {
                    itemStack.shrink(1);
                    if (itemStack.getCount() <= 0) {
                        entity.setDead();
                    }
                }
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new QuartzCrucibleStateContainer(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateQuartzCrucible.LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockStateQuartzCrucible.LEVEL, meta);
    }

    @Override
    public boolean canBePipe() {
        return false;
    }

    private static class QuartzCrucibleStateContainer extends BlockStateContainer {

        public QuartzCrucibleStateContainer(Block blockIn) {
            super(blockIn, BlockStateQuartzCrucible.LEVEL);
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
                                                  @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new BlockStateQuartzCrucible(block, properties);
        }
    }
}
