package mods.clayium.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.CBlocks;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class ClayPickaxe
        extends ItemPickaxe {
    private float efficiencyOnClayOre = 32.0F;

    public ClayPickaxe() {
        super(Item.ToolMaterial.STONE);
        setMaxDamage(500);
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setUnlocalizedName("itemClayPickaxe");
        setTextureName("clayium:claypickaxe");
    }


    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (block == CBlocks.blockClayOre) {
            return (block.getHarvestLevel(meta) <= stack.getItem().getHarvestLevel(stack, block.getHarvestTool(meta))) ? this.efficiencyOnClayOre : (this.efficiencyOnClayOre * 100.0F / 30.0F);
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
