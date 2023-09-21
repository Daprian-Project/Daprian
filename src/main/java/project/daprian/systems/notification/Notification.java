package project.daprian.systems.notification;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import project.daprian.utility.RenderUtil;
import project.daprian.utility.TimeUtil;

import java.awt.*;
import java.time.Duration;

public class Notification {

    String message;
    @Getter
    long time;
    @Getter
    Type type;
    @Getter
    Position position;
    @Getter
    float offset;

    Color typeColor;
    TimeUtil displayTime = new TimeUtil();
    public boolean hasReached = false;

    public Notification(String message, long time, Type type, Position position) {
        this.message = message;
        this.time = time;
        this.type = type;
        this.position = position;
        displayTime.reset();
    }

    public void render() {
        update();

        FontRenderer msg = Minecraft.getMinecraft().fontRendererObj;

        float x = 0;
        float y = 0;

        switch (position) {
            case TOP:
                x = (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2f) - msg.getStringWidth(message) / 2f - 7.5f;
                y = 8;
                break;
            case BOTTOM:
                x = (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) - (15 + msg.getStringWidth(message)) - 8;
                y = (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) - 34;
                break;
            case CROSSHAIR:
                x = (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2f) - msg.getStringWidth(message) / 2f - 7.5f;
                y = (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2f) + 8;
                break;
        }

        switch (type) {
            case SUCCESS:
                typeColor = new Color(119, 221, 119);
                break;
            case ERROR:
                typeColor = new Color(221, 119, 119);
                break;
            case INFO:
                typeColor = new Color(119, 119, 221);
                break;
            case WARNING:
                typeColor = new Color(221, 221, 119);
                break;
        }

        float height = 12;

        RenderUtil.drawRect(x, y - 2 + offset, 15 + msg.getStringWidth(message), height, new Color(30, 30, 30, 190));
        RenderUtil.drawRect(x + 2, y + offset, 8, 8, typeColor);

        msg.drawStringWithShadow(message, x + 13, y - 1 + (height / 2f - msg.FONT_HEIGHT / 2f) + offset, new Color(255, 255, 255, 255).getRGB());
    }

    private void update() {
        hasReached = displayTime.hasReached(Duration.ofSeconds(time));
    }

    public void setOffset(float addValue) {
        offset = addValue;
    }

    public enum Type {
        INFO,
        WARNING,
        SUCCESS,
        ERROR
    }

    public enum Position {
        TOP,
        CROSSHAIR,
        BOTTOM
    }
}