package mods.clayium.util;

import com.google.gson.*;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.crafting.IRecipeElement;
import mods.clayium.util.crafting.AmountedIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonHelper {

    public static void genItemJson(TierPrefix tier, EnumMachineKind kind, String registerName) {
        Path path = Paths.get(
                "D:\\clayium\\src\\main\\resources\\assets\\clayium\\models\\item\\machine\\" + registerName + ".json");
        if (Files.exists(path)) return;

        JsonObject root = new JsonObject();
        root.addProperty("parent", "clayium:block/machine_temp");

        JsonObject textures = new JsonObject();
        textures.addProperty("hull", "clayium:blocks/machinehull-" + (tier.meta() - 1));
        textures.addProperty("face", "clayium:blocks/machine/" + kind.facePath);

        root.add("textures", textures);

        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (IOException ioException) {
            ClayiumCore.logger.error(ioException.getMessage());
            return;
        }

        ClayiumCore.logger.info("Generated: " + path);
    }

    public static void genBlockJsonParent(TierPrefix tier, EnumMachineKind kind, String registerName, String parent) {
        Path path = Paths.get("D:\\clayium\\src\\main\\resources\\assets\\clayium\\models\\block\\machine\\" +
                registerName + ".json");
        if (Files.exists(path)) return;

        JsonObject root = new JsonObject();
        root.addProperty("parent", "clayium:block/" + parent);

        JsonObject textures = new JsonObject();
        textures.addProperty("hull", "clayium:blocks/machinehull-" + (tier.meta() - 1));

        root.add("textures", textures);

        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (IOException ioException) {
            ClayiumCore.logger.error(ioException.getMessage());
            return;
        }

        ClayiumCore.logger.info("Generated: " + path);
    }

    public static void genItemJsonSimple(String registerName) {
        Path path = Paths.get(
                "D:\\clayium\\src\\main\\resources\\assets\\clayium\\models\\item\\machine\\" + registerName + ".json");
        if (Files.exists(path)) return;

        JsonObject root = new JsonObject();
        root.addProperty("parent", "clayium:block/machine/" + registerName);

        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (IOException ioException) {
            ClayiumCore.logger.error(ioException.getMessage());
            return;
        }

        ClayiumCore.logger.info("Generated: " + path);
    }

    public static void genStateJsonSimple(String registerName) {
        Path path = Paths
                .get("D:\\clayium\\src\\main\\resources\\assets\\clayium\\blockstates\\" + registerName + ".json");
        if (Files.exists(path)) return;

        JsonObject root = new JsonObject();

        JsonArray multipart = new JsonArray();

        JsonObject element = new JsonObject();

        JsonObject apply = new JsonObject();
        apply.addProperty("model", "clayium:machine/" + registerName);

        element.add("apply", apply);
        multipart.add(element);
        root.add("multipart", multipart);

        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (IOException ioException) {
            ClayiumCore.logger.error(ioException.getMessage());
            return;
        }

        ClayiumCore.logger.info("Generated: " + path);
    }

    @SuppressWarnings("unchecked")
    public static <N extends Number> N readNumeric(JsonElement json, String key,
                                                   Class<N> primitive) throws JsonSyntaxException {
        if (json.isJsonObject() && ((JsonObject) json).has(key)) {
            JsonElement element = ((JsonObject) json).get(key);
            if (!element.isJsonPrimitive()) {
                throw new JsonSyntaxException(
                        "Expected " + key + " to be a Primitive, was " + JsonUtils.toString(element));
            }

            if (primitive == Integer.class) {
                return (N) (Integer) element.getAsInt();
            }
            if (primitive == Long.class) {
                return (N) (Long) element.getAsLong();
            }
            if (primitive == Short.class) {
                return (N) (Short) element.getAsShort();
            }
            if (primitive == Float.class) {
                return (N) (Float) element.getAsFloat();
            }
            if (primitive == Double.class) {
                return (N) (Double) element.getAsDouble();
            }
            if (primitive == Byte.class) {
                return (N) (Byte) element.getAsByte();
            }

            throw new JsonSyntaxException("Unsupported primitive type " + primitive.getName());
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a " + primitive.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public static <N extends Number> N readNumeric(JsonElement json, String key, N fallback) {
        try {
            return (N) readNumeric(json, key, fallback.getClass());
        } catch (JsonSyntaxException e) {
            return fallback;
        }
    }

    public static JsonObject fromRecipe(IRecipeElement recipe, EnumMachineKind machine) {
        JsonObject root = new JsonObject();

        root.addProperty("type", "clayium:add_recipe");
        root.addProperty("machine", machine.getRegisterName());

        JsonArray input = new JsonArray();
        for (Ingredient ing : recipe.getIngredients()) {
            JsonObject elm = new JsonObject();

            if (ing instanceof AmountedIngredient) {
                elm.addProperty("count", ((AmountedIngredient) ing).getAmount());
            }

            input.add(elm);
        }
        root.add("input", input);

        root.addProperty("tier", recipe.getTier());

        JsonArray output = new JsonArray();
        for (ItemStack stack : recipe.getResults()) {
            JsonObject elm = new JsonObject();

            elm.addProperty("type", "minecraft:item");
            elm.addProperty("item", stack.getItem().getRegistryName().toString());
            if (stack.getHasSubtypes()) {
                elm.addProperty("data", stack.getMetadata());
            }
            elm.addProperty("count", stack.getCount());
        }
        root.add("output", output);

        root.addProperty("energy", recipe.getEnergy());
        root.addProperty("time", recipe.getTime());

        return root;
    }
}
