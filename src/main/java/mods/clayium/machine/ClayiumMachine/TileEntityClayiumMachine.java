package mods.clayium.machine.ClayiumMachine;

import mods.clayium.component.teField.FieldDelegate;
import mods.clayium.component.teField.FieldLong;
import mods.clayium.component.teField.FieldManager;
import mods.clayium.component.value.ContainClayEnergy;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.Interface.IExternalControl;
import mods.clayium.machine.common.*;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.machine.crafting.RecipeList;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTier;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityClayiumMachine extends TileEntityClayContainer implements IButtonProvider, ITickable, IClayEnergyConsumer, ClayiumRecipeProvider<RecipeElement>, IExternalControl, Machine1To1, FieldDelegate {

    protected EnumMachineKind kind = EnumMachineKind.EMPTY;

    @Override
    public EnumMachineKind getKind() {
        return this.kind;
    }

    private RecipeList<RecipeElement> recipeCards = ClayiumRecipes.EMPTY;

    @Nonnull
    @Override
    public RecipeList<RecipeElement> getRecipeList() {
        return this.recipeCards;
    }

    protected RecipeElement doingRecipe = RecipeElement.flat();

    protected final FieldLong craftTime = new FieldLong();
    protected final FieldLong timeToCraft = new FieldLong();
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
    protected final FieldManager fm = new FieldManager(this.timeToCraft, this.craftTime, this.containEnergy);

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

        this.craftTime.deserializeNBT((NBTTagLong) tagCompound.getTag("CraftTime"));
        this.timeToCraft.deserializeNBT((NBTTagLong) tagCompound.getTag("TimeToCraft"));
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");

        this.containEnergy().deserializeNBT((NBTTagIntArray) tagCompound.getTag("ClayEnergy"));

        setKind(EnumMachineKind.fromName(tagCompound.getString("MachineId")));
        this.doingRecipe = this.getRecipe(tagCompound.getInteger("RecipeHash"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setTag("CraftTime", this.craftTime.serializeNBT());
        tagCompound.setTag("TimeToCraft", this.timeToCraft.serializeNBT());
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);

        tagCompound.setTag("ClayEnergy", this.containEnergy().serializeNBT());

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
            this.containEnergy.add(5L);
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

    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy);
    }

    public void proceedCraft() {
        if (!IClayEnergyConsumer.consumeClayEnergy(this, this.debtEnergy)) return;

        this.craftTime.add(1);
        if (this.craftTime.get() < this.timeToCraft.get()) return;

        UtilTransfer.produceItemStack(this.doingRecipe.getResults().get(0), this.getContainerItemStacks(),
                Machine1To1.PRODUCT, this.getInventoryStackLimit());

        this.craftTime.set(0);
        this.timeToCraft.set(0);
        this.debtEnergy = 0L;
        this.doingRecipe = this.getRecipeList().getFlat();
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
            this.doingRecipe = this.getRecipeList().getFlat();
            return false;
        }

        this.craftTime.set(1);
        this.timeToCraft.set(this.doingRecipe.getTime());
        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients().get(0), this.getContainerItemStacks(),
                Machine1To1.MATERIAL);

        return true;
    }

    @Override
    public FieldManager getDelegate() {
        return this.fm;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        if (this.kind == null) return null;
        return this.kind.getFaceResource();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
