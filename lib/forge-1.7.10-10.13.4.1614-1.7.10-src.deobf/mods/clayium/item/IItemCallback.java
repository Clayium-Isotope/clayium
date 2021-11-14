package mods.clayium.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemCallback {
    void onUpdate(ItemStack paramItemStack, World paramWorld, Entity paramEntity, int paramInt, boolean paramBoolean);

    int getItemStackLimit(ItemStack paramItemStack);

    boolean onItemUseFirst(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, float paramFloat3);

    boolean onItemUse(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, World paramWorld, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, float paramFloat3);

    boolean doesSneakBypassUse(World paramWorld, int paramInt1, int paramInt2, int paramInt3, EntityPlayer paramEntityPlayer);

    boolean itemInteractionForEntity(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, EntityLivingBase paramEntityLivingBase);

    ItemStack onItemRightClick(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer);

    boolean canHarvestBlock(Block paramBlock, ItemStack paramItemStack);

    float getDigSpeed(ItemStack paramItemStack, Block paramBlock, int paramInt);

    boolean onBlockStartBreak(ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3, EntityPlayer paramEntityPlayer);

    boolean onBlockDestroyed(ItemStack paramItemStack, World paramWorld, Block paramBlock, int paramInt1, int paramInt2, int paramInt3, EntityLivingBase paramEntityLivingBase);

    boolean onLeftClickEntity(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, Entity paramEntity);

    boolean hitEntity(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase1, EntityLivingBase paramEntityLivingBase2);

    boolean onEntitySwing(EntityLivingBase paramEntityLivingBase, ItemStack paramItemStack);

    EnumAction getItemUseAction(ItemStack paramItemStack);

    int getMaxItemUseDuration(ItemStack paramItemStack);

    void onUsingTick(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, int paramInt);

    void onPlayerStoppedUsing(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, int paramInt);

    ItemStack onEaten(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer);

    Set<String> getToolClasses(ItemStack paramItemStack);

    int getHarvestLevel(ItemStack paramItemStack, String paramString);

    Multimap getAttributeModifiers(ItemStack paramItemStack);

    boolean isItemTool(ItemStack paramItemStack);

    boolean getIsRepairable(ItemStack paramItemStack1, ItemStack paramItemStack2);

    int getItemEnchantability(ItemStack paramItemStack);

    boolean showDurabilityBar(ItemStack paramItemStack);

    int getMaxDamage(ItemStack paramItemStack);

    int getDamage(ItemStack paramItemStack);

    boolean isDamaged(ItemStack paramItemStack);

    void setDamage(ItemStack paramItemStack, int paramInt);

    boolean doesContainerItemLeaveCraftingGrid(ItemStack paramItemStack);

    ItemStack getContainerItem(ItemStack paramItemStack);

    boolean hasContainerItem(ItemStack paramItemStack);

    void onCreated(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer);

    boolean onDroppedByPlayer(ItemStack paramItemStack, EntityPlayer paramEntityPlayer);

    int getEntityLifespan(ItemStack paramItemStack, World paramWorld);

    boolean hasCustomEntity(ItemStack paramItemStack);

    Entity createEntity(World paramWorld, Entity paramEntity, ItemStack paramItemStack);

    boolean onEntityItemUpdate(EntityItem paramEntityItem);

    boolean isValidArmor(ItemStack paramItemStack, int paramInt, Entity paramEntity);

    void onArmorTick(World paramWorld, EntityPlayer paramEntityPlayer, ItemStack paramItemStack);

    String getArmorTexture(ItemStack paramItemStack, Entity paramEntity, int paramInt, String paramString);

    @SideOnly(Side.CLIENT)
    ModelBiped getArmorModel(EntityLivingBase paramEntityLivingBase, ItemStack paramItemStack, int paramInt);

    @SideOnly(Side.CLIENT)
    void renderHelmetOverlay(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, ScaledResolution paramScaledResolution, float paramFloat, boolean paramBoolean, int paramInt1, int paramInt2);

    String getPotionEffect(ItemStack paramItemStack);

    boolean isPotionIngredient(ItemStack paramItemStack);

    float getSmeltingExperience(ItemStack paramItemStack);

    boolean isBookEnchantable(ItemStack paramItemStack1, ItemStack paramItemStack2);

    boolean isBeaconPayment(ItemStack paramItemStack);

    @SideOnly(Side.CLIENT)
    FontRenderer getFontRenderer(ItemStack paramItemStack);
}
