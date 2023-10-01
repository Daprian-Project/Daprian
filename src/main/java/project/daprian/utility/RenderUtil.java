package project.daprian.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
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
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil  {

    private static final FloatBuffer windowPos = BufferUtils.createFloatBuffer(3);
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

    public static void pushScissor(double x, double y, double width, double height) {
        width = MathHelper.clamp_double(width, 0, width);
        height = MathHelper.clamp_double(height, 0, height);

        glPushAttrib(GL_SCISSOR_BIT);
        {
            scissorRect(x, y, width, height);
            glEnable(GL_SCISSOR_TEST);
        }
    }

    public static void scissorRect(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static void popScissor() {
        glDisable(GL_SCISSOR_TEST);
        glPopAttrib();
    }

    public static void drawESP(AxisAlignedBB axisAlignedBB, Color color, float lineWidth) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        // use an extreme high or low value that will probably never be valid
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        // clear the last stored window position
        windowPos.clear();

        // loop thought all min max combinations
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                for (int y = 0; y < 2; y++) {
                    // use gluProject to convert the 3D object coords to 2D window coords
                    if (GLU.gluProject((float) (x == 1 ? axisAlignedBB.minX : axisAlignedBB.maxX), (float) (y == 1 ? axisAlignedBB.minY : axisAlignedBB.maxY), (float) (z == 1 ? axisAlignedBB.minZ : axisAlignedBB.maxZ), ActiveRenderInfo.getMODELVIEW(), ActiveRenderInfo.getPROJECTION(), ActiveRenderInfo.getVIEWPORT(), windowPos)) {
                        if (windowPos.get(2) > 1) {
                            continue;
                        }

                        // scale the output coords with our current scale factor
                        double screenX = windowPos.get(0) / scaledResolution.getScaleFactor();
                        double screenY = windowPos.get(1) / scaledResolution.getScaleFactor();

                        // find the max and min coords
                        minX = Math.min(screenX, minX);
                        minY = Math.min(screenY, minY);
                        maxX = Math.max(screenX, maxX);
                        maxY = Math.max(screenY, maxY);
                    }
                }
            }
        }

        // if minX isn't Double.MAX_VALUE then the other values should also be valid
        if (minX != Double.MAX_VALUE) {
            if (minX > 0 || minY > 0 || maxX <= Minecraft.getMinecraft().displayWidth || maxX <= Minecraft.getMinecraft().displayHeight) {
                // invert the y coords because Minecraft has inverted them as well
                minY = scaledResolution.getScaledHeight() - minY;
                maxY = scaledResolution.getScaledHeight() - maxY;

                GL11.glPushMatrix();
                // enable alpha blending
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                // disable texture 2d since we don't want to draw any textures
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glColor4d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d, color.getAlpha() / 255d);
                // set the outline width to 2
                GL11.glLineWidth(lineWidth);
                // render the outline line loop connects the last point automatically
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex2d(minX, minY);
                GL11.glVertex2d(minX, maxY);
                GL11.glVertex2d(maxX, maxY);
                GL11.glVertex2d(maxX, minY);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    public static boolean containsIgnoreCase(String mainString, String subString) {
        if (mainString == null || subString == null) {
            return false;
        }

        int length = subString.length();
        if (length == 0) {
            return true;
        }

        int limit = mainString.length() - length;
        for (int i = 0; i <= limit; i++) {
            if (mainString.regionMatches(true, i, subString, 0, length)) {
                return true;
            }
        }

        return false;
    }
}