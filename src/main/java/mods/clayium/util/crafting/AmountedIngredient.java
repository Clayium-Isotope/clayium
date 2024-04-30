package mods.clayium.util.crafting;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import com.google.gson.JsonElement;

import mods.clayium.util.JsonHelper;

public class AmountedIngredient extends Ingredient {

    protected final Ingredient internal;
    protected final int amount;

    protected ItemStack[] matchingStacks = null;

    public AmountedIngredient(Ingredient internal, int amount) {
        super(0);
        this.internal = internal;
        this.amount = amount;
    }

    public AmountedIngredient(JsonElement json, JsonContext context) {
        this(CraftingHelper.getIngredient(json, context), JsonHelper.readNumeric(json, "amount", 1));
    }

    @Override
    public boolean apply(@Nullable ItemStack p_apply_1_) {
        if (p_apply_1_ == null || p_apply_1_.isEmpty()) return false;

        return this.internal.apply(p_apply_1_) && p_apply_1_.getCount() >= this.amount;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        if (this.matchingStacks == null) {
            this.matchingStacks = new ItemStack[this.internal.getMatchingStacks().length];
            for (int i = 0; i < this.matchingStacks.length; i++) {
                this.matchingStacks[i] = this.internal.getMatchingStacks()[i].copy();
                this.matchingStacks[i].setCount(this.amount);
            }
        }

        return this.matchingStacks;
    }

    @Override
    protected void invalidate() {
        // this.internal = EMPTY;
        // this.amount = 0;
        this.matchingStacks = null;
    }

    @Override
    public boolean isSimple() {
        return this.internal.isSimple();
    }

    public Ingredient getIngredient() {
        return internal;
    }

    public int getAmount() {
        return amount;
    }
}
