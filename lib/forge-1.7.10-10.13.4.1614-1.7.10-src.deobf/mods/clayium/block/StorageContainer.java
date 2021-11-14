package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.tile.TileGeneric;
import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.gui.TextureExtra;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class StorageContainer extends ClayContainer implements ITieredBlock {
    @SideOnly(Side.CLIENT)
    public IIcon[] FrontIcons;

    public StorageContainer(Material material, String iconstr) {
        super(material, (Class) TileStorageContainer.class, iconstr, 1);

        this.guiId = 15;

        setStepSound(Block.soundTypeMetal);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }


    public IIcon[] TopIcons;


    public IIcon[] SideIcons;


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        this.FrontIcons = new IIcon[2];
        this.TopIcons = new IIcon[2];
        this.SideIcons = new IIcon[2];

        this.SideIcons[0] = (new TextureExtra("clayium:storagecontainerside_", new String[] {this.machineIconStr, "clayium:storagecontainerside"
        })).register(par1IconRegister);
        this.BackIcon = this.LeftIcon = this.RightIcon = (new TextureExtra("clayium:storagecontainerside_", new String[] {this.machineIconStr, "clayium:storagecontainerside"})).register(par1IconRegister);
        this.FrontIcon = this.FrontIcons[0] = (new TextureExtra("clayium:storagecontainer_", new String[] {this.machineIconStr, "clayium:storagecontainer"})).register(par1IconRegister);
        this.TopIcons[0] = (new TextureExtra("clayium:storagecontainertop_", new String[] {this.machineIconStr, "clayium:storagecontainertop"})).register(par1IconRegister);
        this.UpIcon = this.DownIcon = (new TextureExtra("clayium:storagecontainertop_", new String[] {this.machineIconStr, "clayium:storagecontainertop"})).register(par1IconRegister);


        this.SideIcons[1] = (new TextureExtra("clayium:storagecontainerside_1", new String[] {this.machineIconStr, "clayium:storagecontainerside", "clayium:storagecontainerex"})).register(par1IconRegister);
        this.FrontIcons[1] = (new TextureExtra("clayium:storagecontainer_1", new String[] {this.machineIconStr, "clayium:storagecontainer", "clayium:storagecontainerex"})).register(par1IconRegister);
        this.TopIcons[1] = (new TextureExtra("clayium:storagecontainertop_1", new String[] {this.machineIconStr, "clayium:storagecontainertop", "clayium:storagecontainerex"})).register(par1IconRegister);
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import"});
        registerExtractIcons(par1IconRegister, new String[] {"export"});
    }


    public int getTier(ItemStack itemstack) {
        return 6;
    }

    public int getTier(IBlockAccess world, int x, int y, int z) {
        return 6;
    }


    public List getTooltip(ItemStack itemStack) {
        List<String> res = new ArrayList();
        if (itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            if (tag.hasKey("TileEntityNBTTag")) {
                ItemStack item = null;

                NBTTagCompound tetag = (NBTTagCompound) tag.getTag("TileEntityNBTTag");
                NBTTagList tagList = tetag.getTagList("Items", 10);
                for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
                    byte byte0 = tagCompound1.getByte("Slot");
                    if (byte0 == 0) {
                        item = ItemStack.loadItemStackFromNBT(tagCompound1);
                    }
                }
                int size = tetag.getInteger("ItemStackSize");
                int max = tetag.getInteger("MaxStorageSize");

                if (item == null) {
                    size = 0;
                    for (int j = 0; j < tagList.tagCount(); j++) {
                        NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(j);
                        byte byte0 = tagCompound1.getByte("Slot");
                        if (byte0 == 1) {
                            item = ItemStack.loadItemStackFromNBT(tagCompound1);
                        }
                    }
                }

                if (item != null) {
                    res.add(item.getDisplayName());
                    res.add("" + size + "/" + max);
                }
            }
        }


        return res;
    }

    public static ItemStack expandStorage(ItemStack itemStack, int storageSize) {
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock && ((ItemBlock) itemStack.getItem()).field_150939_a instanceof StorageContainer) {
            NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
            NBTTagCompound tetag = tag.hasKey("TileEntityNBTTag") ? (NBTTagCompound) tag.getTag("TileEntityNBTTag") : new NBTTagCompound();
            itemStack.setItemDamage(storageSize2damage(storageSize) * 16);
            tetag.setInteger("MaxStorageSize", storageSize);
            tag.setTag("TileEntityNBTTag", (NBTBase) tetag);
            itemStack.setTagCompound(tag);
        }
        return itemStack;
    }

    public static int getStorageSize(ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock) || !(((ItemBlock) itemStack.getItem()).field_150939_a instanceof StorageContainer))
            return 0;
        NBTTagCompound tetag = TileGeneric.getTileEntityTag(itemStack);
        return (tetag != null && tetag.hasKey("MaxStorageSize")) ? tetag.getInteger("MaxStorageSize") : 65536;
    }

    public static int storageSize2damage(int storageSize) {
        int i = 0;
        for (; (storageSize >>= 1) > 0; i++) ;
        return i;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (te instanceof TileStorageContainer) {
            return getIcon(side, getFront(world, x, y, z) + storageSize2damage(((TileStorageContainer) te).maxStorageSize) * 16);
        }
        return super.getIcon(world, x, y, z, side);
    }


    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (te instanceof TileStorageContainer) {
            return getOverlayIcon(side, getFront(world, x, y, z) + storageSize2damage(((TileStorageContainer) te).maxStorageSize) * 16);
        }
        return super.getOverlayIcon(world, x, y, z, side);
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int i = (meta / 16 < 30) ? 0 : 1;
        this.BackIcon = this.LeftIcon = this.RightIcon = this.SideIcons[i];
        this.FrontIcon = this.FrontIcons[i];
        this.UpIcon = this.DownIcon = this.TopIcons[i];
        return super.getIcon(side, meta % 16);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        for (int i : new int[] {65536, Integer.MAX_VALUE}) {
            ItemStack itemStack = new ItemStack(item, 1);
            itemStack = expandStorage(itemStack, i);
            list.add(itemStack);
        }
    }


    public int getDamageValue(World world, int x, int y, int z) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof TileStorageContainer)
            return storageSize2damage(((TileStorageContainer) te).maxStorageSize);
        return super.getDamageValue(world, x, y, z);
    }


    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        Item item = getItem(world, x, y, z);
        ItemStack ret = new ItemStack(item, 1);
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof TileStorageContainer)
            return expandStorage(ret, ((TileStorageContainer) te).maxStorageSize);
        return super.getPickBlock(target, world, x, y, z, player);
    }
}
