package mods.clayium.block;

import cofh.api.energy.IEnergyConnection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mods.clayium.block.itemblock.IOverridableBlock;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.PlayerKey;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilKeyInput;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public abstract class ClayContainer
        extends BlockContainer
        implements IOverridableBlock, ISpecialToolTip {
    private final Random random = new Random();

    public Class<? extends TileEntity> tileEntityClass;
    public int guiId = -1;

    public int metaMode;
    protected boolean isTransparent = false;
    protected boolean isOverlayTransparent = false;
    protected boolean confirmFirstpass = true;
    private static UtilDirection tracingDirection = null;

    public static float pipeWidth = 0.1875F;

    @SideOnly(Side.CLIENT)
    public IIcon FrontIcon;

    @SideOnly(Side.CLIENT)
    public IIcon RightIcon;

    @SideOnly(Side.CLIENT)
    public IIcon LeftIcon;

    @SideOnly(Side.CLIENT)
    public IIcon BackIcon;

    @SideOnly(Side.CLIENT)
    public IIcon UpIcon;

    @SideOnly(Side.CLIENT)
    public IIcon DownIcon;
    @SideOnly(Side.CLIENT)
    public IIcon FrontOverlayIcon;
    @SideOnly(Side.CLIENT)
    public IIcon RightOverlayIcon;
    @SideOnly(Side.CLIENT)
    public IIcon LeftOverlayIcon;
    @SideOnly(Side.CLIENT)
    public IIcon BackOverlayIcon;
    @SideOnly(Side.CLIENT)
    public IIcon UpOverlayIcon;
    @SideOnly(Side.CLIENT)
    public IIcon DownOverlayIcon;
    @SideOnly(Side.CLIENT)
    public IIcon[] InsertIcons;
    @SideOnly(Side.CLIENT)
    public IIcon[] ExtractIcons;
    @SideOnly(Side.CLIENT)
    public IIcon FilterIcon;
    @SideOnly(Side.CLIENT)
    public IIcon[] InsertPipeIcons;
    @SideOnly(Side.CLIENT)
    public IIcon[] ExtractPipeIcons;
    public String machineIconStr;
    public static int renderPass = 0;
    protected ForgeDirection[][] validRotations;

    public ClayContainer(Material material, Class<? extends TileEntity> tileEntityClass, int metaMode) {
        this(material, tileEntityClass, (String) null, metaMode);
    }

    public ClayContainer(Material material, Class<? extends TileEntity> tileEntityClass, int guiId, int metaMode) {
        this(material, tileEntityClass, (String) null, metaMode);
        this.guiId = guiId;
    }

    public ClayContainer(Material material, Class<? extends TileEntity> tileEntityClass, String machineIconStr, int guiId, int metaMode) {
        this(material, tileEntityClass, machineIconStr, metaMode);
        this.guiId = guiId;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof TileClayContainer) {
            TileClayContainer tileentity = (TileClayContainer) te;
            for (int i : tileentity.getSlotsDrop()) {
                ItemStack itemstack = tileentity.getStackInSlot(i);
                if (itemstack != null) {
                    float f = this.random.nextFloat() * 0.6F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.6F + 0.1F;
                    float f2 = this.random.nextFloat() * 0.6F + 0.1F;
                    while (itemstack.stackSize > 0) {
                        int j = this.random.nextInt(21) + 10;
                        if (j > itemstack.stackSize)
                            j = itemstack.stackSize;
                        itemstack.stackSize -= j;
                        EntityItem entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        float f3 = 0.025F;
                        entityitem.motionX = ((float) this.random.nextGaussian() * f3);
                        entityitem.motionY = ((float) this.random.nextGaussian() * f3 + 0.1F);
                        entityitem.motionZ = ((float) this.random.nextGaussian() * f3);
                        world.spawnEntityInWorld((Entity) entityitem);
                    }
                }
            }
        }
        world.func_147453_f(x, y, z, block);
        if (te instanceof TileGeneric) {
            if (((TileGeneric) te).shouldRefresh() || ((TileGeneric) te).getRemoveFlag()) {
                world.removeTileEntity(x, y, z);
            } else { ((TileGeneric) te).setRemoveFlag(); }
        } else { super.breakBlock(world, x, y, z, block, meta); }
    }

    public ClayContainer(Material material, Class<? extends TileEntity> tileEntityClass, String machineIconStr, int metaMode) {
        super(material);


        this.validRotations = new ForgeDirection[][] {{ }, {ForgeDirection.UP, ForgeDirection.DOWN}, ForgeDirection.VALID_DIRECTIONS};
        this.tileEntityClass = tileEntityClass;
        this.metaMode = metaMode;
        this.machineIconStr = machineIconStr;
    }

    public TileEntity createNewTileEntity(World world, int par2) {
        if (this.tileEntityClass == null)
            return null;
        try { return this.tileEntityClass.newInstance(); } catch (Exception exception) {
            ClayiumCore.logger.catching(exception);
            return null;
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;
        if (!(UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z) instanceof TileGeneric))
            return false;
        if (UtilKeyInput.getKeyLength(player, PlayerKey.KeyType.SPRINT) < 0) {
            ItemStack itemStack = player.getCurrentEquippedItem();
            TileGeneric te = (TileGeneric) world.getTileEntity(x, y, z);
            if (te.isUsable(itemStack, player, side, hitX, hitY, hitZ)) {
                te.useItem(itemStack, player, side, hitX, hitY, hitZ);
                return true;
            }
            return onBlockRightClicked(world, x, y, z, player, side, hitX, hitY, hitZ);
        }
        return false;
    }

    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z) { return (this.metaMode >= 0 && this.metaMode < this.validRotations.length) ? this.validRotations[this.metaMode] : null; }

    protected void openGui(int guiId, World world, int x, int y, int z, EntityPlayer player) { player.openGui(ClayiumCore.INSTANCE, guiId, world, x, y, z); }

    protected void openGui(World world, int x, int y, int z, EntityPlayer player) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof ClayContainer && UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z) instanceof TileGeneric)
            openGui(((ClayContainer) block).guiId, world, x, y, z, player);
    }

    protected boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        world.markBlockForUpdate(x, y, z);
        openGui(world, x, y, z, player);
        return true;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack) {
        TileEntity tile = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        int m = -1;
        if (this.metaMode == 0) { m = 0; } else if (this.metaMode == 1) {
            int direction = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
            if (direction == 0) m = 2;
            if (direction == 1) m = 5;
            if (direction == 2) m = 3;
            if (direction == 3) m = 4;
        } else if (this.metaMode == 2) { m = BlockPistonBase.determineOrientation(world, x, y, z, entity); }
        if (m != -1) {
            world.setBlockMetadataWithNotify(x, y, z, m, 2);
            if (tile != null) tile.blockMetadata = m;
        }
        if (itemstack.hasDisplayName()) ;
        if (world.getTileEntity(x, y, z) instanceof TileGeneric)
            ((TileGeneric) world.getTileEntity(x, y, z)).onBlockPlacedBy(world, x, y, z, entity, itemstack);
    }

    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
        if (worldObj.isRemote) {
            return false;
        }

        ForgeDirection[] axes = getValidRotations(worldObj, x, y, z);
        if (axes == null || axes.length == 0)
            return false;
        boolean flag = false;
        ForgeDirection axis1 = axes[0];
        for (ForgeDirection axis2 : axes) {
            if (axis2 == axis)
                axis1 = axis;
        }
        int meta = worldObj.getBlockMetadata(x, y, z);
        if (meta >= 6)
            meta -= 6;
        ForgeDirection orientation = ForgeDirection.getOrientation(meta);
        ForgeDirection rotated = orientation.getRotation(axis1);
        meta = rotated.ordinal();
        if (meta == -1) {
            return false;
        }
        worldObj.setBlockMetadataWithNotify(x, y, z, meta, 3);

        TileEntity tile = UtilBuilder.safeGetTileEntity((IBlockAccess) worldObj, x, y, z);
        if (tile instanceof TileGeneric)
            ((TileGeneric) tile).setRenderSyncFlag();
        return true;
    }


    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> res;
        if (UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z) instanceof TileGeneric && (
                (TileGeneric) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z)).hasSpecialDrops()) {
            res = ((TileGeneric) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z)).getDrops(world, x, y, z, (Block) this, metadata, fortune);
        } else {
            res = super.getDrops(world, x, y, z, metadata, fortune);
        }
        if (UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z) instanceof TileGeneric && (
                (TileGeneric) UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z)).getRemoveFlag()) {
            world.removeTileEntity(x, y, z);
        }
        return res;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        UtilDirection frontDirection = UtilDirection.getOrientation(getFront(meta % 16));
        UtilDirection sideDirection = UtilDirection.getOrientation(side);

        switch (frontDirection.getSideOfDirection(sideDirection)) {
            case FRONTSIDE:
                return this.FrontIcon;
            case BACKSIDE:
                return this.BackIcon;
            case RIGHTSIDE:
                return this.RightIcon;
            case LEFTSIDE:
                return this.LeftIcon;
            case UPSIDE:
                return this.UpIcon;
            case DOWNSIDE:
                return this.DownIcon;
        }
        return this.blockIcon;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon(int side, int meta) {
        UtilDirection frontDirection = UtilDirection.getOrientation(getFront(meta % 16));
        UtilDirection sideDirection = UtilDirection.getOrientation(side);

        switch (frontDirection.getSideOfDirection(sideDirection)) {
            case FRONTSIDE:
                return this.FrontOverlayIcon;
            case BACKSIDE:
                return this.BackOverlayIcon;
            case RIGHTSIDE:
                return this.RightOverlayIcon;
            case LEFTSIDE:
                return this.LeftOverlayIcon;
            case UPSIDE:
                return this.UpOverlayIcon;
            case DOWNSIDE:
                return this.DownOverlayIcon;
        }
        return null;
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(side, getFront(world, x, y, z));
    }

    public int getFront(TileGeneric tile) {
        return getFront(tile.getBlockMetadata());
    }

    public int getFront(IBlockAccess world, int x, int y, int z) {
        return getFront(world.getBlockMetadata(x, y, z));
    }


    public int getFront(int meta) {
        return (this.metaMode == 0) ? UtilDirection.NORTH.ordinal() : ((meta < 6) ? meta : (meta - 6));
    }


    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon(IBlockAccess world, int x, int y, int z, int side) {
        return getOverlayIcon(side, getFront(world, x, y, z));
    }


    public int damageDropped(int meta) {
        return 0;
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        list.add(new ItemStack(item, 1, (this.metaMode == 0) ? 0 : 0));
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.FilterIcon = par1IconRegister.registerIcon("clayium:filter");
        if (this.machineIconStr != null && !this.machineIconStr.equals("")) {
            setSameIcons(par1IconRegister.registerIcon(this.machineIconStr));
        }

        registerIOIcons(par1IconRegister);
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {}


    @SideOnly(Side.CLIENT)
    public void registerInsertIcons(IIconRegister par1IconRegister, String... iconstrs) {
        if (iconstrs != null) {
            this.InsertIcons = new IIcon[iconstrs.length];
            this.InsertPipeIcons = new IIcon[iconstrs.length];
            for (int i = 0; i < iconstrs.length; i++) {
                this.InsertIcons[i] = par1IconRegister.registerIcon("clayium:" + iconstrs[i]);
                this.InsertPipeIcons[i] = par1IconRegister.registerIcon("clayium:" + iconstrs[i] + "_p");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerExtractIcons(IIconRegister par1IconRegister, String... iconstrs) {
        if (iconstrs != null) {
            this.ExtractIcons = new IIcon[iconstrs.length];
            this.ExtractPipeIcons = new IIcon[iconstrs.length];
            for (int i = 0; i < iconstrs.length; i++) {
                this.ExtractIcons[i] = par1IconRegister.registerIcon("clayium:" + iconstrs[i]);
                this.ExtractPipeIcons[i] = par1IconRegister.registerIcon("clayium:" + iconstrs[i] + "_p");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getInsertIcons(IBlockAccess world, int x, int y, int z) {
        return this.InsertIcons;
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getExtractIcons(IBlockAccess world, int x, int y, int z) {
        return this.ExtractIcons;
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getInsertPipeIcons(IBlockAccess world, int x, int y, int z) {
        return this.InsertPipeIcons;
    }

    @SideOnly(Side.CLIENT)
    public IIcon[] getExtractPipeIcons(IBlockAccess world, int x, int y, int z) {
        return this.ExtractPipeIcons;
    }


    public int getRenderType() {
        return ClayiumCore.clayContainerRenderId;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }


    public boolean renderAsPipe(IBlockAccess world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) >= 6);
    }


    public void setSameIcons(IIcon iicon) {
        this.FrontIcon = this.RightIcon = this.LeftIcon = this.DownIcon = this.UpIcon = this.BackIcon = iicon;
    }

    public void setSameOverlayIcons(IIcon iicon) {
        this.FrontOverlayIcon = this.RightOverlayIcon = this.LeftOverlayIcon = this.DownOverlayIcon = this.UpOverlayIcon = this.BackOverlayIcon = iicon;
    }


    public boolean canChangeRenderType() {
        return true;
    }


    public boolean isOpaqueCube() {
        return !canChangeRenderType();
    }


    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return (isTransparent() || isOverlayTransparent()) ? 1 : 0;
    }


    public boolean isTransparent() {
        return this.isTransparent;
    }


    public boolean isOverlayTransparent() {
        return this.isOverlayTransparent;
    }


    public void setTransparent() {
        this.isTransparent = true;
    }


    public void setOverlayTransparent() {
        this.isOverlayTransparent = true;
    }

    public void preventFirstPass() {
        this.confirmFirstpass = false;
    }


    public boolean canRenderInPass(int pass) {
        renderPass = pass;
        return ((pass == 0 && this.confirmFirstpass) || (pass == 1 && (!this.confirmFirstpass || isTransparent() || isOverlayTransparent())));
    }

    public void setInitialBlockBounds() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }


    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        if (!renderAsPipe((IBlockAccess) world, x, y, z)) {
            super.setBlockBoundsBasedOnState((IBlockAccess) world, x, y, z);
            setInitialBlockBounds();
            return super.collisionRayTrace(world, x, y, z, start, end);
        }

        UtilDirection mindirection = null;
        MovingObjectPosition res = null;
        double o = pipeWidth;

        setBlockBounds((float) (0.5D - o), (float) (0.5D - o), (float) (0.5D - o), (float) (0.5D + o), (float) (0.5D + o), (float) (0.5D + o));
        res = super.collisionRayTrace(world, x, y, z, start, end);


        UtilDirection[] directions = {UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.EAST, UtilDirection.WEST, UtilDirection.UP, UtilDirection.DOWN};
        for (UtilDirection direction : directions) {
            if (checkPipeConnection((IBlockAccess) world, x, y, z, direction)) {
                setBlockBounds((float) (0.5D - o + ((direction.offsetX == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetX == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetY == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetY == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetZ == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetZ == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetX == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetX == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetY == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetY == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetZ == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetZ == 1) ? (0.5D - o) : 0.0D)));


                MovingObjectPosition pos = super.collisionRayTrace(world, x, y, z, start, end);
                if (pos != null && (
                        res == null || pos.hitVec.distanceTo(start) < res.hitVec.distanceTo(start))) {

                    res = pos;
                    mindirection = direction;
                }
            }
        }

        setBlockBounds((float) (0.5D - o), (float) (0.5D - o), (float) (0.5D - o), (float) (0.5D + o), (float) (0.5D + o), (float) (0.5D + o));
        tracingDirection = mindirection;
        return res;
    }


    public boolean checkPipeConnection(IBlockAccess world, int x, int y, int z, UtilDirection direction) {
        TileEntity te1 = UtilBuilder.safeGetTileEntity(world, x, y, z);
        TileEntity te2 = direction.getTileEntity(world, x, y, z);
        int i1 = getConnectionAsImport(te1, direction, te2);

        if (i1 >= 0) {
            int i2 = getConnectionAsExport(te2, direction.getOpposite(), te1);
            if (i1 + i2 >= 1) {
                return true;
            }
        }
        i1 = getConnectionAsExport(te1, direction, te2);
        if (i1 >= 0) {
            int i2 = getConnectionAsImport(te2, direction.getOpposite(), te1);
            if (i1 + i2 >= 1)
                return true;
        }
        return false;
    }

    public static int getConnectionAsImport(TileEntity tile, UtilDirection from, TileEntity tile2) {
        if (tile instanceof net.minecraft.inventory.IInventory) {
            if (tile instanceof IEnergyConnection && ((IEnergyConnection) tile).canConnectEnergy(from.toForgeDirection()) && tile2 instanceof IEnergyConnection && ((IEnergyConnection) tile2)
                    .canConnectEnergy(from.getOpposite().toForgeDirection()))
                return 2;
            if (!(tile instanceof TileClayContainer)) return 1;
            TileClayContainer container = (TileClayContainer) tile;
            int i = UtilDirection.direction2Side(container.getFrontDirection(), from.ordinal()) - 6;

            if (container instanceof mods.clayium.block.tile.TileFluidTranslator && tile2 instanceof net.minecraftforge.fluids.IFluidHandler) {
                if (!(tile2 instanceof mods.clayium.block.tile.TileFluidTranslator)) {
                    return 2;
                }
            }
            if (container.insertRoutes[i] != -1) {
                return container.autoExtract ? 1 : 0;
            }
            return (container instanceof mods.clayium.block.tile.TileClayBuffer || container instanceof mods.clayium.block.tile.TileMultitrackBuffer || container instanceof mods.clayium.block.tile.TileStorageContainer) ? 0 : -1;
        }


        return -1;
    }

    public static int getConnectionAsExport(TileEntity tile, UtilDirection to, TileEntity tile2) {
        if (tile instanceof net.minecraft.inventory.IInventory) {
            if (tile instanceof IEnergyConnection && ((IEnergyConnection) tile).canConnectEnergy(to.toForgeDirection()) && tile2 instanceof IEnergyConnection && ((IEnergyConnection) tile2)
                    .canConnectEnergy(to.getOpposite().toForgeDirection()))
                return 2;
            if (!(tile instanceof TileClayContainer)) return 1;
            TileClayContainer container = (TileClayContainer) tile;
            int i = UtilDirection.direction2Side(container.getFrontDirection(), to.ordinal()) - 6;

            if (container instanceof mods.clayium.block.tile.TileFluidTranslator && tile2 instanceof net.minecraftforge.fluids.IFluidHandler) {
                if (!(tile2 instanceof mods.clayium.block.tile.TileFluidTranslator)) {
                    return 2;
                }
            }
            if (container.extractRoutes[i] != -1) {
                return container.autoInsert ? 1 : 0;
            }
            return (container instanceof mods.clayium.block.tile.TileClayBuffer || container instanceof mods.clayium.block.tile.TileMultitrackBuffer || container instanceof mods.clayium.block.tile.TileStorageContainer) ? 0 : -1;
        }

        return -1;
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        if (!renderAsPipe((IBlockAccess) world, x, y, z)) {
            super.setBlockBoundsBasedOnState((IBlockAccess) world, x, y, z);
            setInitialBlockBounds();
        } else {
            double o = pipeWidth;
            if (tracingDirection == null) {
                setBlockBounds((float) (0.5D - o), (float) (0.5D - o), (float) (0.5D - o), (float) (0.5D + o), (float) (0.5D + o), (float) (0.5D + o));
            } else {
                UtilDirection direction = tracingDirection;
                setBlockBounds((float) (0.5D - o + ((direction.offsetX == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetX == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetY == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetY == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D - o + ((direction.offsetZ == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetZ == -1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetX == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetX == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetY == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetY == 1) ? (0.5D - o) : 0.0D)), (float) (0.5D + o - ((direction.offsetZ == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetZ == 1) ? (0.5D - o) : 0.0D)));
            }
        }


        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getNormalSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }


    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        setBlockBoundsBasedOnState((IBlockAccess) p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }


    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (!renderAsPipe(world, x, y, z)) {
            super.setBlockBoundsBasedOnState(world, x, y, z);
            setInitialBlockBounds();
        }
    }


    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity p_149743_7_) {
        if (!renderAsPipe((IBlockAccess) world, x, y, z)) {
            super.addCollisionBoxesToList(world, x, y, z, aabb, list, p_149743_7_);
        } else {
            double o = pipeWidth;
            AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(x + 0.5D - o, y + 0.5D - o, z + 0.5D - o, x + 0.5D + o, y + 0.5D + o, z + 0.5D + o);
            if (axisalignedbb1 != null && aabb.intersectsWith(axisalignedbb1)) {
                list.add(axisalignedbb1);
            }
            UtilDirection[] directions = {UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.EAST, UtilDirection.WEST, UtilDirection.UP, UtilDirection.DOWN};
            for (UtilDirection direction : directions) {
                TileEntity te = direction.getTileEntity((IBlockAccess) world, x, y, z);
                if (te != null && te instanceof net.minecraft.inventory.IInventory) {
                    axisalignedbb1 = AxisAlignedBB.getBoundingBox(x + 0.5D - o + ((direction.offsetX == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetX == -1) ? (0.5D - o) : 0.0D), y + 0.5D - o + ((direction.offsetY == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetY == -1) ? (0.5D - o) : 0.0D), z + 0.5D - o + ((direction.offsetZ == 1) ? (o * 2.0D) : 0.0D) - ((direction.offsetZ == -1) ? (0.5D - o) : 0.0D), x + 0.5D + o - ((direction.offsetX == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetX == 1) ? (0.5D - o) : 0.0D), y + 0.5D + o - ((direction.offsetY == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetY == 1) ? (0.5D - o) : 0.0D), z + 0.5D + o - ((direction.offsetZ == -1) ? (o * 2.0D) : 0.0D) + ((direction.offsetZ == 1) ? (0.5D - o) : 0.0D));


                    if (axisalignedbb1 != null && aabb.intersectsWith(axisalignedbb1)) {
                        list.add(axisalignedbb1);
                    }
                }
            }
        }
    }


    public void addNormalCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity p_149743_7_) {
        super.addCollisionBoxesToList(world, x, y, z, aabb, list, p_149743_7_);
    }


    public void overrideTo(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block overriddenBlock, int overriddenMeta, Class overriddenTileEntityClass, NBTTagCompound overriddenTileEntityTag) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof IOverridableBlock) {
            ((IOverridableBlock) te).overrideTo(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, overriddenBlock, overriddenMeta, overriddenTileEntityClass, overriddenTileEntityTag);
        }
    }


    public boolean canOverride(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof IOverridableBlock) {
            return ((IOverridableBlock) te).canOverride(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return false;
    }


    public void onOverridden(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
        if (te instanceof IOverridableBlock) {
            ((IOverridableBlock) te).onOverridden(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
    }


    public List getTooltip(ItemStack itemStack) {
        return UtilLocale.localizeTooltip(getUnlocalizedName() + ".tooltip");
    }
}
