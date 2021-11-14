package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockDamaged
        extends Block implements ITieredBlock, ISpecialToolTip, ISpecialUnlocalizedName {
    private IIcon[] iicon = new IIcon[16];


    protected String[] blockNames = new String[16];
    private boolean[] displayModes = new boolean[16];
    protected String[] unlocalizedNames = new String[16];
    protected String[] unlocalizedSubNames = new String[16];
    protected String[] iconNames = new String[16];
    protected String[] iconSubNames = new String[16];
    protected int[] damagesDropped = new int[16];
    protected int[] tiers = new int[16];
    protected List[] tooltips = new List[16];
    protected String lastRegisteredName = null;

    protected HashMap<String, Object>[] hashs = (HashMap<String, Object>[]) new HashMap[16];

    public BlockDamaged(Material material, int _maxMeta) {
        super(material);
        if (_maxMeta > 16) _maxMeta = 16;
        for (int i = 0; i < _maxMeta; i++) {
            addBlockList(i);
        }
    }


    public BlockDamaged(Material material) {
        super(material);
    }

    protected BlockDamaged() {
        this(Material.rock);
    }

    public BlockDamaged addBlockList(int meta) {
        return addBlockList(Integer.toString(meta), meta);
    }

    public BlockDamaged addBlockList(String blockname, int meta) {
        if (meta < 0 || meta >= 16) {
            ClayiumCore.logger.error("Can't register block " + blockname + " to " + getUnlocalizedName() + ". The metadata is invalid.");
            return this;
        }
        if (isRegistered(meta)) {
            ClayiumCore.logger.error("Can't register block " + blockname + " to " + getUnlocalizedName() + ". The metadata has been used.");
            return this;
        }
        if (blockname == null) {
            ClayiumCore.logger.error("Can't register block " + blockname + " to " + getUnlocalizedName() + ". The blockname is invalid.");
            return this;
        }
        if (getMeta(blockname) != -1) {
            ClayiumCore.logger.error("Can't register block " + blockname + " to " + getUnlocalizedName() + ". The blockname has been used.");
            return this;
        }
        this.blockNames[meta] = blockname;
        this.iconNames[meta] = null;
        this.iconSubNames[meta] = "-" + blockname;
        this.tiers[meta] = -1;
        this.displayModes[meta] = true;
        this.damagesDropped[meta] = meta;
        this.unlocalizedNames[meta] = null;
        this.unlocalizedSubNames[meta] = blockname;
        this.tooltips[meta] = new ArrayList();
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setIconName(String iconname) {
        return setIconName(this.lastRegisteredName, iconname);
    }

    public BlockDamaged setIconName(String blockname, String iconname) {
        this.iconNames[getMeta(blockname)] = iconname;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setIconSurfix(String iconsubname) {
        return setIconSurfix(this.lastRegisteredName, iconsubname);
    }

    public BlockDamaged setIconSurfix(String blockname, String iconsubname) {
        this.iconNames[getMeta(blockname)] = iconsubname;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setTier(int tier) {
        return setTier(this.lastRegisteredName, tier);
    }

    public BlockDamaged setTier(String blockname, int tier) {
        this.tiers[getMeta(blockname)] = tier;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setDisplayMode(boolean displayMode) {
        return setDisplayMode(this.lastRegisteredName, displayMode);
    }

    public BlockDamaged setDisplayMode(String blockname, boolean displayMode) {
        this.displayModes[getMeta(blockname)] = displayMode;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setDamageDropped(int damageDropped) {
        return setDamageDropped(this.lastRegisteredName, damageDropped);
    }

    public BlockDamaged setDamageDropped(String blockname, int damageDropped) {
        this.damagesDropped[getMeta(blockname)] = damageDropped;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setSubBlockName(String unlocalizedName) {
        return setSubBlockName(this.lastRegisteredName, unlocalizedName);
    }

    public BlockDamaged setSubBlockName(String blockname, String unlocalizedName) {
        this.unlocalizedNames[getMeta(blockname)] = unlocalizedName;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setSubBlockSurfix(String unlocalizedSubName) {
        return setSubBlockSurfix(this.lastRegisteredName, unlocalizedSubName);
    }

    public BlockDamaged setSubBlockSurfix(String blockname, String unlocalizedSubName) {
        this.unlocalizedSubNames[getMeta(blockname)] = unlocalizedSubName;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged setToolTip(List tooltip) {
        return setToolTip(this.lastRegisteredName, tooltip);
    }

    public BlockDamaged setToolTip(String blockname, List tooltip) {
        this.tooltips[getMeta(blockname)] = tooltip;
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged addToolTip(Object tooltip) {
        return addToolTip(this.lastRegisteredName, tooltip);
    }

    public BlockDamaged addToolTip(String blockname, Object tooltip) {
        if (this.tooltips[getMeta(blockname)] == null) {
            this.tooltips[getMeta(blockname)] = new ArrayList();
        }
        this.tooltips[getMeta(blockname)].add(tooltip);
        this.lastRegisteredName = blockname;
        return this;
    }

    public BlockDamaged putInfo(String key, Object value) {
        return putInfo(this.lastRegisteredName, key, value);
    }

    public BlockDamaged putInfo(String blockname, String key, Object value) {
        if (getMeta(blockname) < 0 || getMeta(blockname) >= 16) return this;
        if (this.hashs[getMeta(blockname)] == null) this.hashs[getMeta(blockname)] = new HashMap<String, Object>();
        this.hashs[getMeta(blockname)].put(key, value);
        return this;
    }

    public Object getInfo(String blockname, String key) {
        if (getMeta(blockname) < 0 || getMeta(blockname) >= 16) return null;
        if (this.hashs[getMeta(blockname)] == null) return null;
        return this.hashs[getMeta(blockname)].get(key);
    }

    protected int getMeta(String blockname) {
        if (blockname == null) return -1;
        for (int i = 0; i < 16; i++) {
            if (blockname.equals(this.blockNames[i]))
                return i;
        }
        return -1;
    }

    public String getBlockName(int meta) {
        return this.blockNames[meta];
    }

    public String getBlockName(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z) != this) return null;
        return this.blockNames[world.getBlockMetadata(x, y, z)];
    }

    public List getTooltip(ItemStack itemStack) {
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i) &&
                    damageDropped(i) == itemStack.getItemDamage()) {
                return getTooltip(itemStack.getItemDamage());
            }
        }
        return new ArrayList();
    }

    public List getTooltip(int meta) {
        List list = UtilLocale.localizeTooltip(getUnlocalizedName(meta) + ".tooltip");
        if (this.tooltips[meta] != null)
            list.addAll(this.tooltips[meta]);
        return list;
    }

    public boolean isRegistered(int meta) {
        return (this.blockNames[meta] != null);
    }

    public List<Integer> getAvailableMetadata() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i))
                list.add(Integer.valueOf(i));
        }
        return list;
    }

    public List<String> getAvailableBlockName() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i))
                list.add(this.blockNames[i]);
        }
        return list;
    }

    public ItemStack get(String blockname) {
        return get(blockname, 1);
    }

    public ItemStack get(String blockname, int stacksize) {
        return (getMeta(blockname) == -1) ? null : new ItemStack(this, stacksize, getMeta(blockname));
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i)) {
                this.iicon[i] = register.registerIcon(getIconName(i));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected String getIconName(int meta) {
        if (isRegistered(meta)) {
            if (this.iconNames[meta] != null)
                return this.iconNames[meta];
            return getTextureName() + this.iconSubNames[meta];
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void setIcon(String blockName, IIcon icon) {
        setIcon(getMeta(blockName), icon);
    }

    @SideOnly(Side.CLIENT)
    public void setIcon(int meta, IIcon icon) {
        this.iicon[meta] = icon;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (this.iicon[meta] != null)
            return this.iicon[meta];
        for (IIcon icon : this.iicon) {
            if (icon != null)
                return icon;
        }
        return Blocks.stone.getIcon(side, meta);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i) && this.displayModes[i]) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }

    public int damageDropped(int meta) {
        return this.damagesDropped[meta];
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i) &&
                    damageDropped(i) == itemStack.getItemDamage()) {
                return getUnlocalizedName(itemStack.getItemDamage());
            }
        }
        return getUnlocalizedName();
    }


    protected String getUnlocalizedName(int meta) {
        if (isRegistered(meta)) {
            if (this.unlocalizedNames[meta] != null)
                return "tile." + this.unlocalizedNames[meta];
            return getUnlocalizedName() + "." + this.unlocalizedSubNames[meta];
        }
        return null;
    }

    public int getTier(ItemStack itemstack) {
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i) &&
                    damageDropped(i) == itemstack.getItemDamage()) {
                return this.tiers[i];
            }
        }
        return -1;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return this.tiers[world.getBlockMetadata(x, y, z)];
    }
}
