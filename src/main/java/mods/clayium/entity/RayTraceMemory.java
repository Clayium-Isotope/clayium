package mods.clayium.entity;

import cpw.mods.fml.common.eventhandler.Event;

import java.util.List;

import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilCoordinate;
import mods.clayium.util.UtilPlayer;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;


public class RayTraceMemory {
    protected Vec3 pos;
    protected Vec3 hit;
    protected Vec3 look;
    protected Vec3 normalizedLook;
    protected float yaw;
    protected float pitch;
    protected int hitRelCoordX;
    protected int hitRelCoordY;
    protected int hitRelCoordZ;
    protected int hitSide;

    public Vec3 getPos() {
        return this.pos;
    }

    public Vec3 getHit() {
        return this.hit;
    }

    public Vec3 getLook() {
        return this.look;
    }

    public Vec3 getNormalizedLook() {
        return this.normalizedLook;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int getHitRelCoordX() {
        return this.hitRelCoordX;
    }

    public int getHitRelCoordY() {
        return this.hitRelCoordY;
    }

    public int getHitRelCoordZ() {
        return this.hitRelCoordZ;
    }

    public int getHitSide() {
        return this.hitSide;
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (tag == null)
            return;
        tag.setDouble("posX", this.pos.xCoord);
        tag.setDouble("posY", this.pos.yCoord);
        tag.setDouble("posZ", this.pos.zCoord);
        tag.setDouble("hitX", this.hit.xCoord);
        tag.setDouble("hitY", this.hit.yCoord);
        tag.setDouble("hitZ", this.hit.zCoord);
        tag.setInteger("hitRX", this.hitRelCoordX);
        tag.setInteger("hitRY", this.hitRelCoordY);
        tag.setInteger("hitRZ", this.hitRelCoordZ);
        tag.setByte("hitSide", (byte) this.hitSide);
    }

    public void readFromNBT(NBTTagCompound tag) {
        if (tag == null)
            return;
        set(Vec3.createVectorHelper(tag.getDouble("posX"), tag.getDouble("posY"), tag.getDouble("posZ")),
                Vec3.createVectorHelper(tag.getDouble("hitX"), tag.getDouble("hitY"), tag.getDouble("hitZ")), tag
                        .getInteger("hitRX"), tag.getInteger("hitRY"), tag.getInteger("hitRZ"));
        this.hitSide = tag.getByte("hitSide");
    }

    public static RayTraceMemory getFromNBT(NBTTagCompound tag) {
        if (tag == null)
            return null;
        return new RayTraceMemory(Vec3.createVectorHelper(tag.getDouble("posX"), tag.getDouble("posY"), tag.getDouble("posZ")),
                Vec3.createVectorHelper(tag.getDouble("hitX"), tag.getDouble("hitY"), tag.getDouble("hitZ")), tag
                .getInteger("hitRX"), tag.getInteger("hitRY"), tag.getInteger("hitRZ"), tag.getByte("hitSide"));
    }

    public RayTraceMemory(Vec3 pos, Vec3 hit, int hitRelCoordX, int hitRelCoordY, int hitRelCoordZ, int hitSide) {
        set(pos, hit, hitRelCoordX, hitRelCoordY, hitRelCoordZ);
        this.hitSide = hitSide;
    }

    public RayTraceMemory(Vec3 entityPos, Vec3 hitPos, int hitSide) {
        int hX = MathHelper.floor_double(hitPos.xCoord), eX = MathHelper.floor_double(entityPos.xCoord);
        int hY = MathHelper.floor_double(hitPos.yCoord), eY = MathHelper.floor_double(entityPos.yCoord);
        int hZ = MathHelper.floor_double(hitPos.zCoord), eZ = MathHelper.floor_double(entityPos.zCoord);
        set(entityPos.addVector(-eX, -eY, -eZ), hitPos.addVector(-hX, -hY, -hZ), hX - eX, hY - eY, hZ - eZ);
        this.hitSide = hitSide;
    }

    protected void set(Vec3 pos, Vec3 hit, int hitRelCoordX, int hitRelCoordY, int hitRelCoordZ) {
        this.pos = pos;
        this.hit = hit;
        this.hitRelCoordX = hitRelCoordX;
        this.hitRelCoordY = hitRelCoordY;
        this.hitRelCoordZ = hitRelCoordZ;

        this.look = pos.subtract(hit).addVector(hitRelCoordX, hitRelCoordY, hitRelCoordZ);
        this.normalizedLook = this.look.normalize();
        UtilCoordinate.Vec3RYP ryp = UtilCoordinate.Vec3RYP.fromXYZ(this.look);
        this.yaw = ryp.getYaw();
        this.pitch = ryp.getPitch();
    }


    public MovingObjectPosition rayTraceBlockFrom(World world, int x, int y, int z, double reach, boolean checkLiquid, boolean checkNeverCollisionBlock, boolean avoidNull) {
        return world.func_147447_a(this.pos.addVector(x, y, z), this.pos.addVector(x + this.normalizedLook.xCoord * reach, y + this.normalizedLook.yCoord * reach, z + this.normalizedLook.zCoord * reach), checkLiquid, checkNeverCollisionBlock, avoidNull);
    }


    public MovingObjectPosition rayTraceEntityFrom(World world, int x, int y, int z, double reach, Class entityClass, IEntitySelector selector) {
        return rayTraceEntity(world, this.pos.addVector(x, y, z), this.normalizedLook, reach, entityClass, selector);
    }

    public InventoryPlayer useItemFrom(ItemStack itemstack, World world, int x, int y, int z, MovingObjectPosition objectMouseOver, boolean sneak) {
        return useItemByFakePlayer(itemstack, world, this.pos.addVector(x, y, z), this.yaw, this.pitch, objectMouseOver, sneak);
    }

    public InventoryPlayer tryPlaceBlockAt(ItemStack itemstack, World world, int x, int y, int z, boolean sneak) {
        ForgeDirection d = ForgeDirection.getOrientation(this.hitSide).getOpposite();
        return interactWithBlockByFakePlayer(itemstack, world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, this.pos.addVector(x, y, z), this.yaw, this.pitch, this.hitSide, this.hit, sneak);
    }

    public InventoryPlayer interactWithBlockFrom(ItemStack itemstack, World world, int x, int y, int z, boolean sneak) {
        return interactWithBlockByFakePlayer(itemstack, world, x, y, z, this.pos.addVector(x, y, z), this.yaw, this.pitch, this.hitSide, this.hit, sneak);
    }

    private static RayTraceMemory[] standardMemories = new RayTraceMemory[6];

    public static RayTraceMemory getStandardMemory(ForgeDirection direction) {
        if (direction == null) return null;
        int i = direction.ordinal();
        if (i < 0 || i >= 6) return null;
        if (standardMemories[i] == null) {
            standardMemories[i] = new RayTraceMemory(Vec3.createVectorHelper(getBoundary(-direction.offsetX, 0.999D), getBoundary(-direction.offsetY, 0.999D), getBoundary(-direction.offsetZ, 0.999D)), Vec3.createVectorHelper(getBoundary(-direction.offsetX, 1.0D), getBoundary(-direction.offsetY, 1.0D), getBoundary(-direction.offsetZ, 1.0D)), direction.offsetX, direction.offsetY, direction.offsetZ, direction.getOpposite().ordinal());
        }
        return standardMemories[i];
    }

    private static double getBoundary(int i, double d) {
        return (i == 0) ? 0.5D : ((i == 1) ? d : (1.0D - d));
    }

    public static InventoryPlayer useItemByFakePlayer(ItemStack itemstack, World world, Vec3 pos, float yaw, float pitch, MovingObjectPosition objectMouseOver, boolean sneak) {
        EntityPlayer player = UtilPlayer.getFakePlayerWithItem(null, itemstack);
        player.setWorld(world);
        player.setPositionAndRotation(pos.xCoord, pos.yCoord, pos.zCoord, yaw, pitch);
        player.setSneaking(sneak);
        useItem(player, world, objectMouseOver);
        return player.inventory;
    }

    public static void useItem(EntityPlayer player, World world, MovingObjectPosition objectMouseOver) {
        if (objectMouseOver != null) {
            switch (objectMouseOver.typeOfHit) {

                case ENTITY:
                    if (interactWithEntity(player, objectMouseOver.entityHit))
                        return;
                    break;
                case BLOCK:
                    if (interactWithBlock(player, world, objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ, objectMouseOver.sideHit, objectMouseOver.hitVec)) {
                        return;
                    }
                    break;
            }

        }
        ItemStack itemstack1 = player.inventory.getCurrentItem();
        PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1, world);
        if (!event.isCanceled() && event.useItem != Event.Result.DENY && itemstack1 != null) {
            useItem(player, world, itemstack1);
        }
    }


    public static InventoryPlayer interactWithBlockByFakePlayer(ItemStack itemstack, World world, int x, int y, int z, Vec3 pos, float yaw, float pitch, int side, Vec3 hitVec, boolean sneak) {
        EntityPlayer player = UtilPlayer.getFakePlayerWithItem(null, itemstack);
        player.setWorld(world);
        player.setPositionAndRotation(pos.xCoord, pos.yCoord, pos.zCoord, yaw, pitch);
        player.setSneaking(sneak);
        interactWithBlock(player, world, x, y, z, side, hitVec);
        return player.inventory;
    }

    public static boolean interactWithBlock(EntityPlayer player, World world, int x, int y, int z, int side, Vec3 hitVec) {
        ItemStack itemstack = player.getHeldItem();
        boolean flag = false;


        int l = (itemstack != null) ? itemstack.stackSize : 0;

        PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, world);
        boolean result = (!event.isCanceled() && event.useItem != Event.Result.DENY);
        if (result && onPlayerRightClick(player, world, itemstack, x, y, z, side, hitVec)) {

            flag = true;
            player.swingItem();
        }
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerEntity = (EntityPlayerMP) player;
            if (playerEntity.playerNetServerHandler != null)
                playerEntity.playerNetServerHandler.sendPacket((Packet) new S23PacketBlockChange(x, y, z, world));
            if (!result)
                return false;
            if (side == 0) y--;
            if (side == 1) y++;
            if (side == 2) z--;
            if (side == 3) z++;
            if (side == 4) x--;
            if (side == 5) x++;
            if (playerEntity.playerNetServerHandler != null)
                playerEntity.playerNetServerHandler.sendPacket((Packet) new S23PacketBlockChange(x, y, z, world));
        }
        itemstack = player.getHeldItem();
        if (itemstack != null && itemstack.stackSize == 0) {

            player.inventory.mainInventory[player.inventory.currentItem] = null;
            itemstack = null;
        }

        if ((player instanceof EntityPlayerMP && itemstack == null) || itemstack.getMaxItemUseDuration() == 0) {

            EntityPlayerMP playerEntity = (EntityPlayerMP) player;
            playerEntity.isChangingQuantityOnly = true;
            playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = ItemStack.copyItemStack(playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem]);
            Slot slot = playerEntity.openContainer.getSlotFromInventory((IInventory) playerEntity.inventory, playerEntity.inventory.currentItem);
            playerEntity.openContainer.detectAndSendChanges();
            playerEntity.isChangingQuantityOnly = false;
        }

        return flag;
    }


    public static boolean onPlayerRightClick(EntityPlayer player, World world, ItemStack itemstack, int x, int y, int z, int side, Vec3 hitVec) {
        float f = (float) hitVec.xCoord - x;
        float f1 = (float) hitVec.yCoord - y;
        float f2 = (float) hitVec.zCoord - z;
        boolean flag = false;

        if (itemstack != null && itemstack
                .getItem() != null && itemstack
                .getItem().onItemUseFirst(itemstack, player, world, x, y, z, side, f, f1, f2)) {

            if (!world.isRemote && itemstack.stackSize <= 0)
                ForgeEventFactory.onPlayerDestroyItem(player, itemstack);
            return true;
        }

        if (!player.isSneaking() || player.getHeldItem() == null || player.getHeldItem().getItem().doesSneakBypassUse(world, x, y, z, player)) {
            flag = world.getBlock(x, y, z).onBlockActivated(world, x, y, z, player, side, f, f1, f2);
        }

        if (!flag && itemstack != null && itemstack.getItem() instanceof ItemBlock) {

            ItemBlock itemblock = (ItemBlock) itemstack.getItem();

            if (!canPlaceItemIntoWorld(itemblock, world, x, y, z, side, player, itemstack)) {
                return false;
            }
        }


        if (flag) {
            return true;
        }
        if (itemstack == null) {
            return false;
        }


        if (!itemstack.tryPlaceItemIntoWorld(player, world, x, y, z, side, f, f1, f2)) {
            return false;
        }
        if (itemstack.stackSize <= 0) {
            ForgeEventFactory.onPlayerDestroyItem(player, itemstack);
        }
        return true;
    }


    public static boolean canPlaceItemIntoWorld(ItemBlock itemBlock, World world, int x, int y, int z, int side, EntityPlayer player, ItemStack itemStack) {
        int[] coord = UtilBuilder.coordTransformOnPlaceBlock(world, x, y, z, side);
        x = coord[0];
        y = coord[1];
        z = coord[2];
        side = coord[3];
        return world.canPlaceEntityOnSide(itemBlock.field_150939_a, x, y, z, false, side, (Entity) null, itemStack);
    }

    public static boolean interactWithEntity(EntityPlayer player, Entity entity) {
        if (!player.worldObj.isRemote && player instanceof EntityPlayerMP)
            ((EntityPlayerMP) player).func_143004_u();
        return player.interactWith(entity);
    }

    public static boolean useItem(EntityPlayer player, World world, ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        int i = itemstack.stackSize;
        int j = itemstack.getItemDamage();
        ItemStack itemstack1 = itemstack.useItemRightClick(world, player);

        if (itemstack1 == itemstack && (itemstack1 == null || (itemstack1.stackSize == i && itemstack1.getMaxItemUseDuration() <= 0 && itemstack1.getItemDamage() == j))) {
            return false;
        }
        player.inventory.mainInventory[player.inventory.currentItem] = itemstack1;

        if (itemstack1.stackSize <= 0) {

            player.inventory.mainInventory[player.inventory.currentItem] = null;
            ForgeEventFactory.onPlayerDestroyItem(player, itemstack1);
        }
        if (player instanceof EntityPlayerMP && !player.isUsingItem()) {
            if (((EntityPlayerMP) player).playerNetServerHandler != null)
                ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
        }
        return true;
    }

    public static MovingObjectPosition rayTraceEntity(World world, Vec3 pos, Vec3 look, double reach, Class entityClass, IEntitySelector selector) {
        Vec3 normalizedLook = look.normalize();
        Vec3 end = pos.addVector(normalizedLook.xCoord * reach, normalizedLook.yCoord * reach, normalizedLook.zCoord * reach);
        Vec3 hit = null;
        Entity pointedEntity = null;
        float f1 = 1.0F;
        List<Entity> list = world.selectEntitiesWithinAABB(entityClass, AxisAlignedBB.getBoundingBox(pos.xCoord, pos.yCoord, pos.zCoord, pos.xCoord, pos.yCoord, pos.zCoord).addCoord(normalizedLook.xCoord * reach, normalizedLook.yCoord * reach, normalizedLook.zCoord * reach).expand(f1, f1, f1), selector);
        double minDistance = reach;

        for (int i = 0; i < list.size(); i++) {

            Entity entity = list.get(i);

            if (entity.canBeCollidedWith()) {

                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(pos, end);

                if (axisalignedbb.isVecInside(pos)) {

                    if (0.0D < minDistance || minDistance == 0.0D) {
                        pointedEntity = entity;
                        hit = (movingobjectposition == null) ? pos : movingobjectposition.hitVec;
                        minDistance = 0.0D;
                    }

                } else if (movingobjectposition != null) {

                    double distance = pos.distanceTo(movingobjectposition.hitVec);

                    if (distance < minDistance || minDistance == 0.0D) {


                        pointedEntity = entity;
                        hit = movingobjectposition.hitVec;
                        minDistance = distance;
                    }
                }
            }
        }


        if (pointedEntity != null && minDistance < reach) {
            return new MovingObjectPosition(pointedEntity, hit);
        }
        return null;
    }
}
