package mods.clayium.machine.StorageContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.util.Constants;

import com.google.common.collect.ImmutableMap;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayContainer.BlockStateClaySidedContainer;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

@HasOriginalState
public class StorageContainer extends ClayHorizontalNoRecipeMachine {

    public StorageContainer() {
        super(TileEntityStorageContainer.class, EnumMachineKind.storageContainer, GuiHandler.GuiIdStorageContainer,
                TierPrefix.precision);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (!stack.hasTagCompound()) return;

        NBTTagCompound tag = stack.getTagCompound();
        assert Objects.nonNull(tag) : "Always pass";
        if (!tag.hasKey("TileEntityNBTTag", Constants.NBT.TAG_COMPOUND)) return;

        final NBTTagCompound tetag = (NBTTagCompound) tag.getTag("TileEntityNBTTag");

        for (NBTBase nbt : tetag.getTagList("Items", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound tagCompound = (NBTTagCompound) nbt;
            if (tagCompound.getByte("Slot") != 0) continue;

            final ItemStack item = new ItemStack(tagCompound);
            if (!item.isEmpty()) {
                tooltip.add(item.getDisplayName());
                tooltip.add(tetag.getInteger("ItemStackSize") + "/" +
                        StorageContainerSize.getByID(tetag.getInteger("MaxStorageSizeMeta")));
            }

            break;
        }

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        if (ClayiumItems.clayCore.equals(playerIn.getHeldItem(hand).getItem()) &&
                state.getValue(StorageContainerSize.STORAGE_SIZE) == StorageContainerSize.NORMAL) {
            worldIn.setBlockState(pos,
                    state.withProperty(StorageContainerSize.STORAGE_SIZE, StorageContainerSize.CLAY_CORE));
            playerIn.getHeldItem(hand).shrink(1);
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    /**
     * SPFFF<br>
     * P: is_pipe, F: facing, S: storage_size
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(StorageContainerSize.STORAGE_SIZE).ordinal();
        meta <<= 1;
        meta |= super.getMetaFromState(state);
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = super.getStateFromMeta(meta);
        state.withProperty(StorageContainerSize.STORAGE_SIZE, StorageContainerSize.getByID(meta >> 4));
        return state;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new StorageContainerStateContainer(this);
    }

    private static class StorageContainerStateContainer extends BlockStateContainer {

        public StorageContainerStateContainer(StorageContainer blockIn) {
            super(blockIn, BlockStateStorageContainer.getPropertyList().toArray(new IProperty[0]));
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
                                                  @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new BlockStateStorageContainer(block, properties);
        }
    }

    private static class BlockStateStorageContainer extends BlockStateClaySidedContainer {

        public static List<IProperty<?>> getPropertyList() {
            return Arrays.asList(
                    FACING, IS_PIPE, StorageContainerSize.STORAGE_SIZE,
                    ARM_UP, ARM_DOWN, ARM_NORTH, ARM_SOUTH, ARM_WEST, ARM_EAST);
        }

        protected BlockStateStorageContainer(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
            super(blockIn, propertiesIn);
        }
    }
}
