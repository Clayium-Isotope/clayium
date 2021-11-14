package mods.clayium.block.itemblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockDamaged
        extends ItemBlockTiered {
    public ItemBlockDamaged(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public String getDefaultUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName() + "." + itemStack.getItemDamage();
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.field_150939_a.getIcon(2, p_77617_1_);
    }


    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
}
