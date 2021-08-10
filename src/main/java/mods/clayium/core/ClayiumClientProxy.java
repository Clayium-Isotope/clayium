package mods.clayium.core;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.tile.TileClayWorkTable;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	@Override
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileClayWorkTable.class, ClayiumBlocks.clayWorkTable.getRegistryName());
	}
}
