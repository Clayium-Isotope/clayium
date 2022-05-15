package mods.clayium.block;

import java.util.List;

import mods.clayium.block.tile.TileRedstoneInterface;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RedstoneInterface
        extends ClayContainerTiered {
    public RedstoneInterface(int tier) {
        super(Material.iron, (Class) TileRedstoneInterface.class, 0, tier);
        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }


    protected boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        world.markBlockForUpdate(x, y, z);

        TileRedstoneInterface te1 = (TileRedstoneInterface) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        String state = player.isSneaking() ? te1.getState() : te1.changeState();
        if (!world.isRemote) player.addChatMessage((IChatComponent) new ChatComponentText(state));
        return true;
    }

    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        setSameOverlayIcons(par1IconRegister.registerIcon("clayium:redstoneinterface"));
    }


    public boolean canProvidePower() {
        return true;
    }


    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int direction) {
        TileRedstoneInterface te = (TileRedstoneInterface) UtilBuilder.safeGetTileEntity(world, x, y, z);
        return te.getProvidingWeakPower();
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip.RedstoneInterface");
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
