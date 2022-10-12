package mods.clayium.item.filter;

import mods.clayium.item.common.IModifyCC;
import mods.clayium.item.common.ItemTiered;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class FilterTemp extends ItemTiered implements IFilter, IModifyCC {
    public FilterTemp(String modelPath, int tier) {
        super(modelPath, tier);

        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityClayContainer) {
            TileEntityClayContainer tecc = (TileEntityClayContainer) worldIn.getTileEntity(pos);

            ItemStack filter = player.getHeldItem(hand);
            List<String> list = new ArrayList<>();

            if (!((IFilter) filter.getItem()).isCopy(filter)) {
                tecc.filters.put(facing, filter.copy());
                player.sendMessage(new TextComponentString("Applied " + filter.getDisplayName()));

                ((IFilter) filter.getItem()).addFilterInformation(filter, player, list, true);

                for (String s : list) {
                    player.sendMessage(new TextComponentString(" " + s));
                }

                tecc.updateEntity();
            } else {
                if (!tecc.filters.get(facing).isEmpty()) {
                    IFilter itemfilterinv = (IFilter) tecc.filters.get(facing).getItem();
                    filter = tecc.filters.get(facing).copy();

                    // where's the method EnumHand -> EntityEquipmentSlot ?
                    switch (hand) {
                        case MAIN_HAND:
                            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemfilterinv.setCopyFlag(filter));
                        case OFF_HAND:
                            player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemfilterinv.setCopyFlag(filter));
                    }
                    player.sendMessage(new TextComponentString("Copied " + filter.getDisplayName()));

                    itemfilterinv.addFilterInformation(filter, player, list, true);
                    for (String s : list) {
                        player.sendMessage(new TextComponentString(" " + s));
                    }
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (this.isCopy(playerIn.getHeldItem(handIn)) && playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                playerIn.sendMessage(new TextComponentString("Cleared the copy flag."));
            }

            return ActionResult.newResult(EnumActionResult.SUCCESS, this.clearCopyFlag(playerIn.getHeldItem(handIn)));
        } else {
            openGui(playerIn.getHeldItem(handIn), worldIn, playerIn);
            return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
    }

    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.addTooltip(stack.getTagCompound(), tooltip, 0);
    }

    @Override
    public void addTooltip(NBTTagCompound filterTag, List<String> list, int indent) {}

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        String name = this.getUnlocalizedNameInefficiently(itemstack);
        if (this.isCopy(itemstack)) {
            name = name + ".copy";
        }

        return I18n.format(name + ".name").trim();
    }

    public boolean isCopy(ItemStack filter) {
        return filter.getItemDamage() == 1;
    }

    public ItemStack setCopyFlag(ItemStack filter) {
        filter.setItemDamage(1);
        return filter;
    }

    public ItemStack clearCopyFlag(ItemStack filter) {
        filter.setItemDamage(0);
        return filter;
    }
}
