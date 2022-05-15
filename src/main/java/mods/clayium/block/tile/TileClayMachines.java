package mods.clayium.block.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.IClayContainerModifier;
import mods.clayium.block.IOverclocker;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.Recipes;
import mods.clayium.util.crafting.SimpleMachinesRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;


public class TileClayMachines
        extends TileClayContainerTiered
        implements IExternalControl {
    public long machineCraftTime;
    public long machineTimeToCraft;
    public long machineConsumingEnergy;
    protected String recipeId;
    protected SimpleMachinesRecipes recipe;
    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public float initCraftTime = 1.0F;
    public float initConsumingEnergy = 1.0F;

    public int externalControlState = 0;

    public boolean isDoingWork = false;
    public double overclockTick = 0.0D;
    public double overclockFactor = 1.0D;
    public double overclockTotalFactor = 1.0D;


    private ItemStack oldMaterial;


    public void initParams() {
        super.initParams();
        this.containerItemStacks = new ItemStack[4];
        this.clayEnergySlot = 3;
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsInsert.add(new int[] {3});
        this.listSlotsExtract.add(new int[] {1});

        this.insertRoutes = new int[] {-1, 0, -1, 1, -1, -1};
        this.maxAutoExtract = new int[] {-1, 1};
        this.extractRoutes = new int[] {0, -1, -1, -1, -1, -1};
        this.maxAutoInsert = new int[] {-1};
        this.slotsDrop = new int[] {0, 1, 3};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public void initParamsByTier(int tier) {
        setDefaultTransportationParamsByTier(tier, ParamMode.MACHINE);
    }


    public void setRecipe(String recipeId_) {
        this.recipeId = recipeId_;
        refreshRecipe();
    }

    public String getRecipeId() {
        return this.recipeId;
    }


    public void refreshRecipe() {
        SimpleMachinesRecipes recipe = (SimpleMachinesRecipes) Recipes.GetRecipes(this.recipeId);


        if (recipe != null) this.recipe = recipe;
    }

    public SimpleMachinesRecipes getRecipe() {
        return this.recipe;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);


        this.machineCraftTime = tagCompound.getLong("CraftTime");
        this.machineTimeToCraft = tagCompound.getLong("TimeToCraft");
        this.recipeId = tagCompound.getString("RecipeId");
        this.machineConsumingEnergy = tagCompound.getLong("ConsumingEnergy");
        this.multCraftTime = tagCompound.getFloat("CraftTimeMultiplier");
        this.multConsumingEnergy = tagCompound.getFloat("ConsumingEnergyMultiplier");

        this.initCraftTime = tagCompound.getFloat("CraftTimeInitMultiplier");
        this.initConsumingEnergy = tagCompound.getFloat("ConsumingEnergyInitMultiplier");

        this.externalControlState = tagCompound.getInteger("ExternalControlState");

        this.overclockTick = tagCompound.getDouble("OverclockTick");
        this.overclockFactor = tagCompound.getDouble("OverclockFactor");
        this.overclockTotalFactor = tagCompound.getDouble("OverclockTotalFactor");

        refreshRecipe();
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setLong("CraftTime", this.machineCraftTime);
        tagCompound.setLong("TimeToCraft", this.machineTimeToCraft);
        tagCompound.setString("RecipeId", this.recipeId);
        tagCompound.setLong("ConsumingEnergy", this.machineConsumingEnergy);
        tagCompound.setFloat("CraftTimeMultiplier", this.multCraftTime);
        tagCompound.setFloat("ConsumingEnergyMultiplier", this.multConsumingEnergy);

        tagCompound.setFloat("CraftTimeInitMultiplier", this.initCraftTime);
        tagCompound.setFloat("ConsumingEnergyInitMultiplier", this.initConsumingEnergy);

        tagCompound.setInteger("ExternalControlState", this.externalControlState);

        tagCompound.setDouble("OverclockTick", this.overclockTick);
        tagCompound.setDouble("OverclockFactor", this.overclockFactor);
        tagCompound.setDouble("OverclockTotalFactor", this.overclockTotalFactor);
    }


    @SideOnly(Side.CLIENT)
    public int getCraftProgressScaled(int par1) {
        if (this.machineTimeToCraft == 0L)
            return 0;
        return (int) (this.machineCraftTime * par1 / this.machineTimeToCraft);
    }

    public boolean isCrafting() {
        return (this.machineCraftTime > 0L);
    }

    public void updateEntity() {
        super.updateEntity();
        this.isDoingWork = false;

        UtilDirection[] directions = {UtilDirection.UP, UtilDirection.DOWN, UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.WEST, UtilDirection.EAST};


        this.clayEnergyStorageSize = 1;

        this.overclockTotalFactor = 1.0D;
        for (UtilDirection direction : directions) {
            Block block = this.worldObj.getBlock(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
            if (canOverclock() && block instanceof IOverclocker) {
                this.overclockTotalFactor *= ((IOverclocker) block).getOverclockFactor((IBlockAccess) this.worldObj, this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
            }
            if (block instanceof IClayContainerModifier) {
                ((IClayContainerModifier) block).modifyClayContainer((IBlockAccess) this.worldObj, this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ, this);
            }
        }


        this.overclockFactor = (this.overclockTotalFactor > 10.0D) ? (this.overclockTotalFactor / 10.0D) : 1.0D;
        this.overclockTick += this.overclockTotalFactor / this.overclockFactor;

        this.multCraftTime = (float) (this.initCraftTime / this.overclockFactor);
        this.multConsumingEnergy = (float) (this.initConsumingEnergy * Math.pow(this.overclockFactor, 1.5D));


        while (this.overclockTick >= 1.0D) {
            if (this.externalControlState >= 0 && canProceedCraft()) {
                proceedCraft();
            }
            this.overclockTick--;
        }
    }

    public boolean canOverclock() {
        return true;
    }


    private int oldBaseTier = -1;
    private Recipes.RecipeCondition oldRecipeCondition;
    private Recipes.RecipeResult oldRecipeResult;
    private Recipes.RecipeCondition[] oldRecipeConditions = new Recipes.RecipeCondition[65];
    private Recipes.RecipeResult[] oldRecipeResults = new Recipes.RecipeResult[65];
    private boolean[] refresh = new boolean[65];

    protected Recipes.RecipeCondition getRecipeCondition(ItemStack material, int baseTier) {
        if (material == null)
            return null;
        if (!UtilItemStack.areTypeEqual(this.oldMaterial, material) || this.oldBaseTier != baseTier) {
            this.oldMaterial = material.copy();
            this.oldBaseTier = baseTier;
            for (int i = 0; i < this.oldRecipeConditions.length; i++)
                this.refresh[i] = false;
        }
        int n = Math.min(64, Math.max(material.stackSize, 0));
        if (!this.refresh[n]) {
            Recipes.RecipeCondition newRecipeCondition = this.recipe.getRecipeConditionFromInventory(new ItemStack[] {material}, 0, baseTier);
            if ((newRecipeCondition == null && this.oldRecipeCondition != null) || (newRecipeCondition != null && !newRecipeCondition.equals(this.oldRecipeCondition)))
                this.oldRecipeResult = this.recipe.getRecipeResult(newRecipeCondition);
            this.oldRecipeCondition = newRecipeCondition;

            this.oldRecipeConditions[n] = this.oldRecipeCondition;
            this.oldRecipeResults[n] = this.oldRecipeResult;
            this.refresh[n] = true;
        }

        return this.oldRecipeConditions[n];
    }

    protected Recipes.RecipeResult getRecipeResult(ItemStack material, int baseTier) {
        if (material == null)
            return null;
        getRecipeCondition(material, baseTier);
        return this.oldRecipeResults[Math.min(64, Math.max(material.stackSize, 0))];
    }

    protected long getEnergy(ItemStack material, int baseTier) {
        Recipes.RecipeResult result = getRecipeResult(material, baseTier);
        return (result == null) ? 0L : result.getEnergy();
    }

    protected long getTime(ItemStack material, int baseTier) {
        Recipes.RecipeResult result = getRecipeResult(material, baseTier);
        return (result == null) ? 0L : result.getTime();
    }

    protected int getConsumedStackSize(ItemStack material, int baseTier) {
        Recipes.RecipeCondition cond = getRecipeCondition(material, baseTier);
        if (cond == null)
            return 0;
        int[] i = cond.getStackSizes(new ItemStack[] {material});
        return (i == null || i.length == 0) ? 0 : i[0];
    }

    protected ItemStack getResult(ItemStack material, int baseTier) {
        Recipes.RecipeResult result = getRecipeResult(material, baseTier);
        if (result == null)
            return null;
        ItemStack[] results = result.getResults();
        return (results == null || results.length == 0) ? null : ((results[0] == null) ? null : results[0].copy());
    }


    protected boolean canCraft(ItemStack material) {
        if (material == null || this.recipe == null) {
            return false;
        }

        ItemStack itemstack = getResult(material, this.baseTier);
        if (itemstack == null) return false;
        if (this.containerItemStacks[1] == null) return true;
        if (!this.containerItemStacks[1].isItemEqual(itemstack)) return false;
        int result = (this.containerItemStacks[1]).stackSize + itemstack.stackSize;
        return (result <= getInventoryStackLimit() && result <= this.containerItemStacks[1].getMaxStackSize());
    }


    public boolean canProceedCraft() {
        if (this.containerItemStacks[2] != null) {
            ItemStack itemStack = this.containerItemStacks[2];
            if (canCraft(itemStack)) {
                return true;
            }
            return false;
        }
        ItemStack itemstack = this.containerItemStacks[0];
        if (canCraft(itemstack)) {
            return true;
        }
        return false;
    }


    public int canPushButton() {
        return canProceedCraft() ? 1 : 0;
    }

    public void pushButton() {
        if (canPushButton() == 1) {
            this.clayEnergy += 5L;
        }


        setSyncFlag();
    }

    public void pushButton(EntityPlayer player, int action) {
        pushButton();
    }

    public void proceedCraft() {
        if (this.containerItemStacks[2] == null) {
            this.machineConsumingEnergy = (long) ((float) getEnergy(this.containerItemStacks[0], this.baseTier) * this.multConsumingEnergy);
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {
            if (this.containerItemStacks[2] == null) {
                this.machineTimeToCraft = (long) ((float) getTime(this.containerItemStacks[0], this.baseTier) * this.multCraftTime);
                this.containerItemStacks[2] = this.containerItemStacks[0].splitStack(getConsumedStackSize(this.containerItemStacks[0], this.baseTier));
                if ((this.containerItemStacks[0]).stackSize <= 0) this.containerItemStacks[0] = null;
            }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                ItemStack itemstack = getResult(this.containerItemStacks[2], this.baseTier);
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                if (this.containerItemStacks[1] == null) {
                    this.containerItemStacks[1] = itemstack.copy();
                } else if (this.containerItemStacks[1].getItem() == itemstack.getItem()) {
                    (this.containerItemStacks[1]).stackSize += itemstack.stackSize;
                }
                if (((this.containerItemStacks[2]).stackSize -= getConsumedStackSize(this.containerItemStacks[2], this.baseTier)) <= 0)
                    this.containerItemStacks[2] = null;

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;

                }
            }
        }
    }


    public void openInventory() {}


    public void closeInventory() {}


    public String getNEIOutputId() {
        return getRecipeId();
    }


    public void doWorkOnce() {
        if (this.externalControlState > 0) {
            this.externalControlState++;
        } else {
            this.externalControlState = 1;
        }
    }

    public void startWork() {
        this.externalControlState = 0;
    }

    public void stopWork() {
        this.externalControlState = -1;
    }

    public boolean isScheduled() {
        return canProceedCraft();
    }

    public boolean isDoingWork() {
        return this.isDoingWork;
    }
}
