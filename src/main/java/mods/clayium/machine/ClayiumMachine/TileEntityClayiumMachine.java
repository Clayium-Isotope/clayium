package mods.clayium.machine.ClayiumMachine;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IRecipeProvider;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTier;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityClayiumMachine extends TileEntityClayContainer implements IButtonProvider, ITickable, IClayEnergyConsumer, IRecipeProvider<RecipeElement> {
    protected enum MachineSlots {
        MATERIAL,
        PRODUCT,
        ENERGY
    }

    protected EnumMachineKind kind = EnumMachineKind.EMPTY;
    private ClayiumRecipe recipeCards = ClayiumRecipes.EMPTY;
    @Override
    public ClayiumRecipe getRecipeCard() {
        return this.recipeCards;
    }

    protected RecipeElement doingRecipe = RecipeElement.flat();

    protected long craftTime;
    protected long timeToCraft;
    protected long debtEnergy;
    private boolean isDoingWork;
    @Override
    public boolean isDoingWork() {
        return this.isDoingWork;
    }
    @Override
    public void setDoingWork(boolean doingWork) {
        this.isDoingWork = doingWork;
    }

    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public float initCraftTime = 1.0F;
    public float initConsumingEnergy = 1.0F;
    private long containEnergy;
    private int clayEnergyStorageSize = 1;

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(MachineSlots.values().length, ItemStack.EMPTY);

        this.setImportRoutes(-1, 0, -1, -2, -1, -1);
        this.setExportRoutes(0, -1, -1, -1, -1, -1);
        this.listSlotsImport.add(new int[] { MachineSlots.MATERIAL.ordinal() });
        this.listSlotsExport.add(new int[] { MachineSlots.PRODUCT.ordinal() });

        this.maxAutoExtract = new int[] { -1, 1 };
        this.maxAutoInsert = new int[] { -1 };

        this.autoInsert = true;
        this.autoExtract = true;

        this.slotsDrop = new int[] { MachineSlots.MATERIAL.ordinal(), MachineSlots.PRODUCT.ordinal(), MachineSlots.ENERGY.ordinal() };
    }

    @Override
    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
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
    public void update() {
        super.update();

        if (!this.getWorld().isRemote)
            IRecipeProvider.update(this);
    }

    private void sendUpdate() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        ClayiumMachine.updateBlockState(world, pos);

        markDirty();
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.craftTime = tagCompound.getLong("CraftTime");
        this.timeToCraft = tagCompound.getLong("TimeToCraft");
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");

        this.setContainEnergy(tagCompound.getLong("ClayEnergy"));

        setKind(EnumMachineKind.fromName(tagCompound.getString("MachineId")));
        this.doingRecipe = this.getRecipe(tagCompound.getInteger("RecipeHash"));
        this.isDoingWork = !this.doingRecipe.isFlat();
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setLong("CraftTime", this.craftTime);
        tagCompound.setLong("TimeToCraft", this.timeToCraft);
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);

        tagCompound.setLong("ClayEnergy", this.containEnergy);

        tagCompound.setInteger("RecipeHash", this.doingRecipe.hashCode());

        tagCompound.setString("MachineId", this.kind.getRegisterName());

        return tagCompound;
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        if (!this.isDoingWork) return ButtonProperty.PERMIT;
        return canCraft(getStackInSlot(MachineSlots.MATERIAL)) ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
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

    @Override
    public long getContainEnergy() {
        return this.containEnergy;
    }

    @Override
    public void setContainEnergy(long energy) {
        this.containEnergy = energy;
    }

    @Override
    public int getEnergySlot() {
//        if (UtilTier.canManufactureCraft(this.tier)) return -1;
        return MachineSlots.ENERGY.ordinal();
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

    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        this.craftTime++;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStack(this.doingRecipe.getResults().get(0), this.getContainerItemStacks(),
                MachineSlots.PRODUCT.ordinal(), this.getInventoryStackLimit());

        this.craftTime = 0L;
        this.timeToCraft = 0L;
        this.debtEnergy = 0L;
        this.setDoingWork(false);
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStack(recipe.getResults().get(0), this.getContainerItemStacks(), MachineSlots.PRODUCT.ordinal(), this.getInventoryStackLimit()) > 0;
    }

    public boolean canProceedCraft() {
        return false;
    }

    public boolean setNewRecipe() {
        this.doingRecipe = getRecipe(getStackInSlot(MachineSlots.MATERIAL));
        if (this.doingRecipe.isFlat()) return false;

        this.craftTime = 0L;

        if (!canCraft(this.doingRecipe) || !IClayEnergyConsumer.compensateClayEnergy(this, this.doingRecipe.getEnergy(), false)) {
            this.timeToCraft = 0L;
            this.debtEnergy = 0L;
            return false;
        }

        this.debtEnergy = this.doingRecipe.getEnergy();
        this.timeToCraft = this.doingRecipe.getTime();
        this.getStackInSlot(MachineSlots.MATERIAL).shrink(this.doingRecipe.getStackSizes(getStackInSlot(MachineSlots.MATERIAL))[0]);

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
                return (int) this.containEnergy;
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
                this.containEnergy = value;
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
