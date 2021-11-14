package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyProvider extends IEnergyConnection {
  int extractEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);
  
  int getEnergyStored(ForgeDirection paramForgeDirection);
  
  int getMaxEnergyStored(ForgeDirection paramForgeDirection);
}


