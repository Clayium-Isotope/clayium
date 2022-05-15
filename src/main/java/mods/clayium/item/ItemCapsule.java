package mods.clayium.item;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.gui.IMultipleRenderIcons;
import mods.clayium.gui.MultipleIcon;
import mods.clayium.util.UtilFluid;
import mods.clayium.util.crafting.ISpecialResultRecipe;
import mods.clayium.util.crafting.RecipeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;


public class ItemCapsule
        extends ItemDamaged {
    private int capacity = 1000;
    private IMultipleRenderIcons icon;

    public int getCapacity() {
        return this.capacity;
    }

    private static volatile List<ItemCapsule> listCapsules;

    public ItemCapsule(int capacity, IMultipleRenderIcons icon) {
        this.capacity = capacity;
        this.icon = icon;
    }

    public ItemCapsule(int capacity, String iconName) {
        this(capacity, (IMultipleRenderIcons) (new MultipleIcon(3)).addIcons(new String[] {iconName + "_mask", iconName + "_mask", iconName}));
    }


    public ItemStack getCapsule(String fluidname, int stackSize) {
        return (fluidname == null) ? new ItemStack(this, stackSize) : new ItemStack(this, stackSize, UtilFluid.getFluidID(fluidname));
    }

    public ItemStack getCapsule(String fluidname) {
        return getCapsule(fluidname, 1);
    }

    public String getFluidNameFromItemStack(ItemStack itemstack) {
        return (itemstack == null) ? null : UtilFluid.getFluidName(itemstack.getItemDamage());
    }

    public Fluid getFluidFromItemStack(ItemStack itemstack) {
        return (itemstack == null) ? null : UtilFluid.getFluid(itemstack.getItemDamage());
    }


    public FluidStack getFluidStackFromItemStack(ItemStack itemstack) {
        Fluid fluid = getFluidFromItemStack(itemstack);
        return (fluid == null) ? null : new FluidStack(fluid, getCapacity());
    }


    public void registerFluidContainer(boolean display) {
        addItemList(String.valueOf(0), 0, this.icon);
        ItemStack empty = getCapsule((String) null);
        for (String fluidname : FluidRegistry.getRegisteredFluids().keySet()) {
            addItemList(String.valueOf(UtilFluid.getFluidID(fluidname)), UtilFluid.getFluidID(fluidname), this.icon, display);
            ItemStack itemstack = getCapsule(fluidname);
            FluidContainerRegistry.registerFluidContainer(getFluidStackFromItemStack(itemstack), itemstack.copy(), empty.copy());
        }
    }

    public static void registerCompressionRecipe(ItemCapsule[] items, int[] compressionChain) {
        for (int i = 0; i < compressionChain.length; i++)
            registerCompressionRecipe(items[i], items[i + 1], compressionChain[i]);
    }

    public static void registerCompressionRecipe(ItemCapsule ingred, ItemCapsule result, int num) {
        GameRegistry.addRecipe(new RecipeCapsuleCompression(ingred, num, result, 1));
        GameRegistry.addRecipe(new RecipeCapsuleCompression(result, 1, ingred, num));
    }

    public static class RecipeCapsuleCompression
            implements IRecipe, ISpecialResultRecipe {
        ItemCapsule ingred;
        int ingredNum = 9;
        ItemCapsule result;
        int resultNum = 1;


        public RecipeCapsuleCompression(ItemCapsule ingred, int ingredNum, ItemCapsule result, int resultNum) {
            this.ingred = ingred;
            this.ingredNum = ingredNum;
            this.result = result;
            this.resultNum = resultNum;
        }


        public boolean matches(InventoryCrafting inv, World world) {
            return (getCraftingResult(inv) != null);
        }


        public ItemStack getCraftingResult(InventoryCrafting inv) {
            int damage = -1;
            int c = 0;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack ing = inv.getStackInSlot(i);
                if (ing != null) {

                    if (ing.getItem() != this.ingred || (damage != -1 && ing.getItemDamage() != damage))
                        return null;
                    damage = ing.getItemDamage();
                    c++;
                }
            }
            return (c == this.ingredNum) ? getRecipeOutput(damage) : null;
        }


        public int getRecipeSize() {
            return this.ingredNum;
        }


        public ItemStack getRecipeOutput() {
            return new ItemStack(this.result, this.resultNum);
        }

        public ItemStack getRecipeOutput(int damage) {
            return new ItemStack(this.result, this.resultNum, damage);
        }

        public List<ItemStack> getRecipeInput(int damage) {
            List<ItemStack> list = new ArrayList<ItemStack>();
            for (int i = 0; i < this.ingredNum; i++)
                list.add(new ItemStack(this.ingred, 1, damage));
            return list;
        }


        public RecipeMap[] getUsageRecipeMap(ItemStack ingredient) {
            if (ingredient != null && ingredient.getItem() == this.ingred)
                return new RecipeMap[] {new RecipeMap(getRecipeInput(ingredient.getItemDamage()).<ItemStack>toArray(new ItemStack[0]), getRecipeOutput(ingredient.getItemDamage()), "shapeless")};
            return null;
        }


        public RecipeMap[] getCraftingRecipeMap(ItemStack result) {
            if (result != null && result.getItem() == this.result)
                return new RecipeMap[] {new RecipeMap(getRecipeInput(result.getItemDamage()).<ItemStack>toArray(new ItemStack[0]), getRecipeOutput(result.getItemDamage()), "shapeless")};
            return null;
        }
    }


    public static synchronized List<ItemCapsule> getAllCapsules() {
        if (listCapsules == null) {
            listCapsules = new ArrayList<ItemCapsule>();
            for (Item item : GameData.getItemRegistry().typeSafeIterable()) {
                if (item instanceof ItemCapsule)
                    listCapsules.add((ItemCapsule) item);
            }
        }
        return listCapsules;
    }

    public static class RecipeCapsulePackaging
            implements IRecipe, ISpecialResultRecipe {
        public RecipeMap[] getUsageRecipeMap(ItemStack ingredient) {
            if (ingredient == null)
                return null;
            ItemStack ingredtemp = ingredient.copy();
            ingredtemp.stackSize = 1;
            if (ingredtemp.getItem() instanceof ItemCapsule) {
                if (ingredtemp.getItemDamage() == 0) {
                    List<RecipeMap> list = new ArrayList<RecipeMap>();
                    for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                        if (((ItemCapsule) ingredient.getItem()).getCapacity() == data.fluid.amount && data.filledContainer.getItem() instanceof ItemCapsule)
                            list.add(new RecipeMap(new ItemStack[] {data.filledContainer.copy(), ingredtemp.copy()}, new ItemStack(ingredtemp.getItem(), 1, UtilFluid.getFluidID(data.fluid)), "shapeless"));
                    }
                    return list.<RecipeMap>toArray(new RecipeMap[0]);
                }
                return null;
            }
            if (FluidContainerRegistry.isFilledContainer(ingredtemp)) {
                List<RecipeMap> list = new ArrayList<RecipeMap>();
                FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(ingredtemp);
                for (ItemCapsule capsule : ItemCapsule.getAllCapsules()) {
                    if (capsule.getCapacity() == fluid.amount)
                        list.add(new RecipeMap(new ItemStack[] {ingredtemp.copy(), new ItemStack(capsule)}, new ItemStack(capsule, 1, UtilFluid.getFluidID(fluid)), "shapeless"));
                }
                return list.<RecipeMap>toArray(new RecipeMap[0]);
            }
            return null;
        }


        public RecipeMap[] getCraftingRecipeMap(ItemStack result) {
            if (result == null)
                return null;
            if (result.getItem() instanceof ItemCapsule && result.getItemDamage() != 0) {
                List<RecipeMap> list = new ArrayList<RecipeMap>();
                ItemStack resulttemp = result.copy();
                resulttemp.stackSize = 1;
                ItemCapsule capsule = (ItemCapsule) resulttemp.getItem();
                for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                    if (capsule.getCapacity() == data.fluid.amount && resulttemp.getItemDamage() == UtilFluid.getFluidID(data.fluid) && !(data.filledContainer.getItem() instanceof ItemCapsule))
                        list.add(new RecipeMap(new ItemStack[] {data.filledContainer.copy(), new ItemStack(capsule)}, resulttemp.copy(), "shapeless"));
                }
                return list.<RecipeMap>toArray(new RecipeMap[0]);
            }

            return null;
        }


        public boolean matches(InventoryCrafting inv, World p_77569_2_) {
            return (checkPlacedItem(inv) && getFluidContainer(inv) != null && getCapsule(inv) != null);
        }


        public ItemStack getCraftingResult(InventoryCrafting inv) {
            if (!checkPlacedItem(inv))
                return null;
            ItemStack fluidContainer = getFluidContainer(inv);
            if (fluidContainer == null)
                return null;
            FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(fluidContainer);
            if (fluid == null)
                return null;
            ItemStack capsule = getCapsule(inv);
            if (capsule == null)
                return null;
            ItemCapsule itemCapsule = (ItemCapsule) capsule.getItem();
            if (itemCapsule.getCapacity() != fluid.amount)
                return null;
            ItemStack ret = new ItemStack(itemCapsule, 1, UtilFluid.getFluidID(fluid));
            if (fluidContainer.hasTagCompound())
                ret.setTagCompound((NBTTagCompound) fluidContainer.getTagCompound().copy());
            return ret;
        }

        private boolean checkPlacedItem(InventoryCrafting inv) {
            if (inv == null)
                return false;
            ItemStack ret = null;
            int count = 0;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                if (inv.getStackInSlot(i) != null)
                    count++;
                if (count > 2)
                    return false;
            }
            return (count == 2);
        }

        private ItemStack getFluidContainer(InventoryCrafting inv) {
            if (inv == null)
                return null;
            ItemStack ret = null;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item != null && !(item.getItem() instanceof ItemCapsule) && FluidContainerRegistry.isFilledContainer(item)) {
                    if (ret != null)
                        return null;
                    ret = item.copy();
                }
            }
            return ret;
        }

        private ItemStack getCapsule(InventoryCrafting inv) {
            if (inv == null)
                return null;
            ItemStack ret = null;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item != null && item.getItem() instanceof ItemCapsule && item.getItemDamage() == 0) {
                    if (ret != null)
                        return null;
                    ret = item.copy();
                }
            }
            return ret;
        }


        public int getRecipeSize() {
            return 2;
        }


        public ItemStack getRecipeOutput() {
            return null;
        }
    }

    public static class RecipeCapsuleUnpackaging
            implements IRecipe, ISpecialResultRecipe {
        public RecipeMap[] getUsageRecipeMap(ItemStack ingredient) {
            if (ingredient == null)
                return null;
            if (ingredient.getItem() instanceof ItemCapsule) {
                if (ingredient.getItemDamage() != 0) {
                    ItemStack ingredtemp = ingredient.copy();
                    ingredtemp.stackSize = 1;
                    List<RecipeMap> list = new ArrayList<RecipeMap>();
                    for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                        if (((ItemCapsule) ingredient.getItem()).getCapacity() == data.fluid.amount && ingredient.getItemDamage() == UtilFluid.getFluidID(data.fluid) && data.emptyContainer != null && !(data.filledContainer.getItem() instanceof ItemCapsule))
                            list.add(new RecipeMap(new ItemStack[] {data.emptyContainer.copy(), ingredtemp.copy()}, data.filledContainer.copy(), "shapeless"));
                    }
                    return list.<RecipeMap>toArray(new RecipeMap[0]);
                }
                return null;
            }
            return null;
        }


        public RecipeMap[] getCraftingRecipeMap(ItemStack result) {
            if (result == null)
                return null;
            if (!(result.getItem() instanceof ItemCapsule) && FluidContainerRegistry.isFilledContainer(result)) {
                ItemStack empty = FluidContainerRegistry.drainFluidContainer(result);
                if (empty == null)
                    return null;
                List<RecipeMap> list = new ArrayList<RecipeMap>();
                ItemStack resulttemp = result.copy();
                resulttemp.stackSize = 1;
                FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(resulttemp);
                for (ItemCapsule capsule : ItemCapsule.getAllCapsules()) {
                    if (capsule.getCapacity() == fluid.amount)
                        list.add(new RecipeMap(new ItemStack[] {empty.copy(), new ItemStack(capsule, 1, UtilFluid.getFluidID(fluid))}, resulttemp.copy(), "shapeless"));
                }
                return list.<RecipeMap>toArray(new RecipeMap[0]);
            }

            return null;
        }


        public boolean matches(InventoryCrafting inv, World p_77569_2_) {
            return (getCraftingResult(inv) != null);
        }


        public ItemStack getCraftingResult(InventoryCrafting inv) {
            if (!checkPlacedItem(inv))
                return null;
            ItemStack fluidContainer = getFluidContainer(inv);
            if (fluidContainer == null)
                return null;
            ItemStack capsule = getCapsule(inv);
            if (capsule == null)
                return null;
            ItemCapsule itemCapsule = (ItemCapsule) capsule.getItem();
            ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(itemCapsule.getFluidStackFromItemStack(capsule), fluidContainer);
            if (filledContainer == null || itemCapsule.getCapacity() != FluidContainerRegistry.getContainerCapacity(filledContainer))
                return null;
            if (capsule.hasTagCompound())
                filledContainer.setTagCompound((NBTTagCompound) capsule.getTagCompound().copy());
            return filledContainer.copy();
        }

        private boolean checkPlacedItem(InventoryCrafting inv) {
            if (inv == null)
                return false;
            int count = 0;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                if (inv.getStackInSlot(i) != null)
                    count++;
                if (count > 2)
                    return false;
            }
            return (count == 2);
        }

        private ItemStack getFluidContainer(InventoryCrafting inv) {
            if (inv == null)
                return null;
            ItemStack ret = null;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item != null && !(item.getItem() instanceof ItemCapsule) && FluidContainerRegistry.isEmptyContainer(item)) {
                    if (ret != null)
                        return null;
                    ret = item.copy();
                }
            }
            return ret;
        }

        private ItemStack getCapsule(InventoryCrafting inv) {
            if (inv == null)
                return null;
            ItemStack ret = null;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item != null && item.getItem() instanceof ItemCapsule && item.getItemDamage() != 0) {
                    if (ret != null)
                        return null;
                    ret = item.copy();
                }
            }
            return ret;
        }


        public int getRecipeSize() {
            return 2;
        }


        public ItemStack getRecipeOutput() {
            return null;
        }
    }

    public static ItemStack getItemCapsuleFromFluidStack(FluidStack fluid) {
        if (fluid == null)
            return null;
        for (ItemCapsule capsule : getAllCapsules()) {
            if (capsule.getCapacity() == fluid.amount)
                return new ItemStack(capsule, 1, UtilFluid.getFluidID(fluid));
        }
        return null;
    }


    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemstack, player, list, flag);
        Fluid fluid = getFluidFromItemStack(itemstack);
        if (fluid != null) {
            list.add(fluid.getLocalizedName(getFluidStackFromItemStack(itemstack)) + "(" + getFluidNameFromItemStack(itemstack) + ")");
        }
        list.add(getCapacity() + "mB");
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 1) {
            Fluid fluid = getFluidFromItemStack(stack);
            if (fluid != null)
                return fluid.getIcon();
            return Blocks.clay.getIcon(0, 0);
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
    }


    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemstack, int pass) {
        if (pass == 1) {
            Fluid fluid = getFluidFromItemStack(itemstack);
            if (fluid != null)
                return fluid.getColor(getFluidStackFromItemStack(itemstack));
            return 16777215;
        }
        return super.getColorFromItemStack(itemstack, pass);
    }


    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return (this.renderPass == 1) ? 0 : 1;
    }


    @SideOnly(Side.CLIENT)
    public void preRenderItemPass(IItemRenderer.ItemRenderType type, ItemStack itemstack, int pass, Object... data) {
        super.preRenderItemPass(type, itemstack, pass, data);
        if (pass == 1) {
            GL11.glDepthMask(false);
            GL11.glDepthFunc(514);
        }
    }


    @SideOnly(Side.CLIENT)
    public void postRenderItemPass(IItemRenderer.ItemRenderType type, ItemStack itemstack, int pass, Object... data) {
        super.postRenderItemPass(type, itemstack, pass, data);
        if (pass == 1) {
            GL11.glDepthMask(true);
            GL11.glDepthFunc(515);
        }
    }
}
