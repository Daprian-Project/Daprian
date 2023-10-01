package project.daprian.utility;

import org.lwjgl.opengl.GL11;

import java.awt.*;

import static project.daprian.utility.GLUtil.*;

public class PolygonUtil {
    public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        if (!filled) GL11.glLineWidth(1);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
        {
            for (double i = 0; i <= amountOfSides; i++) {
                double angle = i * (Math.PI * 2) / amountOfSides;
                vertex(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
            }
        }
        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();
    }

    public static void polygonCentered(double x, double y, double sideLength, int amountOfSides, boolean filled, Color color) {
        x -= sideLength / 2;
        y -= sideLength / 2;
        polygon(x, y, sideLength, amountOfSides, filled, color);
    }

    public static void circle(double x, double y, double radius, boolean filled, Color color) {
        polygon(x, y, radius, 360, filled, color);
    }

    public static void circleCentered(double x, double y, double radius, boolean filled, Color color) {
        x -= radius / 2;
        y -= radius / 2;
        polygon(x, y, radius, 360, filled, color);
    }

    public static void triangle(double x, double y, double sideLength, boolean filled, Color color) {
        polygon(x, y, sideLength, 3, filled, color);
    }

    public static void triangleCentered(double x, double y, double sideLength, boolean filled, Color color) {
        x -= sideLength / 2;
        y -= sideLength / 2;
        polygon(x, y, sideLength, 3, filled, color);
    }

    public static void line(double firstX, double firstY, double secondX, double secondY, double lineWidth, Color color) {
        start();
        if (color != null)
            color(color);
        lineWidth(lineWidth <= 1 ? 1 : lineWidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        begin(GL11.GL_LINES);

        vertex(firstX, firstY);
        vertex(secondX, secondY);

        end();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();
    }
}