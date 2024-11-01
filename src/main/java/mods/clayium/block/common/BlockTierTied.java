package mods.clayium.block.common;

import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BlockTierTied extends EnumMap<TierPrefix, Block> {

    public BlockTierTied() {
        super(TierPrefix.class);
    }

    public List<Block> entryList() {
        List<Block> res = new ArrayList<>();

        for (Entry<TierPrefix, Block> entry : this.entrySet()) {
            res.add(entry.getValue());
        }

        return res;
    }

    public ItemStack get(TierPrefix key) {
        return new ItemStack(super.get(key));
    }

    public ItemStack get(TierPrefix key, int amount) {
        return new ItemStack(super.get(key), amount);
    }

    public Block get(int tier) throws IllegalAccessException {
        if (0 <= tier && tier <= 13)
            return super.get(TierPrefix.get(tier));

        throw new IllegalAccessException();
    }

    public boolean contains(Block block) {
        return this.containsValue(block);
    }

    public TierPrefix getTier(Block value) {
        for (Map.Entry<TierPrefix, Block> entry : this.entrySet()) {
            if (Block.isEqualTo(value, entry.getValue())) return entry.getKey();
        }

        return TierPrefix.none;
    }
}
