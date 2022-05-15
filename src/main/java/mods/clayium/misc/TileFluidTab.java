package mods.clayium.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileFluidTab
        extends TileEntity implements IFluidHandler {
    public ClayTank productTank = new ClayTank(1000);


    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        this.productTank = new ClayTank(1000);
        if (par1NBTTagCompound.hasKey("productTank")) {
            this.productTank.readFromNBT(par1NBTTagCompound.getCompoundTag("productTank"));
        }
    }


    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        NBTTagCompound tank = new NBTTagCompound();
        this.productTank.writeToNBT(tank);
        par1NBTTagCompound.setTag("productTank", (NBTBase) tank);
    }


    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return (Packet) new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }


    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }


    @SideOnly(Side.CLIENT)
    public int getMetadata() {
        return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }


    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon() {
        Fluid fluid = this.productTank.getFluidType();
        return (fluid != null) ? fluid.getIcon() : null;
    }


    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (this.productTank.getFluidType() == resource.getFluid()) {
            return this.productTank.drain(resource.amount, doDrain);
        }
        return null;
    }


    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.productTank.drain(maxDrain, doDrain);
    }


    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() == null) {
            return 0;
        }

        FluidStack current = this.productTank.getFluid();
        FluidStack resourceCopy = resource.copy();
        if (current != null && current.amount > 0 && !current.isFluidEqual(resourceCopy)) {
            return 0;
        }

        int i = 0;
        int used = this.productTank.fill(resourceCopy, doFill);
        resourceCopy.amount -= used;
        i += used;

        return i;
    }


    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return (fluid != null && this.productTank.isEmpty());
    }


    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }


    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] {this.productTank.getInfo()};
    }
}
