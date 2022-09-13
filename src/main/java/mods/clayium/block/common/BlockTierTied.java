package mods.clayium.block.common;

import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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

    public Block get(TierPrefix key) {
        return super.get(key);
    }

    public Block get(int tier) throws IllegalAccessException {
        if (0 <= tier && tier <= 13)
            return super.get(TierPrefix.get(tier));

        throw new IllegalAccessException();
    }
}
