package mods.clayium.block.common;

import mods.clayium.util.UsedFor;
import net.minecraft.item.ItemBlock;

@UsedFor(UsedFor.Type.Block)
public interface IItemBlockHolder {
    ItemBlock getItemBlock();
}
