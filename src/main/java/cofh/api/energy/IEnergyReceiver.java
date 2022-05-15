package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyReceiver extends IEnergyConnection {
  int receiveEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);
  
  int getEnergyStored(ForgeDirection paramForgeDirection);
  
  int getMaxEnergyStored(ForgeDirection paramForgeDirection);
}


