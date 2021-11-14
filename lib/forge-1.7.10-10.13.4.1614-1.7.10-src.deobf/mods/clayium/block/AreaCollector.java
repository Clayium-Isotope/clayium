package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileAreaCollector;
import net.minecraft.client.renderer.texture.IIconRegister;

public class AreaCollector
        extends AreaMiner {
    public AreaCollector(int tier) {
        super(tier, "clayium:areacollector", (Class) TileAreaCollector.class, 16);
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_energy"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }
}
