package mods.clayium.item;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.util.UtilAdvancedTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HarvestCoordClaySteelTools implements IHarvestCoord {
    public int maxRange = 2;

    public HarvestCoordClaySteelTools(int maxRange) {
        this.maxRange = maxRange;
    }

    @Override
    public List<BlockPos> getHarvestedCoordList(ItemStack itemstack, BlockPos blockPos, Vec3i vec31, Vec3i vec32, Vec3i vec33) {
        List<BlockPos> res = new ArrayList<>();
        NBTTagCompound tag = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
        int mode = tag.hasKey("Mode") ? tag.getInteger("Mode") : 0;

        if (mode == this.maxRange && tag.hasKey("Coords")) {
            for (NBTBase coord : tag.getTagList("Coords", 11)) {
                if (coord instanceof NBTTagIntArray) {
                    int[] arr = ((NBTTagIntArray) coord).getIntArray();
                    res.add(new BlockPos(
                            blockPos.getX() + vec31.getX() * arr[0] + vec32.getX() * arr[1] + vec33.getX() * arr[2],
                            blockPos.getY() + vec31.getY() * arr[0] + vec32.getY() * arr[1] + vec33.getY() * arr[2],
                            blockPos.getZ() + vec31.getZ() * arr[0] + vec32.getZ() * arr[1] + vec33.getZ() * arr[2]));
                }
            }

//            NBTTagList coords = tag.getTagList("Coords", 10);
//            for (int i = 0; i < coords.tagCount(); i++) {
//                NBTTagCompound coord = coords.getCompoundTagAt(i);
//                int xx = coord.getInteger("xx");
//                int yy = coord.getInteger("yy");
//                int zz = coord.getInteger("zz");
//                res.add(new BlockPos(
//                        blockPos.getX() + vec31.getX() * xx + vec32.getX() * yy + vec33.getX() * zz,
//                        blockPos.getY() + vec31.getY() * xx + vec32.getY() * yy + vec33.getY() * zz,
//                        blockPos.getZ() + vec31.getZ() * xx + vec32.getZ() * yy + vec33.getZ() * zz));
//            }

            return res;
        }

        int size = Math.min(mode, this.maxRange);
        for (int yy = -size; yy <= size; yy++) {
            for (int xx = -size; xx <= size; xx++) {
                for (int zz = 0; zz <= 0; zz++) {
                    res.add(new BlockPos(
                            blockPos.getX() + vec31.getX() * xx + vec32.getX() * yy + vec33.getX() * zz,
                            blockPos.getY() + vec31.getY() * xx + vec32.getY() * yy + vec33.getY() * zz,
                            blockPos.getZ() + vec31.getZ() * xx + vec32.getZ() * yy + vec33.getZ() * zz));
                }
            }
        }

        return res;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        NBTTagCompound tag = player.getHeldItem(hand).hasTagCompound() ? player.getHeldItem(hand).getTagCompound() : new NBTTagCompound();
        int mode = tag.hasKey("Mode") ? tag.getInteger("Mode") : 0;

        if (player.isSneaking()) {
            if (!ClayiumConfiguration.cfgUtilityMode) {
                player.getHeldItem(hand).damageItem(1, player);
                return new ItemBlock(Blocks.CLAY).onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }
            return EnumActionResult.FAIL;
        }

        if (mode == this.maxRange && worldIn.getBlockState(pos).getBlock() == Blocks.CLAY) {
            NBTTagList coords = new NBTTagList();

            for (int x = -maxRange; x <= maxRange; x++) {
                for (int y = -maxRange; y <= maxRange; y++) {
                    for (int z = -maxRange; z <= maxRange; z++) {
                        if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() == Blocks.CLAY) {
                            Vec3i[] eigen = UtilAdvancedTools.getEigenVectors(player, facing);
                            NBTTagIntArray coord = new NBTTagIntArray(new int[] {
                                    eigen[0].getX() * x + eigen[1].getX() * y + eigen[2].getX() * z,
                                    eigen[0].getY() * x + eigen[1].getY() * y + eigen[2].getY() * z,
                                    eigen[0].getZ() * x + eigen[1].getZ() * y + eigen[2].getZ() * z
                            });
                            coords.appendTag(coord);
                        }
                    }
                }
            }

//            Vec3i[] ev = UtilAdvancedTools.getInverse(UtilAdvancedTools.getEigenVectors(player, facing));
//
//            for (int x = -maxRange; x <= maxRange; x++) {
//                for (int y = -maxRange; y <= maxRange; y++) {
//                    for (int z = -maxRange; z <= maxRange; z++) {
//                        if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() == Blocks.CLAY) {
//                            NBTTagCompound coord = new NBTTagCompound();
//                            coord.setInteger("xx", ev[0].getX() * x + ev[1].getX() * y + ev[2].getX() * z);
//                            coord.setInteger("yy", ev[0].getY() * x + ev[1].getY() * y + ev[2].getY() * z);
//                            coord.setInteger("zz", ev[0].getZ() * x + ev[1].getZ() * y + ev[2].getZ() * z);
//                            coords.appendTag(coord);
//                        }
//                    }
//                }
//            }

            tag.setTag("Coords", coords);
            player.getHeldItem(hand).setTagCompound(tag);
            if (!worldIn.isRemote) {
                player.sendMessage(new TextComponentString("Customized."));
            }
            return EnumActionResult.FAIL;
        }

        if (mode++ >= this.maxRange) mode = 0;
        tag.setInteger("Mode", mode);
        player.getHeldItem(hand).setTagCompound(tag);
        if (!worldIn.isRemote)
            player.sendMessage(new TextComponentString("Set mode " + mode + "."));
        return EnumActionResult.FAIL;
    }
}
