package mods.clayium.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import mods.clayium.block.tile.TilePANCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;


public class PANCoreListPacket
        implements IMessage {
    public int windowId;
    public Set<TilePANCore.ItemStackWithEnergy> ingreds;
    public Set<TilePANCore.ItemStackWithEnergy> prohibiteds;

    public PANCoreListPacket() {}

    public PANCoreListPacket(int windowId, TilePANCore panCore) {
        this.windowId = windowId;
        this.ingreds = panCore.getIngredientItemSet();
        this.prohibiteds = panCore.getProhibitedItemSet();
    }


    public void fromBytes(ByteBuf buf) {
        this.windowId = buf.readInt();
        try {
            this.ingreds = readSetFromBuffer(buf);
            this.prohibiteds = readSetFromBuffer(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.windowId);
        try {
            writeSetToBuffer(this.ingreds, buf);
            writeSetToBuffer(this.prohibiteds, buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeSetToBuffer(Set<TilePANCore.ItemStackWithEnergy> set, ByteBuf buf) throws IOException {
        if (set != null) {
            buf.writeInt(set.size());

            for (TilePANCore.ItemStackWithEnergy item : set) {
                writeItemStackToBuffer(item.itemstack, buf);
                buf.writeDouble(item.cost);
                buf.writeDouble(item.consumption);
            }
        }
    }


    public static Set<TilePANCore.ItemStackWithEnergy> readSetFromBuffer(ByteBuf buf) throws IOException {
        Set<TilePANCore.ItemStackWithEnergy> set = new TreeSet<TilePANCore.ItemStackWithEnergy>((Comparator<? super TilePANCore.ItemStackWithEnergy>) new TilePANCore.ItemStackComparator());
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            ItemStack itemstack = readItemStackFromBuffer(buf);
            double cost = buf.readDouble();
            double consumption = buf.readDouble();
            set.add(new TilePANCore.ItemStackWithEnergy(itemstack, cost, consumption));
        }
        return set;
    }


    public static void writeItemStackToBuffer(ItemStack p_150788_1_, ByteBuf buf) throws IOException {
        if (p_150788_1_ == null) {

            buf.writeShort(-1);
        } else {

            buf.writeShort(Item.getIdFromItem(p_150788_1_.getItem()));
            buf.writeByte(p_150788_1_.stackSize);
            buf.writeShort(p_150788_1_.getItemDamage());
            NBTTagCompound nbttagcompound = null;

            if (p_150788_1_.getItem().isDamageable() || p_150788_1_.getItem().getShareTag()) {
                nbttagcompound = p_150788_1_.stackTagCompound;
            }

            writeNBTTagCompoundToBuffer(nbttagcompound, buf);
        }
    }


    public static ItemStack readItemStackFromBuffer(ByteBuf buf) throws IOException {
        ItemStack itemstack = null;
        short short1 = buf.readShort();

        if (short1 >= 0) {

            byte b0 = buf.readByte();
            short short2 = buf.readShort();
            itemstack = new ItemStack(Item.getItemById(short1), b0, short2);
            itemstack.stackTagCompound = readNBTTagCompoundFromBuffer(buf);
        }

        return itemstack;
    }


    public static void writeNBTTagCompoundToBuffer(NBTTagCompound p_150786_1_, ByteBuf buf) throws IOException {
        if (p_150786_1_ == null) {

            buf.writeShort(-1);
        } else {

            byte[] abyte = CompressedStreamTools.compress(p_150786_1_);
            buf.writeShort((short) abyte.length);
            buf.writeBytes(abyte);
        }
    }


    public static NBTTagCompound readNBTTagCompoundFromBuffer(ByteBuf buf) throws IOException {
        short short1 = buf.readShort();

        if (short1 < 0) {
            return null;
        }


        byte[] abyte = new byte[short1];
        buf.readBytes(abyte);
        return CompressedStreamTools.func_152457_a(abyte, new NBTSizeTracker(2097152L));
    }
}
