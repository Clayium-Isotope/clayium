package mods.clayium.core;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClayiumClientProxy implements IClayiumProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		OBJLoader.INSTANCE.addDomain("clayium");
	}

	@Override
	public void init(FMLInitializationEvent event) {}

	@Override
	public void postInit(FMLPostInitializationEvent event) {}

	@Override
	public void serverLoad(FMLServerStartingEvent event) {}

	public EnumFacing getHittingSide(EntityPlayer player) {
		return Minecraft.getMinecraft().getRenderViewEntity().rayTrace(9999.0D, 0.0F).sideHit;
	}

	public EntityPlayer getClientPlayer() {
		return (Minecraft.getMinecraft()).player;
	}
}
