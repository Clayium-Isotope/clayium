package mods.clayium.util.crafting;

import cpw.mods.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


public class Recipes {
    public static Map<String, Recipes> RecipeMap = new HashMap<String, Recipes>();
    @Deprecated
    public Map RecipeList = new HashMap<Object, Object>();

    public String recipeid;

    public Map<RecipeCondition, RecipeResult> recipeResultMap = new HashMap<RecipeCondition, RecipeResult>();


    public Map<GameRegistry.UniqueIdentifier, List<RecipeCondition>> simpleConditionMap = new HashMap<GameRegistry.UniqueIdentifier, List<RecipeCondition>>();
    public List<RecipeCondition> complexConditionList = new ArrayList<RecipeCondition>();
    protected boolean refreshed = false;

    public static class RecipeCondition {
        protected Object[] materials;
        protected int method;
        protected int tier;

        public Object[] getMaterials() {
            return this.materials;
        }

        public int getMethod() {
            return this.method;
        }

        public int getTier() {
            return this.tier;
        }

        public RecipeCondition(Object[] materials, int method, int tier) {
            this.materials = materials;
            this.method = method;
            this.tier = tier;
        }


        public int match(ItemStack[] itemstacks, int method, int tier, boolean sizeCheck) {
            if (this.materials.length == itemstacks.length) {
                return matchLengthUnchecked(itemstacks, method, tier, sizeCheck);
            }
            return -1;
        }

        public int matchRecipe(Object[] itemstacks, int method, int tier, boolean sizeCheck) {
            if (this.materials.length == itemstacks.length) {
                return matchRecipeLengthUnchecked(itemstacks, method, tier, sizeCheck);
            }
            return -1;
        }

        protected int matchLengthUnchecked(ItemStack[] itemstacks, int method, int tier, boolean sizeCheck) {
            if (this.method != method || this.tier > tier) {
                return -1;
            }
            int stackSize = 0;
            for (int i = 0; i < itemstacks.length && i < this.materials.length; i++) {
                if (!Recipes.canBeCraftedOD(itemstacks[i], this.materials[i], sizeCheck)) {
                    return -1;
                }
                stackSize += Recipes.getStackSize(this.materials[i], itemstacks[i]);
            }

            return stackSize;
        }

        protected int matchRecipeLengthUnchecked(Object[] itemstacks, int method, int tier, boolean sizeCheck) {
            if (this.method != method || this.tier > tier) {
                return -1;
            }
            int stackSize = 0;
            for (int i = 0; i < itemstacks.length && i < this.materials.length; i++) {
                if (!Recipes.canBeCraftedODs(itemstacks[i], this.materials[i], sizeCheck)) {
                    return -1;
                }
                stackSize += Recipes.getStackSize(this.materials[i], Recipes.isSimple(itemstacks[i]));
            }

            return stackSize;
        }

        public boolean isCraftable(ItemStack itemstack, int tier) {
            if (this.tier > tier) {
                return false;
            }

            for (int i = 0; i < this.materials.length; i++) {
                if (Recipes.canBeCraftedODs(itemstack, this.materials[i], false)) {
                    return true;
                }
            }
            return false;
        }

        public int[] getStackSizes(ItemStack[] items) {
            int[] stacksizelist = new int[items.length];
            for (int i = 0; i < items.length && i < this.materials.length; i++) {
                stacksizelist[i] = Recipes.getStackSize(this.materials[i], items[i]);
            }
            return stacksizelist;
        }

        public boolean isAvailable() {
            if (this.materials == null || this.materials.length == 0)
                return false;
            for (Object material : this.materials) {
                if (material != null) {
                    if (!(material instanceof ItemStack)) {
                        if (material instanceof OreDictionaryStack) {
                            List<ItemStack> list = OreDictionary.getOres(Integer.valueOf(((OreDictionaryStack) material).id));
                            if (list == null || list.size() == 0) {
                                return false;
                            }
                        } else if (material instanceof String) {
                            List<ItemStack> list = OreDictionary.getOres((String) material);
                            if (list == null || list.size() == 0) {
                                return false;
                            }
                        } else if (material instanceof IItemPattern) {
                            if (!((IItemPattern) material).isAvailable()) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        }


        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + UtilItemStack.getItemStackHashCode(this.materials);
            result = 31 * result + this.method;
            result = 31 * result + this.tier;
            return result;
        }


        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            RecipeCondition other = (RecipeCondition) obj;
            if (!UtilItemStack.areStacksEqual(this.materials, other.materials))
                return false;
            if (this.method != other.method)
                return false;
            if (this.tier != other.tier)
                return false;
            return true;
        }
    }


    public static int getStackSize(Object item) {
        if (item instanceof ItemStack)
            return ((ItemStack) item).stackSize;
        if (item instanceof OreDictionaryStack)
            return ((OreDictionaryStack) item).stackSize;
        if (item instanceof String) {
            return 1;
        }
        return 0;
    }

    public static ItemStack isSimpleCond(Object[] items) {
        if (items != null && items.length == 1)
            return isSimple(items[0]);
        return null;
    }

    public static ItemStack isSimple(Object item) {
        if (item instanceof ItemStack)
            return (ItemStack) item;
        if (item instanceof IItemPattern) {
            return ((IItemPattern) item).isSimple();
        }
        return null;
    }

    public static int getStackSize(Object recipe, ItemStack item) {
        if (recipe instanceof IItemPattern) {
            if (item == null) {
                ItemStack[] items = ((IItemPattern) recipe).toItemStacks();
                if (items != null && items.length >= 1)
                    item = items[0];
            }
            return ((IItemPattern) recipe).getStackSize(item);
        }
        return getStackSize(recipe);
    }

    public static class RecipeResult {
        protected ItemStack[] results;
        protected long energy;
        protected long time;

        public ItemStack[] getResults() {
            return this.results;
        }

        public long getEnergy() {
            return this.energy;
        }

        public long getTime() {
            return this.time;
        }

        public RecipeResult(ItemStack[] results, long energy, long time) {
            this.results = results;
            this.energy = energy;
            this.time = time;
        }

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + (int) (this.energy ^ this.energy >>> 32L);
            result = 31 * result + UtilItemStack.getItemStackHashCode((Object[]) this.results);
            result = 31 * result + (int) (this.time ^ this.time >>> 32L);
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            RecipeResult other = (RecipeResult) obj;
            if (this.energy != other.energy)
                return false;
            if (!UtilItemStack.areStacksEqual((Object[]) this.results, (Object[]) other.results))
                return false;
            if (this.time != other.time)
                return false;
            return true;
        }
    }


    public Iterable<RecipeCondition> getConditionsForObjects(Object[] materials) {
        if (!this.refreshed) {
            removeUnavailableRecipes();
        }
        ItemStack[] items = new ItemStack[materials.length];
        for (int i = 0; i < materials.length; i++) {
            if (materials[i] instanceof ItemStack) {
                items[i] = (ItemStack) materials[i];
            } else {
                return getAllConditions();
            }
        }
        return getConditionsToSearch(items);
    }

    public Set<RecipeCondition> getAllConditions() {
        return this.recipeResultMap.keySet();
    }

    public void removeUnavailableRecipes() {
        List<RecipeCondition> rem = new ArrayList<RecipeCondition>();
        for (RecipeCondition cond : this.recipeResultMap.keySet()) {
            if (!cond.isAvailable())
                rem.add(cond);
        }
        for (RecipeCondition cond : rem)
            removeRecipe(cond);
        this.refreshed = true;
    }

    public List<RecipeCondition> getConditionsToSearch(ItemStack[] itemstacks) {
        List<RecipeCondition> res = new ArrayList<RecipeCondition>();
        res.addAll(this.complexConditionList);
        for (ItemStack itemstack : itemstacks) {
            List<RecipeCondition> list = this.simpleConditionMap.get(getUniqueIdentifier(itemstack));
            if (list != null)
                res.addAll(list);
        }
        return res;
    }

    protected static GameRegistry.UniqueIdentifier getUniqueIdentifier(ItemStack itemstack) {
        if (itemstack == null) return null;
        return UtilItemStack.findUniqueIdentifierFor(itemstack.getItem());
    }

    public Recipes(String recipeid) {
        this.recipeid = recipeid;
        RecipeMap.put(recipeid, this);
    }

    public static Recipes GetRecipes(String recipeid) {
        return RecipeMap.get(recipeid);
    }


    public void addRecipe(Object[] materials, int method, int tier, ItemStack[] results, long energy, long time, boolean check) {
        IItemPattern[] arrayOfIItemPattern = convertRecipeArray(materials);
        if (arrayOfIItemPattern == null || results == null) {
            ClayiumCore.logger.error("Invalid recipe!");
            ClayiumCore.logger.error("Arrays must not be null.");
            ClayiumCore.logger.error("Recipe ID : " + this.recipeid);
            return;
        }
        for (IItemPattern material : arrayOfIItemPattern) {
            if (material == null || !(material.isSimple() instanceof ItemStack || material.toString() instanceof String || material instanceof OreDictionaryStack || material instanceof IItemPattern)) {
                ClayiumCore.logger.error("Invalid recipe! The ingredient array contains an invalid stack.");
                ClayiumCore.logger.error("Recipe ID : " + this.recipeid);
                ClayiumCore.logger.error("Ingredients : " + Arrays.toString((Object[]) arrayOfIItemPattern));
                ClayiumCore.logger.error("Results : " + Arrays.toString((Object[]) results));
                return;
            }
        }
        for (ItemStack result : results) {
            if (result == null || result.getItem() == null) {
                ClayiumCore.logger.error("Invalid recipe! The result array contains a null stack.");
                ClayiumCore.logger.error("Recipe ID : " + this.recipeid);
                ClayiumCore.logger.error("Ingredients : " + Arrays.toString((Object[]) arrayOfIItemPattern));
                ClayiumCore.logger.error("Results : " + Arrays.toString((Object[]) results));

                return;
            }
        }
        try {
            if (check && getRecipeConditionFromRecipe((Object[]) arrayOfIItemPattern, method, tier, false) != null)
                return;
            addRecipeToOldMap((Object[]) arrayOfIItemPattern, method, tier, results, energy, time);
            addRecipeToRecipeMap((Object[]) arrayOfIItemPattern, method, tier, results, energy, time);
        } catch (Exception e) {
            ClayiumCore.logger.error("Failed to register a recipe.");
            ClayiumCore.logger.error("Recipe ID : " + this.recipeid);
            ClayiumCore.logger.error("Ingredients : " + Arrays.toString((Object[]) arrayOfIItemPattern));
            ClayiumCore.logger.error("Results : " + Arrays.toString((Object[]) results));
            ClayiumCore.logger.catching(e);
        }
    }


    @Deprecated
    protected void addRecipeToOldMap(Object[] materials, int method, int tier, ItemStack[] results, long energy, long time) {
        Map<Object, Object> keyMap = new HashMap<Object, Object>();
        Map<Object, Object> valueMap = new HashMap<Object, Object>();
        keyMap.put("Material", Arrays.asList((Object[]) materials.clone()));
        keyMap.put("Method", Integer.valueOf(method));
        keyMap.put("Tier", Integer.valueOf(tier));
        valueMap.put("Result", Array2ArrayList(results));
        valueMap.put("Energy", Long.valueOf(energy));
        valueMap.put("Time", Long.valueOf(time));
        this.RecipeList.put(keyMap, valueMap);
    }


    protected void addRecipeToRecipeMap(Object[] materials, int method, int tier, ItemStack[] results, long energy, long time) {
        addRecipe(new RecipeCondition(materials, method, tier), new RecipeResult(results, energy, time));
    }

    public RecipeResult addRecipe(RecipeCondition cond, RecipeResult result) {
        addCondition(cond);
        try {
            return this.recipeResultMap.put(cond, result);
        } catch (RuntimeException e) {
            ClayiumCore.logger.error("Failed to add a recipe. Trying to remove this broken recipe.");
            try {
                removeRecipe(cond);
            } catch (RuntimeException e1) {
                ClayiumCore.logger.catching(e);
                ClayiumCore.logger.fatal("Failed to remove the broken recipe. It may cause a server crush.");
                throw e1;
            }
            throw e;
        }
    }

    public RecipeResult removeRecipe(RecipeCondition cond) {
        removeCondition(cond);
        return this.recipeResultMap.remove(cond);
    }

    public RecipeResult getResult(RecipeCondition cond) {
        return this.recipeResultMap.get(cond);
    }

    protected void addCondition(RecipeCondition cond) {
        if (cond == null || cond.getMaterials() == null)
            return;
        ItemStack recipe = isSimpleCond(cond.getMaterials());
        if (recipe != null) {
            GameRegistry.UniqueIdentifier ui = getUniqueIdentifier(recipe);
            List<RecipeCondition> conds = this.simpleConditionMap.get(ui);
            if (conds == null) {
                conds = new ArrayList<RecipeCondition>();
                this.simpleConditionMap.put(ui, conds);
            }
            conds.add(cond);
        } else {
            this.complexConditionList.add(cond);
        }
    }

    protected void removeCondition(RecipeCondition cond) {
        if (cond == null || cond.getMaterials() == null)
            return;
        ItemStack recipe = isSimpleCond(cond.getMaterials());
        if (recipe != null) {
            GameRegistry.UniqueIdentifier ui = getUniqueIdentifier(recipe);
            List<RecipeCondition> conds = this.simpleConditionMap.get(ui);
            if (conds == null) {
                conds = new ArrayList<RecipeCondition>();
                this.simpleConditionMap.put(ui, conds);
            }
            conds.remove(cond);
        } else {
            this.complexConditionList.remove(cond);
        }
    }


    public void addRecipe(Object[] materials, int method, int tier, ItemStack[] results, long energy, long time) {
        addRecipe(materials, method, tier, results, energy, time, false);
    }

    public static boolean canBeCrafted(ItemStack itemstack, ItemStack itemstack2, boolean sizeCheck) {
        if (itemstack2 == null)
            return true;
        if (itemstack == null)
            return false;
        return (UtilItemStack.areItemEqual(itemstack2, itemstack) && (itemstack2
                .getItemDamage() == 32767 || itemstack
                .getItemDamage() == 32767 ||
                UtilItemStack.areDamageEqual(itemstack2, itemstack)) && (!sizeCheck || itemstack2.stackSize <= itemstack.stackSize));
    }


    public static boolean canBeCrafted(ItemStack itemstack, ItemStack itemstack2) {
        return canBeCrafted(itemstack, itemstack2, true);
    }

    public static boolean canBeCraftedOD(ItemStack itemstack, Object object, boolean sizeCheck) {
        if (object == null)
            return true;
        if (itemstack == null)
            return false;
        if (object instanceof String) {
            if (UtilItemStack.hasOreName(itemstack, (String) object)) return true;
        } else if (object instanceof OreDictionaryStack) {
            if (sizeCheck && ((OreDictionaryStack) object).stackSize > itemstack.stackSize)
                return false;
            if (UtilItemStack.hasOreName(itemstack, ((OreDictionaryStack) object).id)) return true;
        } else {
            if (object instanceof ItemStack)
                return canBeCrafted(itemstack, (ItemStack) object, sizeCheck);
            if (object instanceof IItemPattern) {

                return ((IItemPattern) object).match(itemstack, sizeCheck);
            }
        }

        return false;
    }

    public static boolean canBeCraftedOD(ItemStack itemstack, Object object) {
        return canBeCraftedOD(itemstack, object, true);
    }

    public static boolean canBeCraftedODs(Object stackingred, Object recipeingred, boolean sizeCheck) {
        if (recipeingred == null)
            return true;
        if (stackingred == null)
            return false;
        if (stackingred instanceof ItemStack) {
            return canBeCraftedOD((ItemStack) stackingred, recipeingred, sizeCheck);
        }
        if (stackingred instanceof String || stackingred instanceof OreDictionaryStack) {


            int oreid = (stackingred instanceof OreDictionaryStack) ? ((OreDictionaryStack) stackingred).id : OreDictionary.getOreID((String) stackingred);
            int stackSize = (stackingred instanceof OreDictionaryStack) ? ((OreDictionaryStack) stackingred).stackSize : 1;
            for (ItemStack item : OreDictionary.getOres(Integer.valueOf(oreid))) {
                ItemStack item0 = item.copy();
                item0.stackSize = stackSize;
                if (canBeCraftedOD(item0, recipeingred, sizeCheck)) {
                    return true;
                }
            }
        }
        if (stackingred instanceof IItemPattern) {


            return ((IItemPattern) stackingred).hasIntersection(convert(recipeingred), sizeCheck);
        }
        return false;
    }

    public static IItemPattern convert(Object ingred) {
        if (ingred instanceof ItemStack) {
            return new ItemPatternItemStack((ItemStack) ingred);
        }
        if (ingred instanceof OreDictionaryStack) {
            return new ItemPatternOreDictionary(((OreDictionaryStack) ingred).id, ((OreDictionaryStack) ingred).stackSize);
        }
        if (ingred instanceof String) {
            return new ItemPatternOreDictionary((String) ingred, 1);
        }
        if (ingred instanceof IItemPattern) {
            return (IItemPattern) ingred;
        }
        return null;
    }

    public static IItemPattern[] convertRecipeArray(Object[] ingreds) {
        if (ingreds == null)
            return null;
        IItemPattern[] patterns = new IItemPattern[ingreds.length];
        for (int i = 0; i < ingreds.length; i++)
            patterns[i] = convert(ingreds[i]);
        return patterns;
    }

    public static List<ItemStack> Array2ArrayList(ItemStack[] itemstacks) {
        List<ItemStack> res = new ArrayList(itemstacks.length);
        for (int i = 0; i < itemstacks.length; i++) {
            res.add(itemstacks[i]);
        }
        return res;
    }

    public static ItemStack[] ArrayList2Array(List<ItemStack> itemstacks) {
        ItemStack[] res = new ItemStack[itemstacks.size()];
        for (int i = 0; i < itemstacks.size(); i++) {
            res[i] = itemstacks.get(i);
        }
        return res;
    }


    @Deprecated
    public Map.Entry getResultEntry(List materials, int method, int tier, boolean sizeCheck) {
        Map.Entry entry_ = null;
        int maxStackSize = 0;
        for (Iterator<Map.Entry> iterator = this.RecipeList.entrySet().iterator(); iterator.hasNext(); ) {

            Map.Entry entry = iterator.next();
            List materiallist = (List) ((Map) entry.getKey()).get("Material");

            boolean flag = true;
            int stackSize = 0;
            if (materiallist.size() == materials.size()) {
                for (int i = 0; i < materiallist.size(); i++) {

                    if (!canBeCraftedODs(materials.get(i), materiallist.get(i), sizeCheck)) {
                        flag = false;
                    } else if (materiallist.get(i) instanceof ItemStack) {
                        stackSize += ((ItemStack) materiallist.get(i)).stackSize;
                    } else if (materiallist.get(i) instanceof OreDictionaryStack) {
                        stackSize += ((OreDictionaryStack) materiallist.get(i)).stackSize;
                    } else if (materiallist.get(i) instanceof String) {
                        stackSize++;
                    }
                }

                if (flag == true && ((Integer) ((Map) entry
                        .getKey()).get("Method")).intValue() == method && ((Integer) ((Map) entry
                        .getKey()).get("Tier")).intValue() <= tier && stackSize > maxStackSize) {

                    entry_ = entry;
                    maxStackSize = stackSize;
                }
            }
        }
        if (entry_ == null) return null;
        return entry_;
    }

    public RecipeCondition getRecipeConditionFromRecipe(Object[] materials, int method, int tier, boolean sizeCheck) {
        int maxStackSize = -1;
        RecipeCondition res = null;
        for (RecipeCondition cond : getConditionsForObjects(materials)) {
            int s = cond.matchRecipe(materials, method, tier, sizeCheck);
            if (s > maxStackSize) {
                maxStackSize = s;
                res = cond;
            }
        }
        return res;
    }

    public RecipeCondition getRecipeConditionFromRecipe(Object[] materials, int method, int tier) {
        return getRecipeConditionFromRecipe(materials, method, tier, true);
    }

    public RecipeCondition getRecipeConditionFromInventory(ItemStack[] materials, int method, int tier, boolean sizeCheck) {
        int maxStackSize = -1;
        RecipeCondition res = null;
        for (RecipeCondition cond : getConditionsForObjects((Object[]) materials)) {
            int s = cond.match(materials, method, tier, sizeCheck);
            if (s > maxStackSize) {
                maxStackSize = s;
                res = cond;
            }
        }
        return res;
    }

    public RecipeCondition getRecipeConditionFromInventory(ItemStack[] materials, int method, int tier) {
        return getRecipeConditionFromInventory(materials, method, tier, true);
    }

    public RecipeResult getRecipeResultFromRecipe(Object[] materials, int method, int tier, boolean sizeCheck) {
        return getRecipeResult(getRecipeConditionFromRecipe(materials, method, tier, sizeCheck));
    }

    public RecipeResult getRecipeResultFromRecipe(Object[] materials, int method, int tier) {
        return getRecipeResultFromRecipe(materials, method, tier, true);
    }

    public RecipeResult getRecipeResultFromInventory(ItemStack[] materials, int method, int tier, boolean sizeCheck) {
        return getRecipeResult(getRecipeConditionFromInventory(materials, method, tier, sizeCheck));
    }

    public RecipeResult getRecipeResultFromInventory(ItemStack[] materials, int method, int tier) {
        return getRecipeResultFromInventory(materials, method, tier, true);
    }

    public RecipeResult getRecipeResult(RecipeCondition condition) {
        return (condition != null) ? this.recipeResultMap.get(condition) : null;
    }


    @Deprecated
    public Map.Entry getResultEntry(List<ItemStack> materials, int method, int tier) {
        return getResultEntry(materials, method, tier, true);
    }

    @Deprecated
    protected Map getMaterialMap(List<ItemStack> materials, int method, int tier) {
        Map.Entry entry = getResultEntry(materials, method, tier);
        if (entry == null) return null;
        return (Map) entry.getKey();
    }

    @Deprecated
    protected Map getResultMap(List<ItemStack> materials, int method, int tier) {
        Map.Entry entry = getResultEntry(materials, method, tier);
        if (entry == null) return null;
        return (Map) entry.getValue();
    }


    public int[] getConsumedStackSize(ItemStack[] materials, int method, int tier) {
        RecipeCondition cond = getRecipeConditionFromInventory(materials, method, tier);
        if (cond == null) return null;
        return cond.getStackSizes(materials);
    }


    public int[] getConsumedStackSize(List<ItemStack> materials, int method, int tier) {
        return getConsumedStackSize(materials.<ItemStack>toArray(new ItemStack[0]), method, tier);
    }

    public static ItemStack[] getResults(RecipeResult result) {
        return (result == null) ? null : result.getResults();
    }


    public ItemStack[] getResult(ItemStack[] materials, int method, int tier) {
        return getResults(getRecipeResultFromInventory(materials, method, tier));
    }


    public ItemStack[] getResult(List<ItemStack> materials, int method, int tier) {
        return getResult(materials.<ItemStack>toArray(new ItemStack[0]), method, tier);
    }


    public static long getEnergy(RecipeResult result) {
        return (result == null) ? 0L : result.getEnergy();
    }


    public long getEnergy(ItemStack[] materials, int method, int tier) {
        return getEnergy(getRecipeResultFromInventory(materials, method, tier));
    }


    public long getEnergy(List<ItemStack> materials, int method, int tier) {
        return getEnergy(materials.<ItemStack>toArray(new ItemStack[0]), method, tier);
    }

    public static long getTime(RecipeResult result) {
        return (result == null) ? 0L : result.getTime();
    }


    public long getTime(ItemStack[] materials, int method, int tier) {
        return getTime(getRecipeResultFromInventory(materials, method, tier));
    }


    public long getTime(List<ItemStack> materials, int method, int tier) {
        return getTime(materials.<ItemStack>toArray(new ItemStack[0]), method, tier);
    }

    public static long getValue(Object obj) {
        if (obj instanceof Long)
            return ((Long) obj).longValue();
        if (obj instanceof Integer)
            return ((Integer) obj).intValue();
        if (obj instanceof Short)
            return ((Short) obj).shortValue();
        if (obj instanceof Byte)
            return ((Byte) obj).byteValue();
        if (obj instanceof Double)
            return (long) ((Double) obj).doubleValue();
        if (obj instanceof Float)
            return (long) ((Float) obj).floatValue();
        return 0L;
    }

    @Deprecated
    public boolean hasResult(ItemStack[] materials, int tier) {
        return hasResult(Array2ArrayList(materials), tier);
    }

    @Deprecated
    public boolean hasResult(List<ItemStack> materials, int tier) {
        Map.Entry entry_ = null;
        int maxStackSize = 0;
        for (Iterator<Map.Entry> iterator = this.RecipeList.entrySet().iterator(); iterator.hasNext(); ) {

            Map.Entry entry = iterator.next();
            List materiallist = (List) ((Map) entry.getKey()).get("Material");

            boolean flag = true;
            int stackSize = 0;
            if (materiallist.size() <= materials.size()) {
                for (int i = 0; i < materiallist.size(); i++) {

                    if (!canBeCraftedOD(materials.get(i), materiallist.get(i))) {
                        flag = false;
                    } else if (materiallist.get(i) instanceof ItemStack) {
                        stackSize += ((ItemStack) materiallist.get(i)).stackSize;
                    } else if (materiallist.get(i) instanceof OreDictionaryStack) {
                        stackSize += ((OreDictionaryStack) materiallist.get(i)).stackSize;
                    } else if (materiallist.get(i) instanceof String) {
                        stackSize++;
                    }
                }


                if (flag = (((Integer) ((Map) entry.getKey()).get("Tier")).intValue() <= tier && stackSize > maxStackSize)) {

                    entry_ = entry;
                    maxStackSize = stackSize;
                }
            }
        }
        if (entry_ == null) return false;
        return true;
    }


    public boolean isCraftable(ItemStack material, int tier) {
        if (material == null) return false;
        int maxStackSize = -1;
        for (RecipeCondition cond : getConditionsForObjects((Object[]) new ItemStack[] {material})) {
            if (cond.isCraftable(material, tier))
                return true;
        }
        return false;
    }
}


