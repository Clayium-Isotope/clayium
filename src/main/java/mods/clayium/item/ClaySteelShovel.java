package mods.clayium.item;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilAdvancedTools;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ClaySteelShovel extends ItemSpade implements IAdvancedTool {
    private IHarvestCoord harvestCoord;

    public ClaySteelShovel() {
        super(ClayiumCore.CLAY_STEEL);
        setMaxDamage(10000);

        setUnlocalizedName("clay_steel_shovel");
        setRegistryName(ClayiumCore.ModId, "clay_steel_shovel");
        setCreativeTab(ClayiumCore.tabClayium);

        harvestCoord = new HarvestCoordClaySteelTools(ClayiumConfiguration.cfgClaySteelPickaxeRange);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return super.getDestroySpeed(stack, state) * 6.0F;
    }

    public IHarvestCoord getHarvestCoord() {
        return harvestCoord;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        stack.damageItem(UtilAdvancedTools.onBlockDestroyed(stack, worldIn, state.getBlock(), pos, entityLiving), entityLiving);
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return getHarvestCoord().onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, getUnlocalizedName());
    }
}
