package mods.clayium.util;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

public class UtilPlayer {
    private static EntityPlayer defaultPlayer = null;

    public static EntityPlayer getFakePlayer(String id) {
        EntityPlayerMP ret = null;
        if (id != null || defaultPlayer == null) {
            ret = FakePlayerFactory.get(DimensionManager.getWorld(0), getGameProfile(id));
            ret.eyeHeight = 0.0F;
        }

        if (id != null) {
            return ret;
        }

        if (ret != null) {
            defaultPlayer = ret;
        }

        return defaultPlayer;
    }

    public static GameProfile getGameProfile(String id) {
        if (id == null) {
            id = "[Clayium]";
        }

        return new GameProfile(UUID.nameUUIDFromBytes(id.getBytes(Charsets.UTF_8)), id);
    }
}
