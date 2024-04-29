package mods.clayium.item;

import mods.clayium.item.common.ItemTiered;
import mods.clayium.util.TierPrefix;

public class DirectionMemory extends ItemTiered {

    public DirectionMemory() {
        super("direction_memory", TierPrefix.none);
        this.setMaxStackSize(1);
    }

    /*
     * TODO 範囲代行機 Area Activator の実装に則る
     * 
     * @Override
     * public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float
     * hitX, float hitY, float hitZ, EnumHand hand) {
     * 
     * }
     * 
     * public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int
     * side, float posX, float posY, float posZ) {
     * TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
     * NBTTagCompound tag = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
     * if (tag.hasKey("RayTraceMemory", 10)) {
     * RayTraceMemory memory = RayTraceMemory.getFromNBT((NBTTagCompound)tag.getTag("RayTraceMemory"));
     * if (te instanceof IRayTracer && ((IRayTracer)te).acceptRayTraceMemory(memory)) {
     * ((IRayTracer)te).setRayTraceMemory(memory);
     * if (!world.isRemote) {
     * player.addChatMessage(new ChatComponentText("Applied direction memory."));
     * }
     * 
     * return !world.isRemote;
     * }
     * }
     * 
     * double eyeHeight = world.isRemote ? (double)(player.getEyeHeight() - player.getDefaultEyeHeight()) :
     * (double)player.getEyeHeight();
     * RayTraceMemory memoryx = new RayTraceMemory(Vec3.createVectorHelper(player.posX, player.posY + eyeHeight,
     * player.posZ), Vec3.createVectorHelper((double)((float)x + posX), (double)((float)y + posY), (double)((float)z +
     * posZ)), side);
     * NBTTagCompound memoryTag = new NBTTagCompound();
     * memoryx.writeToNBT(memoryTag);
     * tag.setTag("RayTraceMemory", memoryTag);
     * itemstack.setTagCompound(tag);
     * if (!world.isRemote) {
     * player.addChatMessage(new ChatComponentText("Saved your direction to memory."));
     * }
     * 
     * return !world.isRemote;
     * }
     */
}
