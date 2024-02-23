package mods.clayium.network;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.common.IItemBlockHolder;
import mods.clayium.block.itemblock.ItemBlockMachine;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

@Mod.EventBusSubscriber
public class RegistryEventSubscriber {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ClayiumBlocks.getBlocks().toArray(new Block[0]));
        event.getRegistry().registerAll(ClayiumMachines.getBlocks().toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Block block : ClayiumBlocks.getBlocks()) {
            assert block.getRegistryName() != null;
            Item item = block instanceof IItemBlockHolder ? ((IItemBlockHolder) block).getItemBlock() : new ItemBlock(block);
            event.getRegistry().register(item.setRegistryName(block.getRegistryName()));
        }

        for (Map.Entry<EnumMachineKind, Map<TierPrefix, Block>> entry : ClayiumMachines.machineMap.entrySet()) {
            for (Map.Entry<TierPrefix, Block> entry1 : entry.getValue().entrySet()) {
                event.getRegistry().register(new ItemBlockMachine(entry.getKey(), entry1.getKey(), entry1.getValue()));
            }
        }

        event.getRegistry().registerAll(ClayiumItems.getItems().toArray(new Item[0]));
        event.getRegistry().registerAll(ClayiumMaterials.getItems().toArray(new Item[0]));
    }
}
