package mods.clayium.util.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedList<E>
        extends ArrayList<WeightedResult<E>> {
    public boolean add(E result, int weight) {
        return add(new WeightedResult<E>(result, weight));
    }

    public List<E> getResultList() {
        List<E> list = new ArrayList();
        for (int i = 0; i < size(); i++) {
            list.add((get(i)).result);
        }
        return list;
    }

    public int getWeightSum() {
        int sum = 0;
        for (int i = 0; i < size(); i++) {
            sum += (get(i)).weight;
        }
        return sum;
    }

    public E put(Random random) {
        int sum = getWeightSum();
        if (sum <= 0) {
            return (get(random.nextInt(size()))).result;
        }
        int pos = random.nextInt(sum);
        sum = 0;
        for (int i = 0; i < size(); i++) {
            sum += (get(i)).weight;
            if (sum > pos)
                return (get(i)).result;
        }
        return null;
    }
}


