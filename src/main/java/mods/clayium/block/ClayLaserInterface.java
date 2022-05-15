package mods.clayium.block;

import java.util.List;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayLaserInterface;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ClayLaserInterface
        extends ClayContainerTiered {
    protected String iconstr;

    public ClayLaserInterface(int tier) {
        this("clayium:laserinterface", TileClayLaserInterface.class, tier);
    }


    public ClayLaserInterface(String iconstr, Class<? extends TileClayLaserInterface> tileEntityClass, int tier) {
        super(Material.iron, (Class) tileEntityClass, 2, tier);
        this.iconstr = iconstr;
        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }


    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        TileClayContainer te = (TileClayContainer) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te.isUsable(itemStack, player, side, hitX, hitY, hitZ)) {
            te.useItem(itemStack, player, side, hitX, hitY, hitZ);
        }


        return false;
    }

    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);


        this.FrontOverlayIcon = par1IconRegister.registerIcon(this.iconstr);
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.LaserInterface");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
