package project.daprian.utility;

import net.minecraft.client.Minecraft;

public class MovementUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static float[] incrementMoveDirection(float forward, float strafe) {
        if(forward != 0 || strafe != 0) {
            float value = forward != 0 ? Math.abs(forward) : Math.abs(strafe);

            if(forward > 0) {
                if(strafe > 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = -value;
                } else if(strafe < 0) {
                    forward = 0;
                }
            } else if(forward == 0) {
                if(strafe > 0) {
                    forward = value;
                } else {
                    forward = -value;
                }
            } else {
                if(strafe < 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = value;
                } else if(strafe > 0) {
                    forward = 0;
                }
            }
        }

        return new float[] {forward, strafe};
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }

    public static double getSpeed() {
        return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static void Strafe(double speed) {
        if(isMoving()){
            double yaw = Math.toRadians(getPlayerDirection());
            mc.thePlayer.motionZ = Math.cos(yaw) * speed;
            mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        }
    }

    public static void Strafe(){
        if(isMoving()){
            double yaw = Math.toRadians(getPlayerDirection());
            mc.thePlayer.motionZ = Math.cos(yaw) * getSpeed();
            mc.thePlayer.motionX = -Math.sin(yaw) * getSpeed();
        }
    }

    public static float getPlayerDirection() {
        float direction = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward > 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 45;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 45;
            }
        } else if (mc.thePlayer.moveForward < 0) {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 135;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 135;
            } else {
                direction -= 180;
            }
        } else {
            if (mc.thePlayer.moveStrafing > 0) {
                direction -= 90;
            } else if (mc.thePlayer.moveStrafing < 0) {
                direction += 90;
            }
        }

        return direction;
    }
}