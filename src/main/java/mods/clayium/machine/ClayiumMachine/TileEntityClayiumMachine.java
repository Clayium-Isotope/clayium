package mods.clayium.machine.ClayiumMachine;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayiumMachine extends TileEntityClayContainer implements IHasButton, ITickable {
    public enum MachineSlots {
        MATERIAL,
        PRODUCT,
        ENERGY
    }

    protected EnumMachineKind kind;
    private ClayiumRecipe recipeCards;
    public RecipeElement doingRecipe = RecipeElement.FLAT;

    private long craftTime;
    private long timeToCraft;
    private long debtEnergy;

    public long containEnergy;

    public TileEntityClayiumMachine() {
        this.containerItemStacks = NonNullList.withSize(MachineSlots.values().length, ItemStack.EMPTY);

        this.listSlotsImport.add(new int[]{MachineSlots.MATERIAL.ordinal()});
        this.listSlotsExport.add(new int[]{MachineSlots.PRODUCT.ordinal()});

        this.maxAutoExtract = new int[]{-1, 1};
        this.maxAutoInsert = new int[]{-1};

        this.autoInsert = true;
        this.autoExtract = true;

        this.clayEnergySlot = -1;
    }

    @Override
    public void initByTier(int tier) {
        super.initByTier(tier);

        if (UtilTier.canManufactureCraft(this.tier))
            this.clayEnergySlot = -1;
        else
            this.clayEnergySlot = MachineSlots.ENERGY.ordinal();
    }

    public void setKind(EnumMachineKind kind) {
        this.kind = kind;
        if (kind != null) {
            this.recipeCards = kind.getRecipe();
        }
    }

    public ItemStack getStackInSlot(MachineSlots index) {
        return super.getStackInSlot(index.ordinal());
    }

    @Override
    public boolean hasSpecialDrops() {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);

        drops.add(getStackInSlot(MachineSlots.MATERIAL));
        drops.add(getStackInSlot(MachineSlots.PRODUCT));
        drops.add(getStackInSlot(this.clayEnergySlot));
    }

    @Override
    public void update() {
        super.update();

        if (this.doingRecipe == RecipeElement.FLAT) {
            setNewRecipe();
        } else if (compensateClayEnergy(this.debtEnergy)) {
            proceedCraft();
        }
    }

    private void sendUpdate() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        mods.clayium.machine.ClayiumMachine.ClayiumMachine.updateBlockState(world, pos);

        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        this.craftTime = tagCompound.getLong("CraftTime");
        this.timeToCraft = tagCompound.getLong("TimeToCraft");
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");

        this.containEnergy = tagCompound.getLong("ClayEnergy");

        setKind(EnumMachineKind.fromName(tagCompound.getString("MachineId")));
        this.doingRecipe = ClayiumRecipes.getRecipeElement(this.recipeCards, tagCompound.getInteger("RecipeHash"));

        super.readFromNBT(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setLong("CraftTime", this.craftTime);
        tagCompound.setLong("TimeToCraft", this.timeToCraft);
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);

        tagCompound.setLong("ClayEnergy", this.containEnergy);

        tagCompound.setInteger("RecipeHash", this.doingRecipe.hashCode());

        tagCompound.setString("MachineId", this.kind.getRegisterName());

        return tagCompound;
    }

    // 返り値がint型なので、引数に取る
    @SideOnly(Side.CLIENT)
    public int getCraftProgressScaled(int par1) {
        if (this.timeToCraft == 0L) return 0;
        return (int) (this.craftTime * par1 / this.timeToCraft);
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        if (this.doingRecipe != RecipeElement.FLAT) return ButtonProperty.PERMIT;
        return canProceedCraft() ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return button == 0;
    }

    @Override
    public void pushButton(EntityPlayer playerIn, int button) {
        if (UtilTier.canManufactureCraft(this.tier) && canPushButton(button) == ButtonProperty.PERMIT) {
            this.containEnergy += 5L;
        }
    }

    public boolean compensateClayEnergy(long debt) {
        return compensateClayEnergy(debt, true);
    }

    public boolean compensateClayEnergy(long debt, boolean doConsume) {
        if (debt > this.containEnergy) {
            if (!produceClayEnergy()) return false;

            return compensateClayEnergy(debt, doConsume);
        }

        if (doConsume) this.containEnergy -= debt;
        return true;
    }

    public boolean produceClayEnergy() {
        if (this.clayEnergySlot < 0 || this.clayEnergySlot >= getSizeInventory()) return false;
        ItemStack itemstack = getStackInSlot(this.clayEnergySlot);
        if (itemstack.isEmpty()) return false;

        if (!hasClayEnergy(itemstack)) return false;
        containEnergy += getClayEnergy(itemstack);
        itemstack.shrink(1);

        return true;
    }

    public void proceedCraft() {
        this.craftTime++;
        if (this.craftTime < this.timeToCraft) return;

        ItemStack itemstack = this.doingRecipe.getResult().getResults().get(0);

        if (getStackInSlot(MachineSlots.PRODUCT).isEmpty()) {
            setInventorySlotContents(MachineSlots.PRODUCT.ordinal(), itemstack.copy());
        } else if (getStackInSlot(MachineSlots.PRODUCT).isItemEqual(itemstack)) {
            getStackInSlot(MachineSlots.PRODUCT).grow(itemstack.getCount());
        }

        setNewRecipe();
    }

    public RecipeElement getRecipe(ItemStack stack) {
        return recipeCards.getRecipe(stack, tier);
    }

    protected boolean canCraft(ItemStack material) {
        if (material.isEmpty()) return false;

        return canCraft(getRecipe(material));
    }

    protected boolean canCraft(RecipeElement recipe) {
        ItemStack itemstack = recipe.getResult().getResults().get(0);
        if (itemstack.isEmpty()) return false;
        if (getStackInSlot(MachineSlots.PRODUCT).isEmpty()) return true;
        if (!getStackInSlot(MachineSlots.PRODUCT).isItemEqual(itemstack)) return false;

        int result = getStackInSlot(MachineSlots.PRODUCT).getCount() + itemstack.getCount();
        return result <= getInventoryStackLimit()
                && result <= getStackInSlot(MachineSlots.PRODUCT).getMaxStackSize();
    }

    public boolean canProceedCraft() {
        return canCraft(getStackInSlot(MachineSlots.MATERIAL));
    }

    /**
     * これを呼ぶとき、{@link TileEntityClayiumMachine#doingRecipe}は{@link RecipeElement#FLAT}かそれに準ずる状態にあるべき。
     * @return 何かに使えるかもしれないので、成功でtrue、失敗でfalseを返す。
     */
    private boolean setNewRecipe() {
        RecipeElement _recipe = getRecipe(getStackInSlot(MachineSlots.MATERIAL));

        this.craftTime = 0L;

        if (canCraft(_recipe) && compensateClayEnergy(_recipe.getResult().getEnergy())) {
            this.doingRecipe = _recipe;

            this.debtEnergy = this.doingRecipe.getResult().getEnergy();
            this.timeToCraft = this.doingRecipe.getResult().getTime();
            getStackInSlot(MachineSlots.MATERIAL).shrink(this.doingRecipe.getCondition()
                    .getStackSizes(getStackInSlot(MachineSlots.MATERIAL))[0]);

            proceedCraft();
            return true;
        }

        this.timeToCraft = 0L;
        this.debtEnergy = 0L;
        this.doingRecipe = RecipeElement.FLAT;
        return false;
    }
}
