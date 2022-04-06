package mods.clayium.block.common;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ClayiumBlock extends Block implements IItemBlockHolder {
    public ClayiumBlock(Material material, String modelPath) {
        this(material, modelPath, material.getMaterialMapColor());
    }

    public ClayiumBlock(Material material, String modelPath, MapColor mapColor) {
        super(material, mapColor);
        setUnlocalizedName(modelPath);
        setRegistryName(ClayiumCore.ModId, modelPath);
        setCreativeTab(ClayiumCore.tabClayium);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, this.getUnlocalizedName());
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlock(this);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
//        return new ItemStack(getItemBlock());
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
//        return getItemBlock();
    }
}
