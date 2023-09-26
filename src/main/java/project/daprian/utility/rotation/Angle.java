package project.daprian.utility.rotation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import project.daprian.client.events.MotionEvent;

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
    public static float[] getRotationsToPosition(double x, double y, double z) {
        double deltaX = x - mc.thePlayer.posX;
        double deltaY = y - mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
        double deltaZ = z - mc.thePlayer.posZ;

        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float) Math.toDegrees(-Math.atan2(deltaX, deltaZ));
        float pitch = (float) Math.toDegrees(-Math.atan2(deltaY, horizontalDistance));

        return new float[] {yaw, pitch};
    }


    public static float[] getRotationsToEntity(EntityLivingBase entity, boolean usePartialTicks) {
        float partialTicks = mc.timer.renderPartialTicks;

        double entityX = usePartialTicks ? entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks : entity.posX;
        double entityY = usePartialTicks ? entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks : entity.posY;
        double entityZ = usePartialTicks ? entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks : entity.posZ;

        double yDiff = mc.thePlayer.posY - entityY;

        double finalEntityY = yDiff >= 0 ? entityY + entity.getEyeHeight() : -yDiff < mc.thePlayer.getEyeHeight() ? mc.thePlayer.posY + mc.thePlayer.getEyeHeight() : entityY;

        return getRotationsToPosition(entityX, finalEntityY, entityZ);
    }


    public static Rotations smoothRotations(EntityLivingBase currentTarget, Rotations currentRotations){
        currentRotations = new Rotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        FixedRotations fixedRotations = new FixedRotations(currentRotations);
        fixedRotations.updateRotations(Angle.calcRotationToEntity(currentTarget));

        Rotations finalRotations = fixedRotations.getCurrentRotations();

        currentRotations.setYaw(finalRotations.getYaw());
        currentRotations.setPitch(finalRotations.getPitch());
        return currentRotations;
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