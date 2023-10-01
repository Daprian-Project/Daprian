package project.daprian.utility;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GLUtil {
    public static void push() {
        GL11.glPushMatrix();
    }

    public static void enable(int glTarget) {
        GL11.glEnable(glTarget);
    }

    public static void disable(int glTarget) {
        GL11.glDisable(glTarget);
    }

    public static void start() {
        enable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        disable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_CULL_FACE);
        GlStateManager.disableAlpha();
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        enable(GL11.GL_CULL_FACE);
        enable(GL11.GL_TEXTURE_2D);
        disable(GL11.GL_BLEND);
        color(Color.white);
    }

    public static void begin(int glMode) {
        GL11.glBegin(glMode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void vertex(double x, double y) {
        GL11.glVertex2d(x, y);
    }

    public static void translate(double x, double y) {
        GL11.glTranslated(x, y, 0);
    }

    public static void scale(double x, double y) {
        GL11.glScaled(x, y, 1);
    }

    public static void rotate(double x, double y, double z, double angle) {
        GL11.glRotated(angle, x, y, z);
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public static void lineWidth(double width) {
        GL11.glLineWidth((float)width);
    }
}