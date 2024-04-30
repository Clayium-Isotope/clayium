package mods.clayium.machine.ClayiumMachine;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.Interface.IExternalControl;
import mods.clayium.machine.common.*;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.ContainClayEnergy;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTier;
import mods.clayium.util.UtilTransfer;

public class TileEntityClayiumMachine extends TileEntityClayContainer
                                      implements IButtonProvider, ITickable, IClayEnergyConsumer,
                                      ClayiumRecipeProvider<RecipeElement>, IExternalControl, Machine1To1 {

    protected EnumMachineKind kind = EnumMachineKind.EMPTY;

    @Override
    public EnumMachineKind getKind() {
        return this.kind;
    }

    private ClayiumRecipe recipeCards = ClayiumRecipes.EMPTY;

    @Override
    public ClayiumRecipe getRecipeCard() {
        return this.recipeCards;
    }

    protected RecipeElement doingRecipe = RecipeElement.flat();

    protected long craftTime;
    protected long timeToCraft;
    protected long debtEnergy;

    @Override
    public boolean isDoingWork() {
        return !this.doingRecipe.isFlat();
    }

    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public float initCraftTime = 1.0F;
    public float initConsumingEnergy = 1.0F;
    protected final ContainClayEnergy containEnergy = new ContainClayEnergy();
    private int clayEnergyStorageSize = 1;

    protected boolean scheduled = true;

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.add(new int[] { Machine1To1.MATERIAL });
        this.listSlotsExport.add(new int[] { Machine1To1.PRODUCT });

        this.maxAutoExtract = new int[] { -1, 1 };
        this.maxAutoInsert = new int[] { -1 };

        this.autoInsert = true;
        this.autoExtract = true;

        this.slotsDrop = new int[] { Machine1To1.MATERIAL, Machine1To1.PRODUCT, this.getEnergySlot() };
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);
    }

    public void setKind(EnumMachineKind kind) {
        this.kind = kind;
        if (kind != null) {
            this.recipeCards = kind.getRecipe();
        }
    }

    @Override
    public boolean hasSpecialDrops() {
        return true;
    }

    @Override
    public void update() {
        super.update();

        if (!this.getWorld().isRemote)
            IExternalControl.update(this);
    }

    @Override
    public boolean isScheduled() {
        return this.scheduled;
    }

    @Override
    public void startWork() {
        this.scheduled = true;
    }

    @Override
    public void stopWork() {
        this.scheduled = false;
    }

    @Override
    public void doWorkOnce() {
        RecipeProvider.update(this);
    }

    @Override
    public void markDirty() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        // world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        // ClayiumMachine.updateBlockState(world, pos);

        super.markDirty();
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.craftTime = tagCompound.getLong("CraftTime");
        this.timeToCraft = tagCompound.getLong("TimeToCraft");
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");

        this.containEnergy().set(tagCompound.getLong("ClayEnergy"));

        setKind(EnumMachineKind.fromName(tagCompound.getString("MachineId")));
        this.doingRecipe = this.getRecipe(tagCompound.getInteger("RecipeHash"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setLong("CraftTime", this.craftTime);
        tagCompound.setLong("TimeToCraft", this.timeToCraft);
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);

        tagCompound.setLong("ClayEnergy", this.containEnergy.get());

        tagCompound.setInteger("RecipeHash", this.doingRecipe.hashCode());

        tagCompound.setString("MachineId", this.kind.getRegisterName());

        return tagCompound;
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        if (!this.isDoingWork()) return ButtonProperty.PERMIT;
        return !this.canProceedCraft() ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return button == 0;
    }

    @Override
    public void pushButton(EntityPlayer playerIn, int button) {
        if (UtilTier.canManufactureCraft(this.getHullTier()) && canPushButton(button) == ButtonProperty.PERMIT) {
            this.containEnergy.increase(5L);
        }
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.containEnergy;
    }

    @Override
    public int getEnergySlot() {
        // if (UtilTier.canManufactureCraft(this.tier)) return -1;
        return 2;
    }

    @Override
    public boolean acceptClayEnergy() {
        return !UtilTier.canManufactureCraft(this.tier);
    }

    @Override
    public int getClayEnergyStorageSize() {
        return this.clayEnergyStorageSize;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {
        this.clayEnergyStorageSize = size;
    }

    @Override
    public RecipeElement getFlat() {
        return RecipeElement.flat();
    }

    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);
    }

    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        this.craftTime++;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStack(this.doingRecipe.getResults().get(0), this.getContainerItemStacks(),
                Machine1To1.PRODUCT, this.getInventoryStackLimit());

        this.craftTime = 0L;
        this.timeToCraft = 0L;
        this.debtEnergy = 0L;
        this.doingRecipe = this.getFlat();
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStack(recipe.getResults().get(0), this.getContainerItemStacks(),
                Machine1To1.PRODUCT, this.getInventoryStackLimit()) > 0;
    }

    public boolean setNewRecipe() {
        this.doingRecipe = getRecipe(getStackInSlot(Machine1To1.MATERIAL));
        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = this.doingRecipe.getEnergy();

        if (!this.canCraft(this.doingRecipe) || !this.canProceedCraft()) {
            this.doingRecipe = this.getFlat();
            return false;
        }

        this.craftTime = 1L;
        this.timeToCraft = this.doingRecipe.getTime();
        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients().get(0), this.getContainerItemStacks(),
                Machine1To1.MATERIAL);

        return true;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return (int) this.timeToCraft;
            case 1:
                return (int) this.craftTime;
            case 2:
                return (int) this.containEnergy().get();
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.timeToCraft = value;
                return;
            case 1:
                this.craftTime = value;
                return;
            case 2:
                this.containEnergy().set(value);
                return;
            default:
                return;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        if (kind == null) return null;
        return this.kind.getFaceResource();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
