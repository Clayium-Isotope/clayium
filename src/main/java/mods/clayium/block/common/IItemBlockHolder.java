package mods.clayium.block.common;

import net.minecraft.item.ItemBlock;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.Block)
public interface IItemBlockHolder {

    ItemBlock getItemBlock();
}
