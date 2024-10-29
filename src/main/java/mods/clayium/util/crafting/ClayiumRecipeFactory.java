package mods.clayium.util.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.JsonHelper;
import mods.clayium.util.UtilCollect;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * <a href="https://docs.minecraftforge.net/en/1.12.x/utilities/recipes/">Guide</a>
 * {@link CraftingHelper#init()}
 *
 * <pre>
 * {@code example.json
 * {
 *     "type": "clayium:add_recipe",
 *     "machine": "condenser",
 *     "input": [
 *         {
 *             "type": "forge:ore_dict",
 *             "ore": "nuggetGold",
 *             "count": 9,
 *
 *             "priority": 1
 *         }
 *     ],
 *     "tier": 4,
 *     "output": [
 *         {
 *             "type": "minecraft:item",
 *             "item": "minecraft:gold_ingot"
 *         }
 *     ],
 *     "energy": 1000,
 *     "time": 120
 * }
 * }
 * </pre>
 *
 * Usage: resources/assets/clayium/recipes/_factories.json
 */
public class ClayiumRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) throws JsonSyntaxException {
        String group = JsonUtils.getString(json, "machine", "");
        EnumMachineKind kind = EnumMachineKind.fromName(group);
        if (kind == EnumMachineKind.EMPTY || !kind.hasRecipe()) {
            throw new JsonSyntaxException("Invalid machine type: " + group);
        }

        JsonArray input = JsonUtils.getJsonArray(json, "input");
        if (input.size() > kind.getSlotType().inCount) {
            throw new JsonSyntaxException("Too many inputs for " + kind.getRegisterName());
        }

        // https://qastack.jp/programming/23932061/convert-iterable-to-stream-using-java-8-jdk
        NonNullList<Ingredient> inputItems = StreamSupport.stream(input.spliterator(), false)
                .map(elm -> new Tuple<>(
                        JsonHelper.readNumeric(elm, "priority", Integer.MAX_VALUE),
                        new AmountedIngredient(elm, context)))
                .sorted(Comparator.comparingInt(Tuple::getFirst)) // The smaller the "priority" value, the more it comes
                                                                  // to the front
                .map(Tuple::getSecond)
                .collect(UtilCollect.toNonNullList());

        JsonArray output = JsonUtils.getJsonArray(json, "output");
        if (output.size() > kind.getSlotType().outCount) {
            throw new JsonSyntaxException("Too many outputs for " + kind.getRegisterName());
        }

        List<ItemStack> outputItems = new ArrayList<>(kind.getSlotType().outCount);
        for (JsonElement ele : output) {
            if (!ele.isJsonObject()) {
                throw new JsonSyntaxException("Expected item object but found " + ele);
            }

            outputItems.add(UtilItemStack.getItemStack((JsonObject) ele, context));
        }

        long energy;
        if (json.get("energy").isJsonObject()) {
            JsonObject energyObj = json.getAsJsonObject("energy");
            if (!json.has("tier")) throw new JsonSyntaxException(
                    "\"energy\" should be a long number or be an object { \"tier\": int, \"factor\"?: double }");
            energy = ClayiumRecipes.e(JsonHelper.readNumeric(energyObj, "factor", 1.0d),
                    JsonHelper.readNumeric(energyObj, "tier", Integer.class));
        } else {
            energy = JsonHelper.readNumeric(json, "energy", Long.class);
        }

        RecipeElement recipe = new RecipeElement(inputItems, JsonHelper.readNumeric(json, "tier", Integer.class),
                outputItems, energy, JsonHelper.readNumeric(json, "time", Long.class));
        kind.getRecipe().add(recipe);
        return recipe;
    }
}
