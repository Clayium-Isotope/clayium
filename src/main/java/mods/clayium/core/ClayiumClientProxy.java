package mods.clayium.core;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.SiliconeColored;
import mods.clayium.item.ClayiumMaterials;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClayiumClientProxy implements IClayiumProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		OBJLoader.INSTANCE.addDomain("clayium");
	}

	@Override
	public void init(FMLInitializationEvent event) {
		registerColors();
	}

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

	@SideOnly(Side.CLIENT)
	public void registerColors() {
		ClayiumMaterials.requestTint(Minecraft.getMinecraft().getItemColors());

		for (Block coloredSilicone : ClayiumBlocks.siliconeColored) {
			if (coloredSilicone instanceof SiliconeColored) {
				Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockColor) coloredSilicone, coloredSilicone);
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler((IItemColor) coloredSilicone, coloredSilicone);
			}
		}
	}
}
