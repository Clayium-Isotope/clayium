package mods.clayium.block.tile;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.pan.IPANAdapter;
import mods.clayium.pan.IPANAdapterConversionFactory;
import mods.clayium.pan.IPANConversion;
import mods.clayium.pan.IPANConversionProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TilePANAdapter
        extends TileClayContainerTiered
        implements IPANConversionProvider {
    protected int pageNum;
    protected ForgeDirection connectedDirection = null;
    protected boolean connected = false;
    public static List<IPANAdapterConversionFactory> conversionFactories = new ArrayList<IPANAdapterConversionFactory>();

    public static void addConversionFactory(IPANAdapterConversionFactory factory) {
        if (conversionFactories == null)
            conversionFactories = new ArrayList<IPANAdapterConversionFactory>();
        conversionFactories.add(factory);
    }

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.autoExtract = this.autoInsert = false;
        this.containerItemStacks = new ItemStack[153];
        this.slotsDrop = new int[9];
        for (int i = 0; i < this.slotsDrop.length; i++) {
            this.slotsDrop[i] = 144 + i;
        }
    }

    public void initParamsByTier(int tier) {
        this.pageNum = 1;
        switch (tier) {
            case 11:
                this.pageNum = 2;
                break;
            case 12:
                this.pageNum = 4;
                break;
            case 13:
                this.pageNum = 8;
                break;
        }

    }

    public int getPageNum() {
        return this.pageNum;
    }

    private int count = 0;

    public void updateEntity() {
        super.updateEntity();
        this.count++;
        if (((this.count % 40 == 0 && this.refreshResultSlotDelayedFlag) || this.refreshResultSlotInstantFlag) && !this.worldObj.isRemote) {
            refreshResultSlot();
        }
    }

    public void refreshResultSlot() {
        getConversion();
        for (int p = 0; p < 8; p++) {
            IPANConversion conversion = this.connected ? getConversion(this.connectedDirection, p) : null;
            if (conversion != null) {
                ItemStack[] results = conversion.getResults();
                for (int i = 0; i < 9; i++) {
                    this.containerItemStacks[9 + i + 18 * p] = (i < results.length) ? results[i] : null;
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    this.containerItemStacks[9 + i + 18 * p] = null;
                }
            }
        }

        setSyncFlag();
        this.refreshResultSlotDelayedFlag = false;
        this.refreshResultSlotInstantFlag = false;
    }


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot % 18 >= 0 && slot % 18 < 9)
            return true;
        return super.isItemValidForSlot(slot, itemstack);
    }

    protected IPANConversion[] conversions = null;
    protected boolean refreshFlag = true;

    public IPANConversion[] refreshConversion() {
        this.connectedDirection = null;
        this.connected = false;

        List<IPANConversion> res = new ArrayList<IPANConversion>();
        for (int p = 0; p < 8; p++) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (this.connectedDirection == null || this.connectedDirection == direction) {
                    IPANConversion conversion = getConversion(direction, p);
                    if (conversion != null) {
                        this.connectedDirection = direction;
                        this.connected = true;
                        res.add(conversion);
                    }
                }
            }
        }
        return this.connected ? res.<IPANConversion>toArray(new IPANConversion[0]) : null;
    }


    public IPANConversion[] getConversion() {
        if (this.refreshFlag) {
            this.conversions = refreshConversion();
        }
        return this.conversions;
    }

    public void setRefreshConversionFlag() {
        this.refreshFlag = true;
        for (int i = 0; i < this.pagesRefreshFlag.length; i++) {
            for (int j = 0; j < (this.pagesRefreshFlag[i]).length; j++)
                this.pagesRefreshFlag[i][j] = true;
        }
    }

    public void onNeighborChange() {
        setRefreshConversionFlag();
        setRefreshResultSlotFlag();
        setSyncFlag();
    }

    public void onSlotChange() {
        setRefreshConversionFlag();
        setInstantRefreshResultSlotFlag();
        setSyncFlag();
    }

    protected boolean refreshResultSlotInstantFlag = true;
    protected boolean refreshResultSlotDelayedFlag = true;

    public void setRefreshResultSlotFlag() {
        this.refreshResultSlotDelayedFlag = true;
    }

    public void setInstantRefreshResultSlotFlag() {
        this.refreshResultSlotInstantFlag = true;
    }

    protected IPANConversion[][] conversionPages = new IPANConversion[6][8];


    protected boolean[][] pagesRefreshFlag = new boolean[6][8];

    public TilePANAdapter() {
        for (int i = 0; i < this.pagesRefreshFlag.length; i++) {
            for (int j = 0; j < (this.pagesRefreshFlag[i]).length; j++)
                this.pagesRefreshFlag[i][j] = true;
        }
    }

    public IPANConversion getConversion(ForgeDirection direction, int page) {
        int d = direction.ordinal();
        if (this.pagesRefreshFlag[d][page]) {
            this.conversionPages[d][page] = refreshConversion(direction, page);
        }
        return this.conversionPages[d][page];
    }

    public IPANConversion refreshConversion(ForgeDirection direction, int page) {
        for (IPANAdapterConversionFactory factory : conversionFactories) {
            if (factory.match(this.worldObj, this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ)) {
                IPANConversion conversion = factory.getConversion(new InternalPANAdapter(direction, page));
                if (conversion != null) {
                    return conversion;
                }
            }
        }
        return null;
    }

    public class InternalPANAdapter
            implements IPANAdapter {
        int page = 0;
        ForgeDirection direction = null;

        public InternalPANAdapter(ForgeDirection direction, int page) {
            this.page = page;
            this.direction = direction;
        }


        public ItemStack[] getPatternItems() {
            ItemStack[] res = new ItemStack[9];
            for (int i = 0; i < 9; i++) {
                res[i] = TilePANAdapter.this.containerItemStacks[i + this.page * 18];
            }
            return res;
        }


        public ItemStack[] getSubItems() {
            ItemStack[] res = new ItemStack[9];
            for (int i = 0; i < 9; i++) {
                res[i] = TilePANAdapter.this.containerItemStacks[i + 144];
            }
            return res;
        }


        public World getConnectedWorld() {
            return TilePANAdapter.this.worldObj;
        }


        public int getConnectedXCoord() {
            return TilePANAdapter.this.xCoord + ((this.direction != null) ? this.direction.offsetX : 0);
        }


        public int getConnectedYCoord() {
            return TilePANAdapter.this.yCoord + ((this.direction != null) ? this.direction.offsetY : 0);
        }


        public int getConnectedZCoord() {
            return TilePANAdapter.this.zCoord + ((this.direction != null) ? this.direction.offsetZ : 0);
        }
    }

    public void openInventory() {}

    public void closeInventory() {}
}
