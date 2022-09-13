package mods.clayium.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.EnumMachineKind;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonHelper {
    public static void genItemJson(TierPrefix tier, EnumMachineKind kind, String registerName) {
        Path path = Paths.get("D:\\clayium\\src\\main\\resources\\assets\\clayium\\models\\item\\machine\\" + registerName + ".json");
        if (Files.exists(path)) return;

        JsonObject root = new JsonObject();
        root.addProperty("parent", "clayium:block/machine_temp");

        JsonObject textures = new JsonObject();
        textures.addProperty("hull", "clayium:blocks/machinehull-" + (tier.ordinal() - 1));
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
}
