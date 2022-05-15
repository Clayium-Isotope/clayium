package mods.clayium.plugin.multipart;

import codechicken.lib.vec.BlockCoord;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.McBlockPart;
import mods.clayium.block.CBlocks;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class UtilMultipart {
    public static boolean containsPANConductor(IBlockAccess world, int x, int y, int z) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (te instanceof TileMultipart) {
            TileMultipart tm = (TileMultipart) te;
            for (TMultiPart part : tm.jPartList()) {
                if (part instanceof McBlockPart && ((McBlockPart) part).getBlock() instanceof mods.clayium.block.IPANConductor)
                    return true;
            }
        }
        return false;
    }


    public static boolean onItemBlockMultipartUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if (p_77648_1_.stackSize == 0) {
            return false;
        }

        if (placeMultipart(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_1_, p_77648_7_, !p_77648_3_.isRemote)) {

            UtilBuilder.postBlockPlace(((ItemBlock) p_77648_1_.getItem()).field_150939_a, p_77648_3_, p_77648_1_, p_77648_4_, p_77648_5_, p_77648_6_, 1);
            return true;
        }

        int[] coord = UtilBuilder.coordTransformOnPlaceBlock(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_);
        p_77648_4_ = coord[0];
        p_77648_5_ = coord[1];
        p_77648_6_ = coord[2];
        p_77648_7_ = coord[3];

        if (placeMultipart(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_1_, p_77648_7_, !p_77648_3_.isRemote)) {

            UtilBuilder.postBlockPlace(((ItemBlock) p_77648_1_.getItem()).field_150939_a, p_77648_3_, p_77648_1_, p_77648_4_, p_77648_5_, p_77648_6_, 1);
            return true;
        }
        return false;
    }

    public static boolean placeMultipart(World world, int x, int y, int z, ItemStack item, int side, boolean place) {
        TMultiPart part = createPart(world, x, y, z, item, side);
        if (part == null) {
            return false;
        }
        BlockCoord coord = new BlockCoord(x, y, z);
        TileMultipart tile = TileMultipart.getOrConvertTile(world, coord);
        if (tile == null) {
            return false;
        }
        if (tile.canAddPart(part)) {
            if (place)
                TileMultipart.addPart(world, coord, part);
            return true;
        }
        return false;
    }

    public static TMultiPart createPart(World world, int x, int y, int z, ItemStack item, int side) {
        if (item == null)
            return null;
        Item i = item.getItem();
        if (!(i instanceof ItemBlock))
            return null;
        Block b = ((ItemBlock) i).field_150939_a;

        if (b == CBlocks.blockPANCable)
            return new PANCablePart();
        return null;
    }


    public static void registerMicroBlock(Block block, int meta) {
        BlockMicroMaterial.createAndRegister(block, meta);
    }
}
