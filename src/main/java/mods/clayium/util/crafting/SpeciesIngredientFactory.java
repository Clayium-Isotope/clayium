package mods.clayium.util.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

/**
 * Available
 * 
 * <pre>
 * {@code
 * {
 *     "type": "clayium:species",
 *     "root": "filter" | "gadget",
 *     "branch": "blank" etc.,
 *     "nbt": {} // optional
 * }
 * }
 * </pre>
 */
public class SpeciesIngredientFactory implements IIngredientFactory {

    @Nonnull
    @Override
    public Ingredient parse(final JsonContext context, final JsonObject json) {
        return Ingredient.fromStacks(getStack(json));
    }

    public static ItemStack getStack(final JsonObject json) {
        if (!json.has("root") || !json.has("branch")) {
            throw new JsonSyntaxException(
                    "[SpeciesIngredientFactory] Recipe Ingredient Type \"clayium:lineage\" requires 'root' and 'branch' properties");
        }

        final LineageRoot root = LineageRoot.getByRoot(json.get("root").getAsString());
        if (Objects.isNull(root)) {
            throw new JsonSyntaxException("[SpeciesIngredientFactory] Unknown 'root'");
        }

        return root.mapPicker().apply(json.get("branch").getAsString());
    }

    enum LineageRoot {

        Filter("filter"),
        Gadget("gadget");

        LineageRoot(String parent) {
            this.parent = parent;
        }

        private final String parent;

        @Nullable
        public static LineageRoot getByRoot(String parent) {
            for (LineageRoot root : LineageRoot.values()) {
                if (root.parent.equals(parent)) return root;
            }

            return null;
        }

        /**
         * TODO Resource Locationで探索しているのは、後で変更予定
         */
        public Function<String, ItemStack> mapPicker() {
            return branch -> {
                final ResourceLocation rl = new ResourceLocation(ClayiumCore.ModId, this.parent + "_" + branch);
                final Item raw = GameRegistry.findRegistry(Item.class).getValue(rl);
                if (Objects.isNull(raw)) {
                    throw new IllegalArgumentException(rl + " couldn't find");
                }
                return new ItemStack(raw);
            };
        }
    }
}
