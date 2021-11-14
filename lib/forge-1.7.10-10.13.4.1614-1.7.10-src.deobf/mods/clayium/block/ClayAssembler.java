package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileClayAssembler;
import mods.clayium.block.tile.TileClayContainer;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ClayAssembler
        extends ClayMachines {
    public ClayAssembler(String recipeId, String iconstr, int tier) {
        this(recipeId, iconstr, tier, (Class) TileClayAssembler.class);
    }

    public ClayAssembler(String recipeId, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass) {
        this(recipeId, (String) null, iconstr, tier, tileEntityClass, 2, 1);
    }

    public ClayAssembler(String recipeId, String guititle, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId, int metaMode) {
        super(recipeId, guititle, iconstr, tier, tileEntityClass, guiId, metaMode);
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_1", "import_2", "import", "import_energy"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
