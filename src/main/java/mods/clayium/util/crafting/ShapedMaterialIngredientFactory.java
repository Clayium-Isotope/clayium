package mods.clayium.util.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;

/**
 * <pre>
 * {@code
 * {
 *     "type": "clayium:shaped_material",
 *     "material": "clay",
 *     "shape": "plate"
 * }
 * }
 * </pre>
 */
public class ShapedMaterialIngredientFactory implements IIngredientFactory {

    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        if (!json.has("material") || !json.has("shape")) {
            throw new JsonSyntaxException(
                    "Recipe Ingredient Type \"clayium:shaped_material\" requires 'material' and 'shape' properties");
        }

        ClayiumMaterial material = ClayiumMaterial.fromName(json.get("material").getAsString());
        ClayiumShape shape = ClayiumShape.fromName(json.get("shape").getAsString());

        if (material == null || shape == null) {
            throw new JsonSyntaxException("Unknown material or shape");
        }

        String rawOre = ClayiumMaterials.getOreName(material, shape);
        if (OreDictionary.doesOreNameExist(rawOre)) {
            return new OreIngredient(rawOre);
        }

        return new ShapedMaterialIngredient(material, shape);
    }

    public static class ShapedMaterialIngredient extends Ingredient {

        protected final ClayiumMaterial material;
        protected final ClayiumShape shape;

        public ShapedMaterialIngredient(ClayiumMaterial material, ClayiumShape shape) {
            super(ClayiumMaterials.get(material, shape));
            this.material = material;
            this.shape = shape;
        }

        @Override
        public boolean isSimple() {
            return true;
        }
    }
}
