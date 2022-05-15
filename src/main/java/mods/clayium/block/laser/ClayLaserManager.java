package mods.clayium.block.laser;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.CBlocks;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ClayLaserManager {
    public ClayLaser clayLaser;
    public static int historyLength = 10;
    public static int laserLengthMax = 32;

    public long totalIrradiatedEnergy = 0L;
    protected long[] laserEnergyHistory = new long[historyLength];

    protected int targetBlockId;
    protected int targetBlockMeta;
    protected int laserLength;
    protected World world;
    protected int x;
    protected int y;
    protected int z;
    protected UtilDirection direction;
    protected int xx;
    protected int yy;
    protected int zz;
    protected boolean hasTarget;
    protected boolean isIrradiating;
    protected boolean initialized = false;

    public ClayLaserManager() {
        this.initialized = false;
    }

    public ClayLaserManager(World world, int x, int y, int z, UtilDirection direction) {
        reset(world, x, y, z, direction);
    }

    public void reset(World world, int x, int y, int z, UtilDirection direction) {
        this.laserEnergyHistory = new long[historyLength];
        this.totalIrradiatedEnergy = 0L;
        this.targetBlockId = 0;
        this.targetBlockMeta = 0;
        this.laserLength = 0;
        this.hasTarget = false;
        this.isIrradiating = false;
        set(world, x, y, z, direction);
    }

    public void set(World world, int x, int y, int z, UtilDirection direction) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.initialized = true;
    }

    public void update(boolean irradiation) {
        if (irradiation) {
            if (this.initialized) {
                this.isIrradiating = false;
                if (this.clayLaser != null) {
                    int[] numbers = this.clayLaser.numbers;
                    if (numbers != null) {
                        boolean flag = false;
                        for (int n : numbers) {
                            if (n >= 1)
                                flag = true;
                        }
                        if (flag) {
                            irradiateLaser();
                            this.isIrradiating = true;
                        }
                    }
                }
            }
        } else {

            this.isIrradiating = false;
        }
    }

    public void irradiateLaser() {
        if (this.world == null)
            return;
        int x = this.x, dx = this.direction.offsetX;
        int y = this.y, dy = this.direction.offsetY;
        int z = this.z, dz = this.direction.offsetZ;
        int newTargetBlockId = 0;
        int newTargetBlockMeta = 0;
        int newLaserLength = laserLengthMax;
        this.hasTarget = false;
        for (int i = 1; i < laserLengthMax; i++) {
            int xx = x + i * dx;
            int yy = y + i * dy;
            int zz = z + i * dz;
            if (!canGoThrough(xx, yy, zz)) {
                Block block = this.world.getBlock(xx, yy, zz);
                newTargetBlockId = Block.getIdFromBlock(block);
                newTargetBlockMeta = this.world.getBlockMetadata(xx, yy, zz);
                newLaserLength = i;
                this.hasTarget = true;
                this.xx = xx;
                this.yy = yy;
                this.zz = zz;
                break;
            }
        }
        if (!this.hasTarget || newTargetBlockId != this.targetBlockId || newTargetBlockMeta != this.targetBlockMeta || newLaserLength != this.laserLength) {


            this.laserEnergyHistory = new long[historyLength];
            this.totalIrradiatedEnergy = 0L;
        }
        this.targetBlockId = newTargetBlockId;
        this.targetBlockMeta = newTargetBlockMeta;
        this.laserLength = newLaserLength;
        if (this.hasTarget) {
            long n = (long) this.clayLaser.getEnergy();

            this.totalIrradiatedEnergy += n;
            long m = n;
            for (long h : this.laserEnergyHistory) {
                m = Math.min(m, h);
            }

            this.totalIrradiatedEnergy = (long) (m * this.totalIrradiatedEnergy / n);
            for (int j = 0; j < this.laserEnergyHistory.length; j++) {
                if (j < this.laserEnergyHistory.length - 1) {
                    this.laserEnergyHistory[j] = this.laserEnergyHistory[j + 1];
                } else {

                    this.laserEnergyHistory[j] = n;
                }
            }

            Block block = this.world.getBlock(this.xx, this.yy, this.zz);
            int meta = this.world.getBlockMetadata(this.xx, this.yy, this.zz);
            TileEntity tile = UtilBuilder.safeGetTileEntity((IBlockAccess) this.world, this.xx, this.yy, this.zz);


            if (block.getMaterial() == Material.rock && block != Blocks.bedrock && this.totalIrradiatedEnergy >= (long) (block.getBlockHardness(this.world, this.xx, this.yy, this.zz) + 1.0F) * 100L) {
                ItemStack[] items = harvestBlock(this.world, this.xx, this.yy, this.zz);
                if (block == Blocks.coal_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.coal_block, 0, 3);
                } else if (block == Blocks.iron_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.iron_block, 0, 3);
                } else if (block == Blocks.gold_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.gold_block, 0, 3);
                } else if (block == Blocks.diamond_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.diamond_block, 0, 3);
                } else if (block == Blocks.redstone_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.redstone_block, 0, 3);
                } else if (block == Blocks.lapis_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.lapis_block, 0, 3);
                } else if (block == Blocks.emerald_ore) {
                    this.world.setBlock(this.xx, this.yy, this.zz, Blocks.emerald_block, 0, 3);
                } else {
                    dropItems(this.world, this.xx, this.yy, this.zz, items);
                }
            }

            if (block == Blocks.sapling && this.totalIrradiatedEnergy >= 300L && this.totalIrradiatedEnergy < 1000L) {
                harvestBlock(this.world, this.xx, this.yy, this.zz);
                this.world.setBlock(this.xx, this.yy, this.zz, Blocks.sapling, (meta >= 5) ? 0 : (meta + 1), 3);
            }
            if (block == Blocks.sapling && this.totalIrradiatedEnergy >= 300000L) {
                harvestBlock(this.world, this.xx, this.yy, this.zz);
                this.world.setBlock(this.xx, this.yy, this.zz, CBlocks.blockClayTreeSapling, 0, 3);
            }
            if (tile != null && tile instanceof IClayLaserMachine) {
                ((IClayLaserMachine) tile).irradiateClayLaser(this.clayLaser, this.direction.getOpposite());
            }
        }
    }

    protected boolean canGoThrough(int x, int y, int z) {
        if (this.world == null) return false;
        Block block = this.world.getBlock(x, y, z);
        if (block == null) return false;
        return (block.getMaterial() == Material.air || block.getMaterial() == Material.glass);
    }

    public UtilDirection getDirection() {
        return this.direction;
    }

    public int getLaserLength() {
        return this.laserLength;
    }

    public int[] getTargetCoord() {
        (new int[3])[0] = this.xx;
        (new int[3])[1] = this.yy;
        (new int[3])[2] = this.zz;
        return this.hasTarget ? new int[3] : null;
    }

    public boolean hasTarget() {
        return this.hasTarget;
    }

    public boolean isIrradiating() {
        return this.isIrradiating;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        this.totalIrradiatedEnergy = tagCompound.getLong("TotalIrradiatedEnergy");


        NBTTagList tagList = tagCompound.getTagList("LaserEnergyHistory", 10);
        this.laserEnergyHistory = new long[historyLength];

        for (int i = 0; i < tagList.tagCount() && i < this.laserEnergyHistory.length; i++) {
            NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
            this.laserEnergyHistory[i] = tagCompound1.getLong("Energy");
        }
        this.targetBlockId = tagCompound.getInteger("TargetBlockId");
        this.targetBlockMeta = tagCompound.getInteger("TargetBlockMeta");
        this.laserLength = tagCompound.getInteger("LaserLength");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setLong("TotalIrradiatedEnergy", this.totalIrradiatedEnergy);
        NBTTagList tagList = new NBTTagList();
        for (long h : this.laserEnergyHistory) {
            NBTTagCompound tagCompound1 = new NBTTagCompound();
            tagCompound1.setLong("Energy", h);
            tagList.appendTag((NBTBase) tagCompound1);
        }
        tagCompound.setTag("LaserEnergyHistory", (NBTBase) tagList);
        tagCompound.setInteger("TargetBlockId", this.targetBlockId);
        tagCompound.setInteger("TargetBlockMeta", this.targetBlockMeta);
        tagCompound.setInteger("LaserLength", this.laserLength);
    }


    public ItemStack[] harvestBlock(World theWorld, int xx, int yy, int zz) {
        List<ItemStack> itemToDrop = new ArrayList<ItemStack>();

        Block block = theWorld.getBlock(xx, yy, zz);
        int l = theWorld.getBlockMetadata(xx, yy, zz);

        if (!theWorld.isRemote)
            theWorld.playAuxSFXAtEntity(null, 2001, xx, yy, zz, Block.getIdFromBlock(block) + (theWorld.getBlockMetadata(xx, yy, zz) << 12));
        theWorld.setBlockToAir(xx, yy, zz);


        boolean dropXP = false;
        boolean canSilkHarvest = false;
        int fortune = 0;
        if (canSilkHarvest) {


            int j = 0;
            Item item = Item.getItemFromBlock(block);

            if (item != null && item.getHasSubtypes()) {
                j = l;
            }
            ItemStack itemstack = new ItemStack(item, 1, j);


            itemToDrop.add(itemstack);
        } else {

            float g = 1.0F;
            if (!theWorld.isRemote && !theWorld.restoringBlockSnapshots) {

                ArrayList<ItemStack> items = block.getDrops(theWorld, xx, yy, zz, l, fortune);
                g = ForgeEventFactory.fireBlockHarvesting(items, theWorld, block, xx, yy, zz, l, fortune, g, false, null);

                for (ItemStack item : items) {

                    if (theWorld.rand.nextFloat() <= g) {


                        itemToDrop.add(item);
                    }
                }
            }
        }
        if (dropXP)
            block.dropXpOnBlockBreak(theWorld, xx, yy, zz, block.getExpDrop((IBlockAccess) theWorld, l, fortune));
        return itemToDrop.<ItemStack>toArray(new ItemStack[0]);
    }


    public void dropItems(World theWorld, int xx, int yy, int zz, ItemStack[] itemToDrop) {
        for (ItemStack item : itemToDrop) {
            if (!theWorld.isRemote && theWorld.getGameRules().getGameRuleBooleanValue("doTileDrops") && !theWorld.restoringBlockSnapshots) {

                float f = 0.7F;
                double d0 = (theWorld.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
                double d1 = (theWorld.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
                double d2 = (theWorld.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
                EntityItem entityitem = new EntityItem(theWorld, xx + d0, yy + d1, zz + d2, item);
                entityitem.delayBeforeCanPickup = 10;
                theWorld.spawnEntityInWorld((Entity) entityitem);
            }
        }
    }
}
