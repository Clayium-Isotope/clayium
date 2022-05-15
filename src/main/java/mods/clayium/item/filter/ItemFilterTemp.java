package mods.clayium.item.filter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.item.ItemTiered;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilItemStack;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class ItemFilterTemp
        extends ItemTiered
        implements IItemFilter {
    public static final int maxFilterSize = 100;
    public static volatile IIcon iiconCopy = null;


    ItemFilterTemp() {
        setMaxStackSize(1);
    }


    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if (isCopy(p_77659_1_) && p_77659_3_.isSneaking()) {
            if (!p_77659_2_.isRemote) {
                p_77659_3_.addChatMessage((IChatComponent) new ChatComponentText("Cleared the copy flag."));
            }
            return clearCopyFlag(p_77659_1_);
        }
        openGui(p_77659_1_, p_77659_2_, p_77659_3_);

        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemstack, player, list, flag);
        addTooltip(itemstack.stackTagCompound, list, 0);
    }


    public void addFilterInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
        addTooltip(itemstack.stackTagCompound, list, 0);
    }


    public String getItemStackDisplayName(ItemStack itemstack) {
        String name = getUnlocalizedNameInefficiently(itemstack);
        if (isCopy(itemstack))
            name = name + ".copy";
        return StatCollector.translateToLocal("" + name + ".name").trim();
    }


    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack) {
        return false;
    }


    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z) {
        return filterMatch(filterTag, UtilBuilder.getItemBlock(world, x, y, z));
    }


    public void addTooltip(NBTTagCompound filterTag, List list, int indent) {}


    public int getFilterSize(NBTTagCompound filterTag) {
        return 1;
    }

    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {}

    public void checkFilterSize(ItemStack filter, EntityPlayer player, World world) {
        if (filter != null && filter.getItem() instanceof IItemWithFilterSize) {
            IItemWithFilterSize filteritem = (IItemWithFilterSize) filter.getItem();
            if (filteritem.getFilterSize(filter.getTagCompound()) >= 100) {
                filter.setTagCompound(new NBTTagCompound());
                if (!world.isRemote) {
                    player.addChatMessage((IChatComponent) new ChatComponentText("The filter has broken! The filter size is too large!"));
                }
            }
        }
    }


    public boolean isCopy(ItemStack filter) {
        return (filter.getItemDamage() == 1);
    }

    public ItemStack setCopyFlag(ItemStack filter) {
        filter.setItemDamage(1);
        return filter;
    }

    public ItemStack clearCopyFlag(ItemStack filter) {
        filter.setItemDamage(0);
        return filter;
    }

    public static boolean match(ItemStack filter, ItemStack itemstack) {
        if (isFilter(filter)) {
            return ((IItemFilter) filter.getItem()).filterMatch(filter.getTagCompound(), itemstack);
        }
        return false;
    }

    public static boolean match(ItemStack filter, World world, int x, int y, int z) {
        if (isFilter(filter)) {
            return ((IItemFilter) filter.getItem()).filterMatch(filter.getTagCompound(), world, x, y, z);
        }
        return false;
    }


    public static boolean matchBetweenItemstacks(ItemStack filter, ItemStack itemstack, boolean fuzzy) {
        if (filter == null) return (itemstack == null);
        if (!fuzzy) return (itemstack != null && UtilItemStack.areTypeEqual(filter, itemstack));
        if (itemstack == null) return false;
        if (UtilItemStack.areItemDamageEqualOrDamageable(filter, itemstack)) return true;
        return UtilItemStack.haveSameOD(filter, itemstack);
    }

    public static boolean isFilter(ItemStack filter) {
        return (filter != null && filter.getItem() instanceof IItemFilter);
    }


    @SideOnly(Side.CLIENT)
    public synchronized void registerIcons(IIconRegister iicon) {
        super.registerIcons(iicon);
        if (iiconCopy == null)
            iiconCopy = iicon.registerIcon("clayium:filtercopy");
    }

    public int getRenderPasses(int meta) {
        return (meta == 1) ? 2 : 1;
    }


    public boolean requiresMultipleRenderPasses() {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
        return (pass == 0) ? getIconFromDamage(meta) : iiconCopy;
    }
}
