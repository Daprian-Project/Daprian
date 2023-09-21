package project.daprian.utility.rotation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

import java.util.Random;

public class Angle {

    static Minecraft mc = Minecraft.getMinecraft();
    static Random random = new Random();

    public static Bone getRandomBone() {
        return Bone.values()[random.nextInt(Bone.values().length)];
    }

    public static Bone getRandomOneBetween(Bone... bones) {
        return bones[random.nextInt(bones.length)];
    }

    public static float smooth(float from, float to, float speed) {
        float difference = to - from;

        if (difference > speed) {
            difference = speed;
        } else if (difference < -speed) {
            difference = -speed;
        }

        return from + difference;
    }

    public static Rotations calcRotationToEntity(Entity e) {
        double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
                deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
                distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
        float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        double degrees = Math.toDegrees(Math.atan(deltaZ / deltaX));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + degrees);
        } else if (deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + degrees);
        }

        return new Rotations(yaw, pitch);
    }

    public static Vec3 diffIn(Vec3 from, Vec3 to) {
        return new Vec3(from.x - to.x, from.y - to.y, from.z - to.z);
    }

    private static Vec3 getEyes(Entity entity, Bone bone) {
        double y = interpolate(entity.posY, entity.lastTickPosY);
        double x = interpolate(entity.posX, entity.lastTickPosX);
        double z = interpolate(entity.posZ, entity.lastTickPosZ);

        switch (bone) {
            case HEAD:
                y += entity.getEyeHeight();
                break;

            case CHEST:
                y += (entity.getEyeHeight() / 2.0f) + 0.35;
                break;

            case LEGS:
                y += entity.getEyeHeight() / 2.0f;
                break;

            case FEET:
                y += 0.5;
                break;

            case ARM1:
                y += entity.getEyeHeight() - 0.35;
                x -= 0.25;
                z -= 0.25;
                break;
            case ARM2:
                y += entity.getEyeHeight() - 0.35;
                x += 0.25;
                z += 0.25;
                break;
        }

        return new Vec3(x, y, z);
    }

    public static double interpolate(double start, double end) {
        float partialRenderTicks = mc.timer.renderPartialTicks;
        if (partialRenderTicks == 1.0f) {
            return start;
        }

        return end + (start - end) * partialRenderTicks;
    }
}