package mods.clayium.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public interface IItemExRenderer {
    @SideOnly(Side.CLIENT)
    void preRenderItem(IItemRenderer.ItemRenderType paramItemRenderType, ItemStack paramItemStack, Object... paramVarArgs);

    @SideOnly(Side.CLIENT)
    void postRenderItem(IItemRenderer.ItemRenderType paramItemRenderType, ItemStack paramItemStack, Object... paramVarArgs);

    @SideOnly(Side.CLIENT)
    void preRenderItemPass(IItemRenderer.ItemRenderType paramItemRenderType, ItemStack paramItemStack, int paramInt, Object... paramVarArgs);

    @SideOnly(Side.CLIENT)
    void postRenderItemPass(IItemRenderer.ItemRenderType paramItemRenderType, ItemStack paramItemStack, int paramInt, Object... paramVarArgs);
}
