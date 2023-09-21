package project.daprian.utility;

import java.awt.*;

public class ColorUtils {
    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? interpolateColorHue(start, end, angle / 360f) : interpolateColorC(start, end, angle / 360f);
    }

    private static Color interpolateColorC(Color start, Color end, float v) {
        int red = (int) (start.getRed() + (end.getRed() - start.getRed()) * v);
        int green = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * v);
        int blue = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * v);
        return new Color(red, green, blue);
    }

    private static Color interpolateColorHue(Color start, Color end, float v) {
        float[] startHSB = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);
        float hue = startHSB[0] + (endHSB[0] - startHSB[0]) * v;
        float saturation = startHSB[1] + (endHSB[1] - startHSB[1]) * v;
        float brightness = startHSB[2] + (endHSB[2] - startHSB[2]) * v;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public static Color modifyAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}