package project.daprian.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderUtil  {

    private static final FloatBuffer windowPosition = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);

    public static void drawRect(double x, double y, double width, double height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawRect(double x, double y, double width, double height, Color color) {
        drawRect(x, y, width, height, color.getRGB());
    }

    public static void drawOutline(int x, int y, int x1, int y1,int lineWidth, int color) {
        Gui.drawRect(x, y, x + lineWidth, y1, color);
        Gui.drawRect(x, y, x1, y + lineWidth, color);
        Gui.drawRect(x1 - lineWidth, y, x1, y1, color);
        Gui.drawRect(x, y1 - lineWidth, x1, y1, color);
    }

    public static float[] calculateCoords(EntityLivingBase target) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = sr.getScaleFactor();
        double[][] vectors = getDoubles(target);

        Vector3f projection;
        Vector4f position = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0F, -1.0F);

        for (double[] vec : vectors) {
            projection = project2D((float)vec[0], (float)vec[1], (float)vec[2], scaleFactor);

            if (projection != null && projection.z >= 0.0F && projection.z < 1.0F) {
                position.x = Math.min(position.x, projection.x);
                position.y = Math.min(position.y, projection.y);
                position.z = Math.max(position.z, projection.x);
                position.w = Math.max(position.w, projection.y);
            }
        }

        return new float[]{position.x, position.z, position.y, position.w};
    }

    public static double[][] getDoubles(EntityLivingBase target) {
        AxisAlignedBB bb = getAxisAlignedBB(target);

        return new double[][]{
                {bb.minX, bb.minY, bb.minZ}, {bb.minX, bb.maxY, bb.minZ},
                {bb.minX, bb.maxY, bb.maxZ}, {bb.minX, bb.minY, bb.maxZ},
                {bb.maxX, bb.minY, bb.minZ}, {bb.maxX, bb.maxY, bb.minZ},
                {bb.maxX, bb.maxY, bb.maxZ}, {bb.maxX, bb.minY, bb.maxZ}
        };
    }

    public static AxisAlignedBB getAxisAlignedBB(EntityLivingBase target) {
        Vec3 vec3 = getVec3(target);

        float posX = (float)(vec3.x - Minecraft.getMinecraft().getRenderManager().viewerPosX);
        float posY = (float)(vec3.y - Minecraft.getMinecraft().getRenderManager().viewerPosY);
        float posZ = (float)(vec3.z - Minecraft.getMinecraft().getRenderManager().viewerPosZ);

        double halfWidth = target.width / 2.0D + 0.18F;

        return new AxisAlignedBB(
                posX - halfWidth, posY, posZ - halfWidth,
                posX + halfWidth, posY + target.height + 0.18D, posZ + halfWidth
        );
    }

    public static Vec3 getVec3(final EntityLivingBase var0) {
        final float timer = Minecraft.getMinecraft().timer.renderPartialTicks;
        final double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * timer;
        final double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * timer;
        final double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * timer;
        return new Vec3(x, y, z);
    }

    public static Vector3f project2D(float x, float y, float z, int scaleFactor) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject(x, y, z, modelMatrix, projectionMatrix, viewport, windowPosition)) {
            return new Vector3f(windowPosition.get(0) / scaleFactor,
                    (Minecraft.getMinecraft().displayHeight - windowPosition.get(1)) / scaleFactor, windowPosition.get(2));
        }

        return null;
    }

    public static void drawFace(AbstractClientPlayer e, float x, float y, float width, float height) {
        final ResourceLocation skin = e.getLocationSkin();
        GL11.glColor4f(1.0f, 1, 1, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 8.0f, 8.0f, 8, 8, (int) width, (int) height, 64.0f, 64.0f);
    }
}