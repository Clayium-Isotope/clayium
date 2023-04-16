package mods.clayium.util.crafting;

import net.minecraft.util.WeightedRandom;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedList<E> {
    private final List<WeightedList.WeightedResult<E>> internal = new ArrayList<>();
    private final E flat;

    public WeightedList(E flat) {
        this.flat = flat;
    }

    public boolean add(E result, int weight) {
        return this.internal.add(new WeightedResult<>(result, weight));
    }

    @Nullable
    public E get(Random random) {
        WeightedResult<E> wResult = WeightedRandom.getRandomItem(random, this.internal);

        if (wResult == null) return flat;

        return wResult.result;
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
