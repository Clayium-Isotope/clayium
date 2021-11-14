package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileVacuumContainer;
import mods.clayium.gui.TextureExtra;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class VacuumContainer
        extends ClayContainer implements ITieredBlock {
    public VacuumContainer(Material material, String iconstr) {
        super(material, (Class) TileVacuumContainer.class, iconstr, 1);
        this.guiId = 22;

        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);


        this
                .BackIcon = this.LeftIcon = this.RightIcon = (new TextureExtra("clayium:vacuumcontainerside_", new String[] {this.machineIconStr, "clayium:vacuumcontainerside"})).register(par1IconRegister);
        this.FrontIcon = (new TextureExtra("clayium:vacuumcontainer_", new String[] {this.machineIconStr, "clayium:vacuumcontainer"})).register(par1IconRegister);
        this.UpIcon = (new TextureExtra("clayium:vacuumcontainertop_", new String[] {this.machineIconStr, "clayium:vacuumcontainertop"})).register(par1IconRegister);
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }

    public int getTier(ItemStack itemstack) {
        return 6;
    }

    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 6;
    }
}
