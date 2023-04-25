package mods.clayium.machine.Interface;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @see net.minecraft.client.renderer.entity.RenderItemFrame;
 */
@SideOnly(Side.CLIENT)
public class TESRInterface extends TileEntitySpecialRenderer<TileEntityGeneric> {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final BlockRendererDispatcher blockRenderer = mc.getBlockRendererDispatcher();
    private final RenderItem itemRenderer = mc.getRenderItem();
    private final TileEntityRendererDispatcher tileRenderer = TileEntityRendererDispatcher.instance;

    @Override
    public void render(TileEntityGeneric tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (tile == null || !(tile instanceof ISynchronizedInterface) || !((ISynchronizedInterface) tile).isSynced()) {
            return;
        }

        IInterfaceCaptive core = ((ISynchronizedInterface) tile).getCore();
        if (core == IInterfaceCaptive.NONE) return;

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.enableBlend();

        drawRemoteCore(x, y, z, core, tile, partialTicks);

        highlightCore(tile, core, partialTicks);

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.enableBlend();
    }

    /**
     * @see net.minecraft.client.renderer.EntityRenderer#drawNameplate(FontRenderer, String, float, float, float, int, float, float, boolean, boolean)
     */
    public static void drawString(FontRenderer fontrenderer, String s) {
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, -1.0F, 1.0F);
        int i = fontrenderer.getStringWidth(s) / 2;
        GlStateManager.enablePolygonOffset();
        GL11.glPolygonOffset(0.1F, 1.0F);
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(-i - 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        bufferBuilder.pos(-i - 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        bufferBuilder.pos(i + 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        bufferBuilder.pos(i + 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disablePolygonOffset();
        fontrenderer.drawString(s, -i, 0, -1);
        GL11.glPopMatrix();
    }

    private void highlightCore(TileEntityGeneric in, IInterfaceCaptive core, float ticktime) {
        if (core.getWorld().provider.getDimension() != in.getWorld().provider.getDimension()) {
            return;
        }

        float cr = (float)(Math.sin((double)(ticktime * 0.1F) + 0.0) + 1.0) * 0.5F;
        float cg = (float)(Math.sin((double)(ticktime * 0.1F) + 2.0943951023931953) + 1.0) * 0.5F;
        float cb = (float)(Math.sin((double)(ticktime * 0.1F) + 4.1887902047863905) + 1.0) * 0.5F;
        float cf = (float)(Math.sin((double)(ticktime * 0.12F) + 0.0) + 1.0) * 0.15F + 0.2F;

        IBlockState cstate = core.getWorld().getBlockState(core.getPos());
        AxisAlignedBB caabb = cstate.getBoundingBox(core.getWorld(), core.getPos());
        Color highlight = new Color(cr, cg, cb, cf);

        // Note: Block Appearance is 2, border is false.
        UtilRender.renderOffsetAABB(caabb, new Vec3d(core.getPos()), highlight);

        UtilRender.renderLine(new Vec3d(core.getPos()), new Vec3d(in.getPos()), highlight);
    }

    private void drawRemoteCoreBlock(IInterfaceCaptive core, ItemStack itemstack, float ticktime) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.05F, 0.0F);
        GL11.glRotatef(ticktime * 2.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        GL11.glEnable(32826);
        GL11.glAlphaFunc(516, 0.0F);
        GL11.glEnable(3042);

//        blockRenderer.renderBlockBrightness(core.getWorld().getBlockState(core.getPos()), 1.0f); // face only
//        tileRenderer.render((TileEntity) core, x, y, z, ticktime); // render hull not work :(
        itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);

        GL11.glPopMatrix();
    }

    private void drawRemoteCoreInfo(IInterfaceCaptive core, ItemStack itemstack, Entity viewEntity) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.4F, 0.0F);
        GL11.glRotatef(180.0F - viewEntity.rotationYaw, 0.0F, 1.0F, 0.0F);

        {
            GL11.glPushMatrix();
            GL11.glScalef(0.01F, 0.01F, 0.01F);
            GlStateManager.disableDepth();
            drawString(getFontRenderer(), itemstack.getDisplayName());
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }

        GL11.glTranslatef(0.0F, -0.1F, 0.0F);

        {
            GL11.glPushMatrix();
            GL11.glScalef(0.005F, 0.005F, 0.005F);
            GlStateManager.disableDepth();
            drawString(getFontRenderer(), "[" + core.getPos().getX() + "," + core.getPos().getY() + "," + core.getPos().getZ() + "]" + ";" + core.getWorld().provider.getDimensionType().getName());
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    private void drawRemoteCore(double x, double y, double z, IInterfaceCaptive core, TileEntityGeneric in, float partialTicks) {
        Entity viewEntity = mc.getRenderViewEntity();
        RayTraceResult mop = viewEntity == null ? null : viewEntity.rayTrace(9999.0d, 0.0f);

        if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK || !mop.getBlockPos().equals(in.getPos())) {
            return;
        }

        float ticktime = (float) in.getWorld().getWorldTime() + partialTicks;
        ItemStack itemstack = UtilBuilder.getItemBlock(core.getWorld(), core.getPos());

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        float f = 0.8F;
        switch (mop.sideHit) {
            case UP:
                GL11.glTranslatef(0.0F, f, 0.0F);
                break;
            case DOWN:
                GL11.glTranslatef(0.0F, -f, 0.0F);
                break;
            case NORTH:
                GL11.glTranslatef(0.0F, 0.0F, -f);
                break;
            case SOUTH:
                GL11.glTranslatef(0.0F, 0.0F, f);
                break;
            case WEST:
                GL11.glTranslatef(-f, 0.0F, 0.0F);
                break;
            case EAST:
                GL11.glTranslatef(f, 0.0F, 0.0F);
        }

        drawRemoteCoreBlock(core, itemstack, ticktime);

        GlStateManager.disableLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);

        drawRemoteCoreInfo(core, itemstack, viewEntity);

        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
}
