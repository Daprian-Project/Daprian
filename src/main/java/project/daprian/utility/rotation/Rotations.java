package project.daprian.utility.rotation;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import project.daprian.client.events.MotionEvent;

// Credits to a open source base client
@Getter
public class Rotations {
    public static final Rotations INVALID = new Rotations(Float.NaN, Float.NaN);

    private float yaw, pitch;

    public Rotations(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isValid() {
        return !Float.isNaN(yaw) && !Float.isNaN(pitch);
    }

    public void invalidate() {
        yaw = Float.NaN;
        pitch = Float.NaN;
    }

    public static float getGCD() {
        return (float) (Math.pow(Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6 + 0.2, 3) * 1.2);
    }

    public static void rotate(MotionEvent event, Rotations rotations) {
        event.setYaw(rotations.getYaw());
        event.setPitch(rotations.getPitch());

        Minecraft.getMinecraft().thePlayer.rotationPitchHead = rotations.getPitch();
        Minecraft.getMinecraft().thePlayer.rotationYawHead = rotations.getYaw();
        Minecraft.getMinecraft().thePlayer.renderYawOffset = rotations.getYaw();
    }
}