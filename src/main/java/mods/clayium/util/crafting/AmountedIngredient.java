package mods.clayium.util.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class AmountedIngredient extends Ingredient {
    private Ingredient internal;
    private int amount;

    public AmountedIngredient(Ingredient internal, int amount) {
        super(0);
        this.internal = internal;
        this.amount = amount;
    }

    @Override
    public boolean apply(@Nullable ItemStack p_apply_1_) {
        if (p_apply_1_ == null || p_apply_1_.isEmpty()) return false;

        return this.internal.apply(p_apply_1_) && p_apply_1_.getCount() >= this.amount;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return this.internal.getMatchingStacks();
    }

    @Override
    protected void invalidate() {
        this.internal = null;
        this.amount = 0;
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
