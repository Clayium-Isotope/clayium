package mods.clayium.machine.ClayBuffer;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.INormalInventory;
import mods.clayium.util.UtilTier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.stream.IntStream;

public class TileEntityClayBuffer extends TileEntityClayContainer implements INormalInventory {
    private int inventoryX;
    private int inventoryY;
    private int tier;

    // TileEntity#create() がチャンクのロード時にstaticで呼ばれるので、エラー回避用コンストラクタ
    public TileEntityClayBuffer() { this(0); }

    public TileEntityClayBuffer(int tier) {
        super(54);

        if (tier != 0)
            initByTier(tier);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return IntStream.range(0, this.inventoryX * this.inventoryY).toArray();
    }

    @Override
    public int getInventoryX() {
        return this.inventoryX;
    }

    @Override
    public int getInventoryY() {
        return this.inventoryY;
    }

    @Override
    public int getInventoryP() {
        return 1;
    }

    @Override
    public int getInventoryStart() {
        return 0;
    }

    private void initByTier(int tier) {
        this.tier = tier;

        switch (this.tier) {
            case 4:
                this.inventoryX = 1 + (this.inventoryY = 1);
                break;
            case 5:
                this.inventoryX = 1 + (this.inventoryY = 2);
                break;
            case 6:
                this.inventoryX = 1 + (this.inventoryY = 3);
                break;
            case 7:
                this.inventoryX = 1 + (this.inventoryY = 4);
                break;
            case 8:
                this.inventoryX = 5 + (this.inventoryY = 4);
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                this.inventoryX = 3 + (this.inventoryY = 6);
                break;
            default:
                this.inventoryX = this.inventoryY = 1;
                break;
        }

        // TODO refactor below
        int machineInvSize = this.inventoryX * this.inventoryY;
        int[] inserts = new int[machineInvSize];
        int[] extracts = new int[machineInvSize];

        for (int i = 0; i < machineInvSize; i++) {
            inserts[i] = i;
            extracts[i] = machineInvSize - 1 - i;
        }

        this.listSlotsImport.add(inserts);
        this.listSlotsExport.add(extracts);

        UtilTier.BufferTransport config = UtilTier.BufferTransport.getByTier(tier);
        if (config != null) {
            this.autoInsertInterval = config.autoInsertInterval;
            this.autoExtractInterval = config.autoExtractInterval;
            this.maxAutoInsertDefault = config.maxAutoInsertDefault;
            this.maxAutoExtractDefault = config.maxAutoExtractDefault;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        //
        initByTier(compound.getInteger("Tier"));

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Tier", this.tier);

        return super.writeToNBT(compound);
    }
}
