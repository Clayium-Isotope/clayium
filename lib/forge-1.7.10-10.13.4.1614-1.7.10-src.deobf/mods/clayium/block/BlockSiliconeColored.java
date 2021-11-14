package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemDye;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSiliconeColored
        extends BlockDamaged {
    public BlockSiliconeColored() {
        super(Material.iron);
        setBlockTextureName("clayium:siliconecolored");
        setBlockName("blockSilicone");
        setHardness(2.0F);
        setResistance(2.0F);
        setStepSound(Block.soundTypeMetal);

        for (int i = 0; i < 16; i++) {
            addBlockList(ItemDye.field_150921_b[BlockColored.func_150031_c(i)], i);
            setTier(5);
            setSubBlockSurfix(ItemDye.field_150921_b[BlockColored.func_150031_c(i)]);
            setIconName("clayium:siliconecolored");
        }
    }


    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        int r = (getMapColor(p_149741_1_)).colorValue >> 16 & 0xFF;
        int g = (getMapColor(p_149741_1_)).colorValue >> 8 & 0xFF;
        int b = (getMapColor(p_149741_1_)).colorValue & 0xFF;
        r = (r * 3 + 256) / 4;
        g = (g * 3 + 256) / 4;
        b = (b * 3 + 256) / 4;
        return (r << 16) + (g << 8) + b;
    }


    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return getRenderColor(world.getBlockMetadata(x, y, z));
    }


    public MapColor getMapColor(int p_149728_1_) {
        return MapColor.getMapColorForBlockColored(p_149728_1_);
    }


    public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta != colour) {

            world.setBlockMetadataWithNotify(x, y, z, colour, 3);
            return true;
        }
        return false;
    }
}
