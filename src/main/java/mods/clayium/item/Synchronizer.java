package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;
import mods.clayium.item.common.IModifyCC;
import mods.clayium.machine.Interface.ClayInterface.ClayInterface;
import mods.clayium.machine.Interface.ClayInterface.TileEntityClayInterface;
import mods.clayium.machine.common.IInterfaceCaptive;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class Synchronizer extends ClayiumItem implements IModifyCC {
    private static final String SourceKey = "SyncSource";

    public Synchronizer() {
        super("synchronizer");

        setMaxStackSize(1);
    }

    // TODO Server-Client同期問題が起こる予感
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        assert !itemStack.isEmpty();
        Block block = worldIn.getBlockState(pos).getBlock();
        TileEntity tile = worldIn.getTileEntity(pos);

        if (player.isSneaking()) { // インターフェースにShift+右クリックでシンクロ
            return applySync(player, itemStack, worldIn, pos, block, tile);
        } else { // 機械に右クリックで同期元を設定
            return makeReadyToSync(player, itemStack, worldIn, pos, block, tile);
        }
    }

    protected static EnumActionResult applySync(EntityPlayer player, ItemStack itemStack, World world, BlockPos pos, Block block, TileEntity tile) {
        if (!(block instanceof ClayInterface && tile instanceof TileEntityClayInterface)) {
            if (world.isRemote) {
                player.sendMessage(UtilLocale.localizeAndFormatComponent("info.sync.InvalidSyncTo"));
            }
            return EnumActionResult.PASS;
        }

        int[] coord; // [I; DimID, x, y, z]
        if (!itemStack.hasTagCompound() || itemStack.getTagCompound() == null
                || !itemStack.getTagCompound().hasKey(SourceKey, Constants.NBT.TAG_INT_ARRAY)
                || (coord = itemStack.getTagCompound().getIntArray(SourceKey)).length != 4
        ) {
            if (world.isRemote) {
                player.sendMessage(UtilLocale.localizeAndFormatComponent("info.sync.HasNoTarget"));
            }
            return EnumActionResult.PASS;
        }

        World coreWorld = DimensionManager.getWorld(coord[0]);
        BlockPos corePos = new BlockPos(coord[1], coord[2], coord[3]);

        if (!IInterfaceCaptive.isSyncable(coreWorld.getTileEntity(corePos))) {
            if (world.isRemote) {
                player.sendMessage(UtilLocale.localizeAndFormatComponent("info.sync.TargetNotExist"));
            }
            return EnumActionResult.PASS;
        }

        // TODO 登録処理

        if (world.isRemote) {
            player.sendMessage(UtilLocale.localizeAndFormatComponent("info.sync.SuccessSync"));
        }
        itemStack.getTagCompound().removeTag(SourceKey);

        return EnumActionResult.SUCCESS;
    }

    protected static EnumActionResult makeReadyToSync(EntityPlayer player, ItemStack itemStack, World world, BlockPos pos, Block block, TileEntity tile) {
        if (!IInterfaceCaptive.isSyncable(tile)) {
            if (world.isRemote) {
                player.sendMessage(UtilLocale.localizeAndFormatComponent("info.sync.SourceDisagreed"));
            }
            return EnumActionResult.PASS;
        }

        if (world.isRemote) {
            player.sendMessage(UtilLocale.localizeAndFormatComponent("info.sync.SuccessSetSource"));
        }

        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();

        assert compound != null;
        compound.setIntArray(SourceKey, new int[] {world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ() });

        itemStack.setTagCompound(compound);

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);

        if (player == null || !player.isRemote)
            return;

        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(SourceKey, Constants.NBT.TAG_INT_ARRAY))
            return;

        int[] coord = stack.getTagCompound().getIntArray(SourceKey);

        tooltip.add("");
        tooltip.add(UtilLocale.localizeAndFormat("info.sync.tooltip.Source", coord[1], coord[2], coord[3], DimensionManager.getProviderType(coord[0]).getName()));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack;
    }
}
