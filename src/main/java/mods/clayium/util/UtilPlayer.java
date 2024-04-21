package mods.clayium.util;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class UtilPlayer {
    private static final GameProfile defaultProfile = new GameProfile(UUID.nameUUIDFromBytes("[Clayium]".getBytes(Charsets.UTF_8)), "[Clayium]");
    private static final EntityPlayer defaultPlayer = FakePlayerFactory.get(DimensionManager.getWorld(0), defaultProfile);

    @Nonnull
    public static EntityPlayer getFakePlayer(@Nullable String id) {
        if (id == null) {
            return defaultPlayer;
        }

        EntityPlayerMP ret = FakePlayerFactory.get(DimensionManager.getWorld(0), getGameProfile(id));
        ret.eyeHeight = 0.0F;

        return ret;
    }

    @Nonnull
    public static GameProfile getGameProfile(@Nullable String id) {
        if (id == null) {
            return defaultProfile;
        }

        return new GameProfile(UUID.nameUUIDFromBytes(id.getBytes(Charsets.UTF_8)), id);
    }

    @Nonnull
    public static EntityPlayer getDefaultFake() {
        return defaultPlayer;
    }
}
