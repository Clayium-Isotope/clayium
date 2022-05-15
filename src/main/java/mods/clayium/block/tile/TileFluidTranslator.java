package mods.clayium.block.tile;

import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilTransfer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileFluidTranslator
        extends TileClayBuffer implements IFluidHandler {
    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        this.listSlotsInsert.add(this.slotsDrop);
        this.listSlotsExtract.add(this.slotsDrop);
    }

    public void doAutoExtractFromSide(int side, int front) {
        int route = this.insertRoutes[side];
        if (route == 0) {
            UtilTransfer.extractFromTank(this, this.listSlotsInsert.get(route), UtilDirection.getOrientation(front), UtilDirection.getSide(side), (this.maxAutoExtract != null && route < this.maxAutoExtract.length && this.maxAutoExtract[route] >= 0) ? this.maxAutoExtract[route] : this.maxAutoExtractDefault);
        } else {
            super.doAutoExtractFromSide(side, front);
        }
    }

    public void doAutoInsertToSide(int side, int front) {
        int route = this.extractRoutes[side];
        if (route == 0) {
            UtilTransfer.insertToTank(this, this.listSlotsExtract.get(route), UtilDirection.getOrientation(front), UtilDirection.getSide(side), (this.maxAutoInsert != null && route < this.maxAutoInsert.length && this.maxAutoInsert[route] >= 0) ? this.maxAutoInsert[route] : this.maxAutoInsertDefault);
        } else {
            super.doAutoInsertToSide(side, front);
        }
    }

    public void doAutoExtract() {
        super.doAutoExtract();
        UtilTransfer.sortFluid(this.containerItemStacks, this.listSlotsInsert.get(0));
    }


    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return UtilTransfer.fillFluid(this.containerItemStacks, this.listSlotsInsert.get(0), resource, doFill);
    }


    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return UtilTransfer.drainFluid(this.containerItemStacks, this.listSlotsInsert.get(0), resource, doDrain);
    }


    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return UtilTransfer.drainFluid(this.containerItemStacks, this.listSlotsInsert.get(0), maxDrain, doDrain);
    }


    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return UtilTransfer.canFillFluid(this.containerItemStacks, this.listSlotsInsert.get(0), fluid);
    }


    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return UtilTransfer.canDrainFluid(this.containerItemStacks, this.listSlotsInsert.get(0), fluid);
    }


    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return UtilTransfer.getTankInfo(this.containerItemStacks, this.listSlotsInsert.get(0));
    }
}
