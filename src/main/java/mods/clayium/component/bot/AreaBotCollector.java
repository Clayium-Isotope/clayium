package mods.clayium.component.bot;

import mods.clayium.component.EnumBotResult;
import mods.clayium.component.Stockholder;
import mods.clayium.item.filter.IFilter;
import mods.clayium.util.UtilNBT;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AreaBotCollector implements AreaBot {
    public static final int progressPerJob = 400;
    protected final Stockholder progress = Stockholder.init();
    protected boolean willTerminate = false;
    @Nonnull
    protected AxisAlignedBB aabb = NULL_AABB;
    @Nullable protected World world = null;

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("aabb", UtilNBT.serializeAABB(this.getAxisAlignedBB()));
        UtilNBT.addWorldToTag(tag, this.world);
        tag.setBoolean("will_terminate", this.willTerminate);
        tag.setTag("progress", this.progress().serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.setAxisAlignedBB(UtilNBT.deserializeAABB(nbt.getCompoundTag("aabb")));
        this.setWorld(UtilNBT.getWorldFromTag(nbt));
        this.setFinite(nbt.getBoolean("will_terminate"));
        this.progress().deserializeNBT((NBTTagIntArray) nbt.getTag("progress"));
    }

    @Override
    public Stockholder progress() {
        return this.progress;
    }

    @Override
    public void setFinite(boolean willTerminate) {
        this.willTerminate = willTerminate;
    }

    @Override
    public AxisAlignedBB getAxisAlignedBB() {
        return this.aabb;
    }

    @Override
    public void setAxisAlignedBB(@Nonnull AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    @Override
    public boolean hasAxisAlignedBB() {
        return this.aabb != NULL_AABB;
    }

    @Override
    public Appearance getBoxAppearance() {
        return Appearance.NoRender;
    }

    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, LocalBot localBot) {
        if (!this.isReady()) {
            return EnumBotResult.NotReady;
        }
        assert this.world != null;
        List<EntityItem> list = this.world.getEntitiesWithinAABB(EntityItem.class, this.aabb, item -> true);

        ItemStack filter = reference.getStackInSlot(0);

        if (list.isEmpty()) {
            return EnumBotResult.Incomplete;
        }

        for (EntityItem eitem : list) {
            ItemStack item = eitem.getItem().copy();
            ItemStack item1 = item.copy();
            item1.setCount(1);

            if (!IFilter.match(filter, item)) {
                continue;
            }

            boolean flag1 = true;

            while (flag1 && this.hasEnoughProgress(this.progressPerJob())) {
                if (!ItemHandlerHelper.insertItemStacked(output, item, true).isEmpty() || !this.hasEnoughProgress(this.progressPerJob())) {
                    break;
                }

                this.declineProgress(this.progressPerJob());
                ItemStack rest = ItemHandlerHelper.insertItemStacked(output, item, false);
                if (rest.isEmpty()) {
                    eitem.setDead();
                    flag1 = false;
                }

                if (this.willTerminate) {
                    this.clearProgress();
                    return EnumBotResult.EndOfTerm;
                }
            }
        }

        return EnumBotResult.Success;
    }

    @Override
    public long progressPerJob() {
        return (long) ((double) progressPerJob * 1.0);
    }

    public void setWorld(@Nullable World world) {
        this.world = world;
    }

    @Override
    public boolean isReady() {
        return AreaBot.super.isReady() && this.world != null;
    }
}
