package mods.clayium.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilAdvancedTools;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ClaySteelShovel extends ItemSpade implements IAdvancedTool {
    private IHarvestCoord harvestCoord;

    public ClaySteelShovel() {
        super(ToolMaterial.EMERALD);
        setMaxDamage(10000);
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setUnlocalizedName("itemClaySteelShovel");
        setTextureName("clayium:claysteelshovel");

        this.harvestCoord = new HarvestCoordClaySteelTools(ClayiumCore.cfgClaySteelPickaxeRange);
    }


    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return super.getDigSpeed(stack, block, meta) * 6.0F;
    }


    public IHarvestCoord getHarvestCoord() {
        return this.harvestCoord;
    }


    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        boolean res = super.onBlockDestroyed(itemstack, world, block, x, y, z, entity);
        itemstack.damageItem(UtilAdvancedTools.onBlockDestroyed(itemstack, world, block, x, y, z, entity), entity);
        return res;
    }


    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        return getHarvestCoord().onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemstack, player, list, flag);
        List alist = UtilLocale.localizeTooltip(getUnlocalizedName(itemstack) + ".tooltip");
        if (alist != null)
            list.addAll(alist);
    }
}
