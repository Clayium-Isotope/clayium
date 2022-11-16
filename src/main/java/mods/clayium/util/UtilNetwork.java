package mods.clayium.util;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

public class UtilNetwork {
    public static void sendParticlePacketFromServer(WorldServer server, EnumParticleTypes particle, double posX, double posY, double posZ, int number, double motionX, double motionY, double motionZ, double diffusion, double distance) {
        server.spawnParticle(particle, posX, posY, posZ, number, motionX, motionY, motionZ, diffusion);
    }
}
