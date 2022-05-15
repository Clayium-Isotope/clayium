package mods.clayium.block;

import java.util.ArrayList;
import java.util.Random;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ClayOre
        extends BlockDamaged {
    private Random random = new Random();

    public ClayOre() {
        super(Material.rock, 3);
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 1);
    }


    public Item getItemDropped(int meta, Random random, int fortune) {
        return (meta == 0) ? Items.clay_ball : Item.getItemFromBlock(this);
    }


    public int damageDropped(int damage) {
        return (damage == 0) ? 0 : damage;
    }


    public int quantityDropped(Random random) {
        return ClayiumCore.multiplyProgressionRateI(4 + random.nextInt(5) * random.nextInt(4));
    }


    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && Item.getItemFromBlock(this) != getItemDropped(0, random, fortune)) {
            int i = random.nextInt(fortune + 2) - 1;
            if (i < 0) {
                i = 0;
            }
            return quantityDropped(random) * (i + 1);
        }
        return quantityDropped(random);
    }


    public int quantityDropped(int meta, int fortune, Random random) {
        return (meta == 0) ? quantityDroppedWithBonus(fortune, random) : 1;
    }


    public int getExpDrop(IBlockAccess iBlockAccess, int meta, int fortune) {
        return (meta == 0) ? MathHelper.getRandomIntegerInRange(this.random, 0, 1) : 0;
    }


    public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
        p_149636_2_.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        p_149636_2_.addExhaustion(0.025F);

        if (canSilkHarvest(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_) && EnchantmentHelper.getSilkTouchModifier((EntityLivingBase) p_149636_2_)) {

            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = createStackedBlock(p_149636_6_);

            if (itemstack != null) {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, p_149636_1_, this, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, 0, 1.0F, true, p_149636_2_);
            for (ItemStack is : items) {
                dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, is);
            }
        } else {

            this.harvesters.set(p_149636_2_);
            int i1 = EnchantmentHelper.getFortuneModifier((EntityLivingBase) p_149636_2_);
            if (p_149636_2_.getHeldItem() != null) {
                if (p_149636_2_.getHeldItem().getItem() == CItems.itemClayShovel) {
                    i1 = (i1 + 1) * 3;
                }
                if (p_149636_2_.getHeldItem().getItem() == CItems.itemClayPickaxe) {
                    i1 = (i1 + 1) * 4;
                }
            }
            dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, i1);
            this.harvesters.set(null);
        }
    }


    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return (super.canHarvestBlock(player, meta) || (player.getHeldItem() != null && (player.getHeldItem().getItem() == CItems.itemClayShovel || player.getHeldItem().getItem() == CItems.itemClayPickaxe)));
    }
}
