package mods.clayium.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.IWorldGenerator;

import mods.clayium.block.ClayiumBlocks;

public class ClayOreGenerator implements IWorldGenerator {

    static int clayOreVeinNumber = 8;
    static int clayOreVeinSize = 24;
    static int clayOreVeinMinY = 24;
    static int clayOreVeinMaxY = 88;
    static boolean generateDenseClayOreVein = true;
    static int denseClayOreVeinSize = 10;
    static int largeDenseClayOreVeinNumber = 2;
    static int largeDenseClayOreVeinSize = 6;
    static int largeDenseClayOreVeinMinY = 10;
    static int largeDenseClayOreVeinMaxY = 16;

    public static void loadConfig(Configuration cfg) {
        String category = "worldgen";
        clayOreVeinNumber = cfg.getInt("ClayOreVeinNumber", category, 8, 0, 99, "Number of Clay Ore Veins per Chunk");
        clayOreVeinSize = cfg.getInt("ClayOreVeinSize", category, 24, 0, 50, "");
        clayOreVeinMinY = cfg.getInt("ClayOreVeinMinY", category, 24, 0, 255, "");
        clayOreVeinMaxY = cfg.getInt("ClayOreVeinMaxY", category, 88, 0, 255, "");
        generateDenseClayOreVein = cfg.getBoolean("GenerateDenseClayOreVein", category, true, "");
        denseClayOreVeinSize = cfg.getInt("DenseClayOreVeinSize", category, 10, 0, 50, "");
        largeDenseClayOreVeinNumber = cfg.getInt("LargeDenseClayOreVeinNumber", category, 2, 0, 99,
                "Number of Large Dense Clay Ore Veins per Chunk");
        largeDenseClayOreVeinSize = cfg.getInt("LargeDenseClayOreVeinSize", category, 6, 0, 50, "");
        largeDenseClayOreVeinMinY = cfg.getInt("LargeDenseClayOreVeinMinY", category, 10, 0, 255, "");
        largeDenseClayOreVeinMaxY = cfg.getInt("LargeDenseClayOreVeinMaxY", category, 16, 0, 255, "");
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        if (world.provider instanceof WorldProviderSurface) {
            generateOre(world, random, new ChunkPos(chunkX << 4, chunkZ << 4));
        }
    }

    private void generateOre(World world, Random random, ChunkPos chunk) {
        for (int i = 0; i < clayOreVeinNumber; ++i) {
            BlockPos pos = new BlockPos(
                    chunk.x + random.nextInt(16),
                    clayOreVeinMinY + random.nextInt(clayOreVeinMaxY - clayOreVeinMinY),
                    chunk.z + random.nextInt(16));

            new WorldGenMinable(ClayiumBlocks.clayOre.getDefaultState(), clayOreVeinSize,
                    p -> p == Blocks.STONE.getDefaultState())
                            .generate(world, random, pos);

            if (generateDenseClayOreVein) {
                new WorldGenMinable(ClayiumBlocks.denseClayOre.getDefaultState(), denseClayOreVeinSize,
                        p -> p == ClayiumBlocks.clayOre.getDefaultState())
                                .generate(world, random, pos);

                if (world.getBlockState(pos.add(8, 0, 8)).getBlock() == ClayiumBlocks.denseClayOre &&
                        random.nextInt(2) == 0) {
                    world.setBlockState(pos.add(8, 0, 8), ClayiumBlocks.largeDenseClayOre.getDefaultState(), 2);
                }
            }
        }

        for (int i = 0; i < largeDenseClayOreVeinNumber; ++i) {
            BlockPos pos = new BlockPos(
                    chunk.x + random.nextInt(16),
                    largeDenseClayOreVeinMinY + random.nextInt(largeDenseClayOreVeinMaxY - largeDenseClayOreVeinMinY),
                    chunk.z + random.nextInt(16));

            new WorldGenMinable(ClayiumBlocks.largeDenseClayOre.getDefaultState(), largeDenseClayOreVeinSize,
                    p -> p == Blocks.STONE.getDefaultState())
                            .generate(world, random, pos);
        }
    }
}
