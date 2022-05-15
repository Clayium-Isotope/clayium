package mods.clayium.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IHarvestCoord {
    List<Vec3> getHarvestedCoordList(ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3, Vec3 paramVec31, Vec3 paramVec32, Vec3 paramVec33);

    boolean onItemUse(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, float paramFloat3);
}
