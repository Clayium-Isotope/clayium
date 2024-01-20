package mods.clayium.network.client;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.client.render.HasOriginalState;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumShapedMaterial;
import mods.clayium.machine.ClayiumMachines;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ModelRegistryEventSubscriber {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : ClayiumBlocks.getBlocks()) {
            assert block.getRegistryName() != null;
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                    new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }

        for (Block block : ClayiumMachines.getBlocks()) {
            assert block.getRegistryName() != null;
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                    new ModelResourceLocation(ClayiumCore.ModId + ":machine/" + block.getRegistryName().getResourcePath(), "inventory"));
            // Custom blockstate mapping
            if (!block.getClass().isAnnotationPresent(HasOriginalState.class)) {
                ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                    @Override
                    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                        return new ModelResourceLocation(ClayiumCore.ModId + ":empty");
                    }
                });
            }
        }

        for (Item item : ClayiumItems.getItems()) {
            assert item.getRegistryName() != null;
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

        for (ItemStack stack : ClayiumMaterials.getMaterials()) {
            assert stack.getItem().getRegistryName() != null;

            if (stack.getItem() instanceof ClayiumShapedMaterial) {
                if (((ClayiumShapedMaterial) stack.getItem()).useGeneralIcon())
                    ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                            new ModelResourceLocation(ClayiumCore.ModId + ":material/" + ((ClayiumShapedMaterial) stack.getItem()).getTempFile(), "inventory"));
                else
                    ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                            new ModelResourceLocation(ClayiumCore.ModId + ":material/" + stack.getItem().getRegistryName().getResourcePath(), "inventory"));
            }
        }

        for (ItemStack stack : ClayiumMaterials.clayParts.values()) {
            assert stack.getItem().getRegistryName() != null;
            if (stack.getItem() instanceof ClayiumShapedMaterial)
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                        new ModelResourceLocation(ClayiumCore.ModId + ":part/" + stack.getItem().getRegistryName().getResourcePath(), "inventory"));
        }

        for (ItemStack stack : ClayiumMaterials.denseClayParts.values()) {
            assert stack.getItem().getRegistryName() != null;
            if (stack.getItem() instanceof ClayiumShapedMaterial)
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), 0,
                        new ModelResourceLocation(ClayiumCore.ModId + ":part/" + stack.getItem().getRegistryName().getResourcePath(), "inventory"));
        }
    }
}
