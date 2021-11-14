package mods.clayium.item;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilAdvancedTools;
import mods.clayium.util.UtilBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class HarvestCoordClaySteelTools
        implements IHarvestCoord {
    public int maxRange = 2;

    public HarvestCoordClaySteelTools(int maxRange) {
        this.maxRange = maxRange;
    }

    public List<Vec3> getHarvestedCoordList(ItemStack itemstack, int x, int y, int z, Vec3 xxVector, Vec3 yyVector, Vec3 zzVector) {
        List<Vec3> res = new ArrayList<Vec3>();
        NBTTagCompound tag = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
        int mode = tag.hasKey("Mode") ? tag.getInteger("Mode") : 0;
        if (mode == this.maxRange && tag.hasKey("Coords")) {
            NBTTagList coords = tag.getTagList("Coords", 10);
            for (int i = 0; i < coords.tagCount(); i++) {
                NBTTagCompound coord = coords.getCompoundTagAt(i);
                int xx = coord.getInteger("xx");
                int j = coord.getInteger("yy");
                int zz = coord.getInteger("zz");
                res.add(Vec3.createVectorHelper(x + xxVector.xCoord * xx + yyVector.xCoord * j + zzVector.xCoord * zz, y + xxVector.yCoord * xx + yyVector.yCoord * j + zzVector.yCoord * zz, z + xxVector.zCoord * xx + yyVector.zCoord * j + zzVector.zCoord * zz));
            }


            return res;
        }
        int size = (mode < this.maxRange) ? mode : this.maxRange;
        for (int yy = -size; yy <= size; yy++) {
            for (int xx = -size; xx <= size; xx++) {
                for (int zz = 0; zz <= 0; zz++) {
                    res.add(Vec3.createVectorHelper(x + xxVector.xCoord * xx + yyVector.xCoord * yy + zzVector.xCoord * zz, y + xxVector.yCoord * xx + yyVector.yCoord * yy + zzVector.yCoord * zz, z + xxVector.zCoord * xx + yyVector.zCoord * yy + zzVector.zCoord * zz));
                }
            }
        }


        return res;
    }


    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        NBTTagCompound tag = p_77648_1_.hasTagCompound() ? p_77648_1_.getTagCompound() : new NBTTagCompound();
        int mode = tag.hasKey("Mode") ? tag.getInteger("Mode") : 0;
        if (!p_77648_2_.isSneaking()) {
            if (mode == this.maxRange && p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) == Blocks.clay) {
                Vec3[] mat = UtilAdvancedTools.getEigenVectorsInSafe(p_77648_2_);
                if (mat != null) {
                    Vec3[] ev = UtilAdvancedTools.getInverse(mat);
                    NBTTagList coords = new NBTTagList();
                    int range = this.maxRange;
                    for (int x = -range; x <= range; x++) {
                        for (int y = -range; y <= range; y++) {
                            for (int z = -range; z <= range; z++) {
                                if (p_77648_3_.getBlock(p_77648_4_ + x, p_77648_5_ + y, p_77648_6_ + z) == Blocks.clay) {
                                    NBTTagCompound coord = new NBTTagCompound();
                                    coord.setInteger("xx", (int) ((ev[0]).xCoord * x + (ev[1]).xCoord * y + (ev[2]).xCoord * z));
                                    coord.setInteger("yy", (int) ((ev[0]).yCoord * x + (ev[1]).yCoord * y + (ev[2]).yCoord * z));
                                    coord.setInteger("zz", (int) ((ev[0]).zCoord * x + (ev[1]).zCoord * y + (ev[2]).zCoord * z));
                                    coords.appendTag((NBTBase) coord);
                                }
                            }
                        }
                    }
                    tag.setTag("Coords", (NBTBase) coords);
                    p_77648_1_.setTagCompound(tag);
                    if (!p_77648_3_.isRemote)
                        p_77648_2_.addChatMessage((IChatComponent) new ChatComponentText("Customized."));
                    return false;
                }
            }


            if (mode++ >= this.maxRange) mode = 0;
            tag.setInteger("Mode", mode);
            p_77648_1_.setTagCompound(tag);
            if (!p_77648_3_.isRemote)
                p_77648_2_.addChatMessage((IChatComponent) new ChatComponentText("Set mode " + mode + "."));
            return false;
        }
        return (mode == this.maxRange && !ClayiumCore.cfgUtilityMode && UtilBuilder.placeBlockByRightClick(Blocks.clay, 0, p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, 0));
    }
}
