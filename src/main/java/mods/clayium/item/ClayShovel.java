package mods.clayium.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.CBlocks;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class ClayShovel
        extends ItemSpade {
    protected float efficiencyOnClayBlocks = 32.0F;
    private float efficiencyOnClayOre = 12.0F;

    public ClayShovel() {
        super(ToolMaterial.WOOD);
        setMaxDamage(500);
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setUnlocalizedName("itemClayShovel");
        setTextureName("clayium:clayshovel");
    }


    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (block.getMaterial() == Material.clay) {
            return this.efficiencyOnClayBlocks;
        }
        if (block == CBlocks.blockClayOre) {
            return this.efficiencyOnClayOre;
        }
        if (ForgeHooks.isToolEffective(stack, block, meta)) {
            return this.efficiencyOnProperMaterial;
        }
        return super.getDigSpeed(stack, block, meta);
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemstack, player, list, flag);
        List alist = UtilLocale.localizeTooltip(getUnlocalizedName(itemstack) + ".tooltip");
        if (alist != null)
            list.addAll(alist);
    }
}
