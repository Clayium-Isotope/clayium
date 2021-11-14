package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileClayEnergyLaser;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClayEnergyLaser extends ClayNoRecipeMachines {
    public ClayEnergyLaser(int tier) {
        super((String) null, "clayium:energylaser", tier, (Class) TileClayEnergyLaser.class, 2);
        this.guiId = 13;
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_energy"});
        registerExtractIcons(par1IconRegister, new String[0]);
    }


    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
        super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
        updatePower(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
    }


    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        updatePower(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    public void updatePower(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_) {
        if (!p_149695_1_.isRemote) {
            TileEntity tile = UtilBuilder.safeGetTileEntity((IBlockAccess) p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
            if (tile instanceof TileClayEnergyLaser) {
                ((TileClayEnergyLaser) tile).setPowered(ClayiumCore.cfgInverseClayLaserRSCondition ? (!p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_)) : p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_));
            }
        }
    }

    public List getTooltip(ItemStack itemStack) {
        List<String> ret = UtilLocale.localizeTooltip("tooltip.ClayEnergyLaser");
        ret.addAll(super.getTooltip(itemStack));
        if (UtilLocale.canLocalize("tooltip.ClayEnergyLaser.energyConsumption")) {
            int e = 0;
            switch (this.tier) {
                case 7:
                    e = TileClayEnergyLaser.consumingEnergyBlue;
                    break;
                case 8:
                    e = TileClayEnergyLaser.consumingEnergyGreen;
                    break;
                case 9:
                    e = TileClayEnergyLaser.consumingEnergyRed;
                    break;
                case 10:
                    e = TileClayEnergyLaser.consumingEnergyWhite;
                    break;
            }

            ret.add(UtilLocale.localizeAndFormat("tooltip.ClayEnergyLaser.energyConsumption", new Object[] {UtilLocale.ClayEnergyNumeral(e)}));
        }
        return ret;
    }
}
