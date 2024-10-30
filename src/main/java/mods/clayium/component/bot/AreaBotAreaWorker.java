package mods.clayium.component.bot;

import mods.clayium.component.value.Stockholder;
import mods.clayium.util.WrapMutableBlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class AreaBotAreaWorker implements AreaBot {
    @Nonnull
    protected AxisAlignedBB aabb = NULL_AABB;
    protected final WrapMutableBlockPos pos = new WrapMutableBlockPos();
    protected boolean willTerminate = false;
    protected final Stockholder progress = new Stockholder();

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("pos", NBTUtil.createPosTag(this.pos));
        tag.setBoolean("will_terminate", this.willTerminate);
        tag.setTag("progress", this.progress.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.pos.setPos(NBTUtil.getPosFromTag(nbt.getCompoundTag("pos")));
        this.willTerminate = nbt.getBoolean("will_terminate");
        this.progress.deserializeNBT((NBTTagIntArray) nbt.getTag("progress"));
    }

    @Override
    public void reboot() {
        if (this.hasAxisAlignedBB()) {
            this.pos.setPos(this.aabb.minX, this.aabb.maxY, this.aabb.minZ);
        }
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
        if (aabb == NULL_AABB) {
            this.aabb = NULL_AABB;
            this.pos.setPos(0, 0, 0);
            return;
        }

        this.aabb = new AxisAlignedBB(
                Math.floor(aabb.minX + 0.5), Math.floor(aabb.maxY - 0.5), Math.floor(aabb.minZ + 0.5),
                Math.floor(aabb.maxX - 0.5), Math.floor(aabb.minY + 0.5), Math.floor(aabb.maxZ - 0.5)
        );
    }

    @Override
    public boolean hasAxisAlignedBB() {
        return this.aabb != NULL_AABB;
    }

    @Override
    public Appearance getBoxAppearance() {
        return Appearance.NoRender;
    }

    /**
     * EnumBotResult の濫用ではある
     */
    public EnumBotResult resetPos() {
        if (this.isReady()) {
            this.pos.setPos(this.aabb.minX, this.aabb.maxY, this.aabb.minZ);
            return EnumBotResult.Success;
        }

        if (this.willTerminate) {
            this.setAxisAlignedBB(NULL_AABB);
            return EnumBotResult.EndOfTerm;
        }

        return EnumBotResult.NotReady;
    }

    /**
     * EnumBotResult の濫用ではある
     */
    public EnumBotResult nextPos() {
        if (!this.isReady()) return EnumBotResult.NotReady;

        this.pos.incrX();
        if (this.pos.getX() > this.aabb.maxX) {
            this.pos.setX((int) this.aabb.minX);
            this.pos.incrZ();
        }

        if (this.pos.getZ() > this.aabb.maxZ) {
            this.pos.setZ((int) this.aabb.minZ);
            this.pos.decrY();
        }

        if (this.pos.getY() < this.aabb.minY) {
            return this.resetPos();
        }
        return EnumBotResult.Success;
    }

    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, LocalBot bot) {
        if (!this.isReady() || !bot.isReady()) return EnumBotResult.NotReady;

        bot.setProgressAccess(this);
        EnumBotResult botResult = bot.work(input, reference, output, this.pos.toImmutable());

        if (botResult == EnumBotResult.Success || botResult == EnumBotResult.Obstacle) {
            return this.nextPos();
        }
        return botResult;
    }
}
