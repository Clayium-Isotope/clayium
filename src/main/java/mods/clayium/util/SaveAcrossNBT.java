package mods.clayium.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * この interface を頼りに、NBT読み書きができると保証する機会はないのが望ましい。<br>
 * class ごとに、適切な場面でNBTを利用するよう心掛けるべき。
 */
public interface SaveAcrossNBT {
    void readFromNBT(NBTTagCompound compound);
    NBTTagCompound writeToNBT(NBTTagCompound compound);
}
