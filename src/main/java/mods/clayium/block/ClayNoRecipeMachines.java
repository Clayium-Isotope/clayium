package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayContainerTiered;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClayNoRecipeMachines
        extends ClayContainerTiered {
    protected String iconstr;
    private String guititle;

    public ClayNoRecipeMachines(String guititle, String iconstr, String machineIconStr, int tier, Class<? extends TileClayContainer> tileEntityClass, int metaMode) {
        this(guititle, iconstr, machineIconStr, tier, tileEntityClass, 1, metaMode);
    }

    public ClayNoRecipeMachines(String guititle, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int metaMode) {
        this(guititle, iconstr, tier, tileEntityClass, 1, metaMode);
    }

    public ClayNoRecipeMachines(String guititle, String iconstr, String machineIconStr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId, int metaMode) {
        super(Material.iron, (Class) tileEntityClass, machineIconStr, guiId, metaMode, tier);
        initClayNoRecipeMachines(iconstr, guititle);
    }

    public ClayNoRecipeMachines(String guititle, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId, int metaMode) {
        super(Material.iron, (Class) tileEntityClass, guiId, metaMode, tier);
        initClayNoRecipeMachines(iconstr, guititle);
    }

    private void initClayNoRecipeMachines(String iconstr, String guititle) {
        this.iconstr = iconstr;
        this.guititle = guititle;


        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }


    public TileEntity createNewTileEntity(World world, int par2) {
        TileClayContainerTiered tile = (TileClayContainerTiered) super.createNewTileEntity(world, par2);
        tile.containerName(this.guititle);

        return (TileEntity) tile;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);


        if (this.iconstr != null && !this.iconstr.equals("")) {
            this.FrontOverlayIcon = par1IconRegister.registerIcon(this.iconstr);
        }
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import", "import_energy"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
