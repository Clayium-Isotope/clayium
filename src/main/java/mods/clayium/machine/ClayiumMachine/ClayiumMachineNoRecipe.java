package mods.clayium.machine.ClayiumMachine;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.SoundType;
import net.minecraft.world.World;

public class ClayiumMachineNoRecipe extends ClayiumMachine {
    private String guititle;

    public ClayiumMachineNoRecipe(String guititle, EnumMachineKind kind, int tier, Class<? extends TileEntityClayContainer> teClass, int guiID) {
        super(kind, "", tier, teClass, guiID);

        this.guititle = guititle;

        setSoundType(SoundType.METAL);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileGeneric createNewTileEntity(World worldIn, int meta) {
        TileGeneric te = super.createNewTileEntity(worldIn, meta);
        if (te instanceof TileEntityClayContainer)
            ((TileEntityClayContainer) te).setCustomName(this.guititle);
        return te;
    }
}
