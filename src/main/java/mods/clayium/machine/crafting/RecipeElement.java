package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RecipeElement {
    public static final RecipeElement FLAT = new RecipeElement(ItemStack.EMPTY, 0, 0, ItemStack.EMPTY, 0, 0);

    public RecipeElement(ItemStack materialIn, int method, int tier, ItemStack resultIn, long energy, long time) {
        this(Arrays.asList(materialIn), method, tier, Arrays.asList(resultIn), energy, time);
    }

    public RecipeElement(List<ItemStack> materialIn, int method, int tier, List<ItemStack> resultIn, long energy, long time) {
        condition = new RecipeCondition(materialIn, method, tier);
        result = new RecipeResult(resultIn, energy, time);
    }

    public RecipeCondition getCondition() {
        return condition;
    }

    public RecipeResult getResult() {
        return result;
    }

    private final RecipeCondition condition;
    private final RecipeResult result;

    public static class RecipeCondition {
        private final List<ItemStack> materials;
        private final int method;
        private final int tier;

        public List<ItemStack> getMaterials() {
            return materials;
        }

        public int getMethod() {
            return method;
        }

        public int getTier() {
            return tier;
        }

        public RecipeCondition(List<ItemStack> materials, int method, int tier) {
            this.materials = materials;
            this.method = method;
            this.tier = tier;
        }

        public boolean match(List<ItemStack> itemStacksIn, int methodIn, int tierIn) {
            if (method != methodIn || tier > tierIn || materials.size() > itemStacksIn.size()) return false;
            return match(itemStacksIn);
        }

        public boolean match(List<ItemStack> itemStacksIn) {
            for (int i = 0; i < materials.size(); i++)
                if (!inclusion(materials.get(i), itemStacksIn.get(i)))
                    return false;

            return true;
        }

        public static boolean inclusion(ItemStack from, ItemStack comes) {
            if (from.isEmpty()) return true;
            if (from.getCount() > comes.getCount()) return false;
            if (from.getHasSubtypes() && comes.getHasSubtypes()) return from.isItemEqual(comes);
            return from.getItem() == comes.getItem();
        }
    }

    public static class RecipeResult {
        private final List<ItemStack> results;
        private final long energy;
        private final long time;

        public List<ItemStack> getResults() {
            return results;
        }

        public long getEnergy() {
            return energy;
        }

        public long getTime() {
            return time;
        }

        public RecipeResult(List<ItemStack> results, long energy, long time) {
            this.results = results;
            this.energy = energy;
            this.time = time;
        }
    }
}