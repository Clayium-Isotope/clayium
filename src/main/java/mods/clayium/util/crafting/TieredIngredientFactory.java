package mods.clayium.util.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.util.JsonHelper;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <pre>{@code
 * {
 *     "type": "clayium:tiered",
 *     "keyword": "circuit" | "hull" | "machine",
 *     "machine": "assembler",  // when "keyword" is "machine"
 *     "tier": 1
 * }
 * }</pre>
 */
public class TieredIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) throws JsonSyntaxException {
        int tier = JsonHelper.readNumeric(json, "tier", Integer.class);
        if (tier < 0 || tier > TierPrefix.values().length) {
            throw new JsonSyntaxException("Invalid tier: " + tier);
        }

        if (!json.has("keyword")) {
            throw new JsonSyntaxException("Keyword not found; must be either 'circuit', 'hull' or 'machine");
        }

        String rawKeyword = JsonUtils.getString(json, "keyword");
        TieredIngredientKeyword keyword = TieredIngredientKeyword.fromName(rawKeyword);

        if (keyword == null) {
            throw new JsonSyntaxException("Invalid keyword: " + rawKeyword);
        }

        return TieredIngredient.parse(keyword, tier, json);
    }

    public static class TieredIngredient extends Ingredient {
        protected final TieredIngredientKeyword keyword;
        protected final EnumMachineKind machine;
        protected final int tier;

        protected TieredIngredient(TieredIngredientKeyword keyword, EnumMachineKind machine, int tier, ItemStack... stacks) {
            super(stacks);
            this.keyword = keyword;
            this.machine = machine;
            this.tier = tier;
        }

        public static TieredIngredient parse(TieredIngredientKeyword keyword, int tier, JsonObject json) throws JsonSyntaxException {
            switch (keyword) {
                case CIRCUIT:
                    return new TieredIngredient(TieredIngredientKeyword.CIRCUIT, EnumMachineKind.EMPTY, tier, ClayiumRecipes.circuits.get(tier));
                case HULL:
                    return new TieredIngredient(TieredIngredientKeyword.HULL, EnumMachineKind.EMPTY, tier, ClayiumRecipes.machines.get(tier));
                case MACHINE:
                    if (!json.has("machine")) {
                        throw new JsonSyntaxException("The key \"machine\" not found");
                    }

                    EnumMachineKind kind = EnumMachineKind.fromName(JsonUtils.getString(json, "machine"));
                    if (kind == EnumMachineKind.EMPTY) {
                        throw new JsonSyntaxException("Unknown machine: " + kind);
                    }

                    Block machine = ClayiumMachines.get(kind, TierPrefix.get(tier));
                    if (machine == null || machine == Blocks.AIR) {
                        throw new JsonSyntaxException("Could not find machine: " + kind + ", tier: " + tier);
                    }

                    return new TieredIngredient(TieredIngredientKeyword.MACHINE, kind, tier, new ItemStack(machine));
            }

            throw new JsonSyntaxException("Could not process keyword: " + keyword);
        }
    }

    /*package-private*/ enum TieredIngredientKeyword {
        CIRCUIT("circuit"), HULL("hull"), MACHINE("machine");

        TieredIngredientKeyword(String value) {
            this.value = value;
        }

        /*package-private*/ final String value;

        @Nullable // when not found
        public static TieredIngredientKeyword fromName(String name) {
            for (TieredIngredientKeyword keyword : values()) {
                if (keyword.value.equals(name)) {
                    return keyword;
                }
            }
            return null;
        }
    }
}
