package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class ClayMachines
        extends ClayContainerTiered {
    protected String iconstr;
    protected String recipeId;
    protected String guititle;
    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;

    public ClayMachines(String iconstr, int tier) {
        this(iconstr, tier, (Class) TileClayMachines.class, 1);
    }

    public ClayMachines(String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId) {
        this(iconstr, tier, tileEntityClass, guiId, 1);
    }

    public ClayMachines(String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId, int metaMode) {
        this((String) null, iconstr, tier, tileEntityClass, guiId, metaMode);
    }

    public ClayMachines(String guititle, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId, int metaMode) {
        this((String) null, guititle, iconstr, tier, tileEntityClass, guiId, metaMode);
    }

    public ClayMachines(String recipeId, String iconstr, int tier) {
        this(recipeId, iconstr, tier, (Class) TileClayMachines.class);
    }

    public ClayMachines(String recipeId, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass) {
        this(recipeId, iconstr, tier, tileEntityClass, 1);
    }

    public ClayMachines(String recipeId, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId) {
        this(recipeId, (String) null, iconstr, tier, tileEntityClass, guiId, 1);
    }

    public ClayMachines(String recipeId, String guititle, String iconstr, int tier, Class<? extends TileClayContainer> tileEntityClass, int guiId, int metaMode) {
        super(Material.iron, (Class) tileEntityClass, metaMode, tier);
        this.iconstr = iconstr;
        this.guititle = guititle;

        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
        this.recipeId = recipeId;
        this.guiId = guiId;
    }


    public TileEntity createNewTileEntity(World world, int par2) {
        TileClayMachines tile = (TileClayMachines) super.createNewTileEntity(world, par2);

        tile.setRecipe(this.recipeId);


        tile.containerName(this.guititle);

        tile.initCraftTime = this.multCraftTime;
        tile.initConsumingEnergy = this.multConsumingEnergy;
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


    public String getRecipeId() {
        return this.recipeId;
    }


    public List getTooltip(ItemStack itemStack) {
        List ret = UtilLocale.localizeTooltip("tooltip." + this.recipeId);
        ret.addAll(super.getTooltip(itemStack));
        return ret;
    }
}
