package mods.clayium.util.crafting;

import net.minecraft.util.WeightedRandom;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedList<E> {
    private final List<WeightedList.WeightedResult<E>> internal = new ArrayList<>();

    public WeightedList() {}

    public boolean add(E result, int weight) {
        return this.internal.add(new WeightedResult<>(result, weight));
    }

    @Nullable
    public E get(Random random) {
        return WeightedRandom.getRandomItem(random, this.internal).result;
    }

    public E get(int index) {
        return WeightedRandom.getRandomItem(this.internal, index).result;
    }

    static class WeightedResult<E> extends WeightedRandom.Item {
        public E result;

        public WeightedResult(E result, int weight) {
            super(weight);

            this.result = result;
        }
    }
}
