package mods.clayium.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;

public class ClayShovel extends ItemSpade {

    public ClayShovel() {
        super(ToolMaterial.WOOD);
        this.attackSpeed = -3f;
        setMaxDamage(500);
        setTranslationKey("clay_shovel");
        setRegistryName(ClayiumCore.ModId, "clay_shovel");
        setCreativeTab(ClayiumCore.tabClayium);
        setHarvestLevel("spade", 1);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (state.getMaterial() == Material.CLAY)
            return efficiencyOnClayBlocks;
        if (state.getBlock() == ClayiumBlocks.clayOre || state.getBlock() == ClayiumBlocks.denseClayOre ||
                state.getBlock() == ClayiumBlocks.largeDenseClayOre)
            return efficiencyOnClayOre;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        UtilLocale.localizeTooltip(tooltip, getTranslationKey());
    }

    protected float efficiencyOnClayBlocks = 32F;
    private float efficiencyOnClayOre = 12F;
}
