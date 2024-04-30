package mods.clayium.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClayiumCommonProxy implements IClayiumProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {}

    @Override
    public void init(FMLInitializationEvent event) {}

    @Override
    public void postInit(FMLPostInitializationEvent event) {}

    @Override
    public void serverLoad(FMLServerStartingEvent event) {}

    public EnumFacing getHittingSide(EntityPlayer player) {
        return ForgeHooks.rayTraceEyes(player, 9999.0d).sideHit;
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    @Override
    public void registerTileEntity() {}

    @Override
    public void updateFlightStatus(int mode) {}

    @Override
    public void overclockPlayer(int delay) {}
}
