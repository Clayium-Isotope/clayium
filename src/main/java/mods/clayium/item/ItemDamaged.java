package mods.clayium.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mods.clayium.gui.IMultipleRenderIcons;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;


public class ItemDamaged
        extends ItemTiered
        implements IItemExRenderer {
    protected Map<String, Integer> metaMap = new HashMap<String, Integer>();
    private Set<String> displayModeSet = new HashSet<String>();
    private Map<String, Object> iconStringMap = new HashMap<String, Object>();
    private Map<String, IIcon> iiconMap = new HashMap<String, IIcon>();
    private Map<String, Integer> tierMap = new HashMap<String, Integer>();
    private Map<String, List> tooltipMap = new HashMap<String, List>();

    private Map<String, IItemCallback> callbackMap = new HashMap<String, IItemCallback>();


    private IItemCallback defaultCallback;


    protected int renderPass;


    public ItemDamaged addItemList(String itemname, int meta, String iconString, boolean display) {
        this.metaMap.put(itemname, Integer.valueOf(meta));
        if (display)
            this.displayModeSet.add(itemname);
        this.iconStringMap.put(itemname, iconString);
        return this;
    }

    public ItemDamaged addItemList(String itemname, int meta, IMultipleRenderIcons materialicon, boolean display) {
        this.metaMap.put(itemname, Integer.valueOf(meta));
        if (display)
            this.displayModeSet.add(itemname);
        this.iconStringMap.put(itemname, materialicon);
        return this;
    }

    public ItemDamaged addItemList(String itemname, int meta, String iconString) {
        return addItemList(itemname, meta, iconString, true);
    }

    public ItemDamaged addItemList(String itemname, int meta, IMultipleRenderIcons materialicon) {
        return addItemList(itemname, meta, materialicon, true);
    }

    public ItemDamaged addItemList(String itemname, int meta, String iconString, int tier, boolean display) {
        return addItemList(itemname, meta, iconString, display).setTier(itemname, tier);
    }

    public ItemDamaged addItemList(String itemname, int meta, IMultipleRenderIcons materialicon, int tier, boolean display) {
        return addItemList(itemname, meta, materialicon, display).setTier(itemname, tier);
    }

    public ItemDamaged addItemList(String itemname, int meta, String iconString, int tier) {
        return addItemList(itemname, meta, iconString).setTier(itemname, tier);
    }

    public ItemDamaged addItemList(String itemname, int meta, IMultipleRenderIcons materialicon, int tier) {
        return addItemList(itemname, meta, materialicon).setTier(itemname, tier);
    }


    public boolean containsMeta(int meta) {
        return (getItemName(meta) != null);
    }

    public boolean containsKey(String itemname) {
        return this.metaMap.containsKey(itemname);
    }

    public ItemDamaged setTier(String itemname, int tier) {
        this.tierMap.put(itemname, Integer.valueOf(tier));
        return this;
    }

    public int getTier(ItemStack itemstack) {
        String name = getItemName(itemstack);
        return this.tierMap.containsKey(name) ? ((Integer) this.tierMap.get(name)).intValue() : -1;
    }

    public ItemDamaged setCallback(String itemname, IItemCallback itemCallback) {
        this.callbackMap.put(itemname, itemCallback);
        return this;
    }

    public IItemCallback getItemCallback(String itemname) {
        return this.callbackMap.containsKey(itemname) ? this.callbackMap.get(itemname) : this.defaultCallback;
    }

    public IItemCallback getItemCallback(ItemStack itemstack) {
        return getItemCallback(getItemName(itemstack));
    }

    private boolean isDefaultCallback(ItemStack itemstack) {
        return true;
// FIXME       return getItemCallback(getItemName(itemstack)) == defaultCallback;
    }

    public ItemDamaged setToolTip(String itemname, List tooltip) {
        this.tooltipMap.put(itemname, tooltip);
        return this;
    }

    public List getToolTip(String itemname) {
        return this.tooltipMap.get(itemname);
    }

    public ItemDamaged addToolTip(String itemname, Object tooltip) {
        if (!this.tooltipMap.containsKey(itemname)) {
            setToolTip(itemname, new ArrayList());
        }
        List<Object> list = getToolTip(itemname);
        if (list != null)
            list.add(tooltip);
        return this;
    }

    public int getMeta(String itemname) {
        if (!this.metaMap.containsKey(itemname))
            return -1;
        return ((Integer) this.metaMap.get(itemname)).intValue();
    }

    public String getIconString(String itemname) {
        if (!this.iconStringMap.containsKey(itemname))
            return "";
        Object object = this.iconStringMap.get(itemname);
        return (object instanceof String) ? (String) object : "";
    }

    public IIcon getIIcon(String itemname) {
        if (!this.iiconMap.containsKey(itemname))
            return null;
        return this.iiconMap.get(itemname);
    }

    public IMultipleRenderIcons getIMultipleRenderIcons(int meta) {
        Object object = this.iconStringMap.get(getItemName(meta));
        return (object instanceof IMultipleRenderIcons) ? (IMultipleRenderIcons) object : null;
    }


    public ItemStack getItemStack(String itemname) {
        return getItemStack(itemname, 1);
    }

    public ItemStack get(String itemname) {
        return getItemStack(itemname);
    }

    public ItemStack getItemStack(String itemname, int par2) {
        return new ItemStack(this, par2, getMeta(itemname));
    }

    public ItemStack get(String itemname, int par2) {
        return getItemStack(itemname, par2);
    }

    public String getItemName(int meta) {
        Iterator<Map.Entry<String, Integer>> iterator = this.metaMap.entrySet().iterator();

        while (true) {
            if (!iterator.hasNext()) {
                return null;
            }
            Map.Entry<String, Integer> entry = iterator.next();

            if (((Integer) entry.getValue()).intValue() == meta)
                return entry.getKey();
        }
    }

    public String getItemName(ItemStack itemStack) {
        return (itemStack == null) ? null : getItemName(super.getDamage(itemStack));
    }


    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iicon) {
        for (Iterator<Map.Entry<String, Object>> iterator = this.iconStringMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            if (value instanceof String) {
                IIcon icon = iicon.registerIcon("clayium:" + (String) value);
                this.iiconMap.put(entry.getKey(), icon);
                continue;
            }
            if (value instanceof IMultipleRenderIcons) {
                ((IMultipleRenderIcons) value).registerIcons(iicon);
                this.iiconMap.put(entry.getKey(), ((IMultipleRenderIcons) value).getIconFromPass(0));
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return getIIcon(getItemName(meta));
    }


    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        List<Integer> l = new ArrayList<Integer>();
        for (String string : this.displayModeSet) {
            l.add(Integer.valueOf(getMeta(string)));
        }
        Collections.sort(l);
        for (Integer i : l) {
            getSubItems(getItemName(i.intValue()), item, creativeTab, list);
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(String itemname, Item item, CreativeTabs creativeTab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, getMeta(itemname)));
    }


    public int getMetadata(int meta) {
        return meta;
    }


    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName() + "." + getItemName(itemStack);
    }


    public int getColorFromItemStack(ItemStack itemstack, int pass) {
        IMultipleRenderIcons i = getIMultipleRenderIcons(itemstack.getItemDamage());
        return (i == null) ? super.getColorFromItemStack(itemstack, pass) : i.getColorFromPass(pass);
    }


    public int getRenderPasses(int meta) {
        IMultipleRenderIcons i = getIMultipleRenderIcons(meta);
        return (i == null) ? 1 : i.getRenderPasses();
    }


    public boolean requiresMultipleRenderPasses() {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
        IMultipleRenderIcons i = getIMultipleRenderIcons(meta);

        return (i == null) ? super.getIconFromDamageForRenderPass(meta, pass) : i.getIconFromPass(pass);
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemstack, player, list, flag);
        List alist = getToolTip(getItemName(itemstack));
        if (alist != null)
            list.addAll(alist);
    }

    public ItemDamaged() {
        this.renderPass = 0;
        setHasSubtypes(true);
        this.defaultCallback = new ItemCallbackDefault();
    }

    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return super.getSpriteNumber();
    }


    @SideOnly(Side.CLIENT)
    public void preRenderItem(IItemRenderer.ItemRenderType type, ItemStack itemstack, Object... data) {
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        this.renderPass = 0;
    }


    @SideOnly(Side.CLIENT)
    public void postRenderItem(IItemRenderer.ItemRenderType type, ItemStack itemstack, Object... data) {
        GL11.glDisable(3042);
        this.renderPass = 0;
    }


    @SideOnly(Side.CLIENT)
    public void preRenderItemPass(IItemRenderer.ItemRenderType type, ItemStack itemstack, int pass, Object... data) {
        this.renderPass = pass;
    }


    @SideOnly(Side.CLIENT)
    public void postRenderItemPass(IItemRenderer.ItemRenderType type, ItemStack itemstack, int pass, Object... data) {}


    public class ItemCallbackDefault
            implements IItemCallback {
        public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
            return ItemDamaged.this.onItemUse(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }

        public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
            return ItemDamaged.this.getDigSpeed(itemstack, block, metadata);
        }

        public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
            return ItemDamaged.this.onItemRightClick(itemstack, world, player);
        }

        public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
            return ItemDamaged.this.onEaten(itemstack, world, player);
        }

        public boolean hitEntity(ItemStack itemstack, EntityLivingBase entity, EntityLivingBase player) {
            return ItemDamaged.this.hitEntity(itemstack, entity, player);
        }

        public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase player) {
            return ItemDamaged.this.onBlockDestroyed(itemstack, world, block, x, y, z, player);
        }

        public boolean canHarvestBlock(Block block, ItemStack itemstack) {
            return ItemDamaged.this.canHarvestBlock(block, itemstack);
        }

        public int getItemStackLimit(ItemStack itemstack) {
            return ItemDamaged.this.getItemStackLimit(itemstack);
        }

        public Set<String> getToolClasses(ItemStack itemstack) {
            return ItemDamaged.this.getToolClasses(itemstack);
        }

        public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity) {
            return ItemDamaged.this.itemInteractionForEntity(itemstack, player, entity);
        }

        public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemstack) {
            return ItemDamaged.this.doesContainerItemLeaveCraftingGrid(itemstack);
        }

        public ItemStack getContainerItem(ItemStack itemstack) {
            return ItemDamaged.this.getContainerItem(itemstack);
        }

        public boolean hasContainerItem(ItemStack itemstack) {
            return ItemDamaged.this.hasContainerItem(itemstack);
        }

        public void onUpdate(ItemStack itemstack, World world, Entity player, int slot, boolean isEquipped) {
            ItemDamaged.this.onUpdate(itemstack, world, player, slot, isEquipped);
        }

        public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
            ItemDamaged.this.onCreated(itemstack, world, player);
        }

        public EnumAction getItemUseAction(ItemStack itemstack) {
            return ItemDamaged.this.getItemUseAction(itemstack);
        }

        public int getMaxItemUseDuration(ItemStack itemstack) {
            return ItemDamaged.this.getMaxItemUseDuration(itemstack);
        }

        public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int itemInUseCount) {
            ItemDamaged.this.onPlayerStoppedUsing(itemstack, world, player, itemInUseCount);
        }

        public String getPotionEffect(ItemStack itemstack) {
            return ItemDamaged.this.getPotionEffect(itemstack);
        }

        public boolean isPotionIngredient(ItemStack itemstack) {
            return ItemDamaged.this.isPotionIngredient(itemstack);
        }

        public boolean isItemTool(ItemStack itemstack) {
            return ItemDamaged.this.isItemTool(itemstack);
        }

        public boolean getIsRepairable(ItemStack itemstack, ItemStack material) {
            return ItemDamaged.this.getIsRepairable(itemstack, material);
        }

        public Multimap getAttributeModifiers(ItemStack itemstack) {
            return ItemDamaged.this.getAttributeModifiers(itemstack);
        }

        public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
            return ItemDamaged.this.onDroppedByPlayer(itemstack, player);
        }

        public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
            return ItemDamaged.this.onItemUseFirst(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }

        public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
            return ItemDamaged.this.onBlockStartBreak(itemstack, X, Y, Z, player);
        }

        public void onUsingTick(ItemStack itemstack, EntityPlayer player, int count) {
            ItemDamaged.this.onUsingTick(itemstack, player, count);
        }

        public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity) {
            return ItemDamaged.this.onLeftClickEntity(itemstack, player, entity);
        }

        public int getEntityLifespan(ItemStack itemstack, World world) {
            return ItemDamaged.this.getEntityLifespan(itemstack, world);
        }

        public boolean hasCustomEntity(ItemStack itemstack) {
            return ItemDamaged.this.hasCustomEntity(itemstack);
        }

        public Entity createEntity(World world, Entity location, ItemStack itemstack) {
            return ItemDamaged.this.createEntity(world, location, itemstack);
        }

        public boolean onEntityItemUpdate(EntityItem entityItem) {
            return ItemDamaged.this.onEntityItemUpdate(entityItem);
        }

        public float getSmeltingExperience(ItemStack itemstack) {
            return ItemDamaged.this.getSmeltingExperience(itemstack);
        }

        public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
            return ItemDamaged.this.doesSneakBypassUse(world, x, y, z, player);
        }

        public void onArmorTick(World world, EntityPlayer player, ItemStack itemstack) {
            ItemDamaged.this.onArmorTick(world, player, itemstack);
        }

        public boolean isValidArmor(ItemStack itemstack, int armorType, Entity entity) {
            return ItemDamaged.this.isValidArmor(itemstack, armorType, entity);
        }

        public boolean isBookEnchantable(ItemStack itemstack, ItemStack book) {
            return ItemDamaged.this.isBookEnchantable(itemstack, book);
        }

        public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
            return ItemDamaged.this.getArmorTexture(itemstack, entity, slot, type);
        }

        @SideOnly(Side.CLIENT)
        public FontRenderer getFontRenderer(ItemStack itemstack) {
            return ItemDamaged.this.getFontRenderer(itemstack);
        }

        @SideOnly(Side.CLIENT)
        public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, int armorSlot) {
            return ItemDamaged.this.getArmorModel(entityLiving, itemstack, armorSlot);
        }

        public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemstack) {
            return ItemDamaged.this.onEntitySwing(entityLiving, itemstack);
        }

        @SideOnly(Side.CLIENT)
        public void renderHelmetOverlay(ItemStack itemstack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
            ItemDamaged.this.renderHelmetOverlay(itemstack, player, resolution, partialTicks, hasScreen, mouseX, mouseY);
        }

        public boolean showDurabilityBar(ItemStack itemstack) {
            return ItemDamaged.this.showDurabilityBar(itemstack);
        }

        public int getMaxDamage(ItemStack itemstack) {
            return ItemDamaged.this.getMaxDamage(itemstack);
        }

        public boolean isDamaged(ItemStack itemstack) {
            return ItemDamaged.this.isDamaged(itemstack);
        }

        public void setDamage(ItemStack itemstack, int damage) {
            ItemDamaged.this.setDamage(itemstack, damage);
        }

        public int getHarvestLevel(ItemStack itemstack, String toolClass) {
            return ItemDamaged.this.getHarvestLevel(itemstack, toolClass);
        }

        public int getItemEnchantability(ItemStack itemstack) {
            return ItemDamaged.this.getItemEnchantability(itemstack);
        }

        public boolean isBeaconPayment(ItemStack itemstack) {
            return ItemDamaged.this.isBeaconPayment(itemstack);
        }

        public int getDamage(ItemStack itemstack) {
            return ItemDamaged.this.getDamage(itemstack);
        }
    }


    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (isDefaultCallback(itemstack))
            return super.onItemUse(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
        return getItemCallback(itemstack).onItemUse(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        if (isDefaultCallback(itemstack))
            return super.getDigSpeed(itemstack, block, metadata);
        return getItemCallback(itemstack).getDigSpeed(itemstack, block, metadata);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        if (isDefaultCallback(itemstack))
            return super.onItemRightClick(itemstack, world, player);
        return getItemCallback(itemstack).onItemRightClick(itemstack, world, player);
    }

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
        if (isDefaultCallback(itemstack))
            return super.onEaten(itemstack, world, player);
        return getItemCallback(itemstack).onEaten(itemstack, world, player);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entity, EntityLivingBase player) {
        if (isDefaultCallback(itemstack))
            return super.hitEntity(itemstack, entity, player);
        return getItemCallback(itemstack).hitEntity(itemstack, entity, player);
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase player) {
        if (isDefaultCallback(itemstack))
            return super.onBlockDestroyed(itemstack, world, block, x, y, z, player);
        return getItemCallback(itemstack).onBlockDestroyed(itemstack, world, block, x, y, z, player);
    }

    public boolean canHarvestBlock(Block block, ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.canHarvestBlock(block, itemstack);
        return getItemCallback(itemstack).canHarvestBlock(block, itemstack);
    }

    public int getItemStackLimit(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getItemStackLimit(itemstack);
        return getItemCallback(itemstack).getItemStackLimit(itemstack);
    }

    public Set<String> getToolClasses(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getToolClasses(itemstack);
        return getItemCallback(itemstack).getToolClasses(itemstack);
    }

    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity) {
        if (isDefaultCallback(itemstack))
            return super.itemInteractionForEntity(itemstack, player, entity);
        return getItemCallback(itemstack).itemInteractionForEntity(itemstack, player, entity);
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.doesContainerItemLeaveCraftingGrid(itemstack);
        return getItemCallback(itemstack).doesContainerItemLeaveCraftingGrid(itemstack);
    }

    public ItemStack getContainerItem(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getContainerItem(itemstack);
        return getItemCallback(itemstack).getContainerItem(itemstack);
    }

    public boolean hasContainerItem(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.hasContainerItem(itemstack);
        return getItemCallback(itemstack).hasContainerItem(itemstack);
    }

    public void onUpdate(ItemStack itemstack, World world, Entity player, int slot, boolean isEquipped) {
        if (isDefaultCallback(itemstack))
            super.onUpdate(itemstack, world, player, slot, isEquipped);
        getItemCallback(itemstack).onUpdate(itemstack, world, player, slot, isEquipped);
    }

    public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
        if (isDefaultCallback(itemstack))
            super.onCreated(itemstack, world, player);
        getItemCallback(itemstack).onCreated(itemstack, world, player);
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getItemUseAction(itemstack);
        return getItemCallback(itemstack).getItemUseAction(itemstack);
    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getMaxItemUseDuration(itemstack);
        return getItemCallback(itemstack).getMaxItemUseDuration(itemstack);
    }

    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int itemInUseCount) {
        if (isDefaultCallback(itemstack))
            super.onPlayerStoppedUsing(itemstack, world, player, itemInUseCount);
        getItemCallback(itemstack).onPlayerStoppedUsing(itemstack, world, player, itemInUseCount);
    }

    public String getPotionEffect(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getPotionEffect(itemstack);
        return getItemCallback(itemstack).getPotionEffect(itemstack);
    }

    public boolean isPotionIngredient(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.isPotionIngredient(itemstack);
        return getItemCallback(itemstack).isPotionIngredient(itemstack);
    }

    public boolean isItemTool(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.isItemTool(itemstack);
        return getItemCallback(itemstack).isItemTool(itemstack);
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack material) {
        if (isDefaultCallback(itemstack))
            return super.getIsRepairable(itemstack, material);
        return getItemCallback(itemstack).getIsRepairable(itemstack, material);
    }

    public Multimap getAttributeModifiers(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getAttributeModifiers(itemstack);
        return getItemCallback(itemstack).getAttributeModifiers(itemstack);
    }

    public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
        if (isDefaultCallback(itemstack))
            return super.onDroppedByPlayer(itemstack, player);
        return getItemCallback(itemstack).onDroppedByPlayer(itemstack, player);
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (isDefaultCallback(itemstack))
            return super.onItemUseFirst(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
        return getItemCallback(itemstack).onItemUseFirst(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        if (isDefaultCallback(itemstack))
            return super.onBlockStartBreak(itemstack, X, Y, Z, player);
        return getItemCallback(itemstack).onBlockStartBreak(itemstack, X, Y, Z, player);
    }

    public void onUsingTick(ItemStack itemstack, EntityPlayer player, int count) {
        if (isDefaultCallback(itemstack))
            super.onUsingTick(itemstack, player, count);
        getItemCallback(itemstack).onUsingTick(itemstack, player, count);
    }

    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity) {
        if (isDefaultCallback(itemstack))
            return super.onLeftClickEntity(itemstack, player, entity);
        return getItemCallback(itemstack).onLeftClickEntity(itemstack, player, entity);
    }

    public int getEntityLifespan(ItemStack itemstack, World world) {
        if (isDefaultCallback(itemstack))
            return super.getEntityLifespan(itemstack, world);
        return getItemCallback(itemstack).getEntityLifespan(itemstack, world);
    }

    public boolean hasCustomEntity(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.hasCustomEntity(itemstack);
        return getItemCallback(itemstack).hasCustomEntity(itemstack);
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.createEntity(world, location, itemstack);
        return getItemCallback(itemstack).createEntity(world, location, itemstack);
    }

    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (isDefaultCallback(entityItem.getEntityItem()))
            return super.onEntityItemUpdate(entityItem);
        return getItemCallback(entityItem.getEntityItem()).onEntityItemUpdate(entityItem);
    }

    public float getSmeltingExperience(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getSmeltingExperience(itemstack);
        return getItemCallback(itemstack).getSmeltingExperience(itemstack);
    }

    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        if (isDefaultCallback(player.getHeldItem()))
            return super.doesSneakBypassUse(world, x, y, z, player);
        return getItemCallback(player.getHeldItem()).doesSneakBypassUse(world, x, y, z, player);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            super.onArmorTick(world, player, itemstack);
        getItemCallback(itemstack).onArmorTick(world, player, itemstack);
    }

    public boolean isValidArmor(ItemStack itemstack, int armorType, Entity entity) {
        if (isDefaultCallback(itemstack))
            return super.isValidArmor(itemstack, armorType, entity);
        return getItemCallback(itemstack).isValidArmor(itemstack, armorType, entity);
    }

    public boolean isBookEnchantable(ItemStack itemstack, ItemStack book) {
        if (isDefaultCallback(itemstack))
            return super.isBookEnchantable(itemstack, book);
        return getItemCallback(itemstack).isBookEnchantable(itemstack, book);
    }

    public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
        if (isDefaultCallback(itemstack))
            return super.getArmorTexture(itemstack, entity, slot, type);
        return getItemCallback(itemstack).getArmorTexture(itemstack, entity, slot, type);
    }

    @SideOnly(Side.CLIENT)
    public FontRenderer getFontRenderer(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getFontRenderer(itemstack);
        return getItemCallback(itemstack).getFontRenderer(itemstack);
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, int armorSlot) {
        if (isDefaultCallback(itemstack))
            return super.getArmorModel(entityLiving, itemstack, armorSlot);
        return getItemCallback(itemstack).getArmorModel(entityLiving, itemstack, armorSlot);
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.onEntitySwing(entityLiving, itemstack);
        return getItemCallback(itemstack).onEntitySwing(entityLiving, itemstack);
    }

    @SideOnly(Side.CLIENT)
    public void renderHelmetOverlay(ItemStack itemstack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
        if (isDefaultCallback(itemstack))
            super.renderHelmetOverlay(itemstack, player, resolution, partialTicks, hasScreen, mouseX, mouseY);
        getItemCallback(itemstack).renderHelmetOverlay(itemstack, player, resolution, partialTicks, hasScreen, mouseX, mouseY);
    }

    public boolean showDurabilityBar(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.showDurabilityBar(itemstack);
        return getItemCallback(itemstack).showDurabilityBar(itemstack);
    }

    public int getMaxDamage(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getMaxDamage(itemstack);
        return getItemCallback(itemstack).getMaxDamage(itemstack);
    }

    public boolean isDamaged(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.isDamaged(itemstack);
        return getItemCallback(itemstack).isDamaged(itemstack);
    }

    public void setDamage(ItemStack itemstack, int damage) {
        if (isDefaultCallback(itemstack))
            super.setDamage(itemstack, damage);
        getItemCallback(itemstack).setDamage(itemstack, damage);
    }

    public int getHarvestLevel(ItemStack itemstack, String toolClass) {
        if (isDefaultCallback(itemstack))
            return super.getHarvestLevel(itemstack, toolClass);
        return getItemCallback(itemstack).getHarvestLevel(itemstack, toolClass);
    }

    public int getItemEnchantability(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getItemEnchantability(itemstack);
        return getItemCallback(itemstack).getItemEnchantability(itemstack);
    }

    public boolean isBeaconPayment(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.isBeaconPayment(itemstack);
        return getItemCallback(itemstack).isBeaconPayment(itemstack);
    }

    public int getDamage(ItemStack itemstack) {
        if (isDefaultCallback(itemstack))
            return super.getDamage(itemstack);
        return getItemCallback(itemstack).getDamage(itemstack);
    }
}
