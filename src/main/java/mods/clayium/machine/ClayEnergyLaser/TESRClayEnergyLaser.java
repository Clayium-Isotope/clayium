package mods.clayium.machine.ClayEnergyLaser;

import mods.clayium.machine.ClayContainer.TESRClayContainer;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserManager;
import mods.clayium.machine.ClayEnergyLaser.laser.RenderClayLaser;

public class TESRClayEnergyLaser extends TESRClayContainer {
    @Override
    public void render(TileEntityClayContainer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (te instanceof IClayLaserManager)
            RenderClayLaser.render((IClayLaserManager) te, x, y, z);
    }

    @Override
    public boolean isGlobalRenderer(TileEntityClayContainer te) {
        return true;
    }
}
