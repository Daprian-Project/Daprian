package project.daprian.client.modules.combat;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.Vec3;
import project.daprian.client.Main;
import project.daprian.client.events.MotionEvent;
import project.daprian.client.events.PacketEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.MathUtil;
import project.daprian.utility.MovementUtil;

@Module.Info(name = "Velocity", category = Category.Combat, bindable = false)
public class Velocity extends Module {

    private final Setting<Mode> mode = Setting.create(setting -> setting.setValues("Mode", Mode.Simple));
    private final Setting<Integer> horizontal = Setting.create(setting -> setting.setValues("Horizontal", 0, 0, 100, 1));
    private final Setting<Integer> vertical = Setting.create(setting -> setting.setValues("Vertical", 0, 0, 100, 1));
    private final Setting<Integer> randomHorizontal = Setting.create(setting -> {
        setting.setValues("Horizontal", 0, 0, 100, 1);
        setting.setVisible(() -> mode.getValue().equals(Mode.Random));
    });
    private final Setting<Integer> randomVertical = Setting.create(setting -> {
        setting.setValues("Vertical", 0, 0, 100, 1);
        setting.setVisible(() -> mode.getValue().equals(Mode.Random));
    });

    private Vec3 lastPosition;
    private boolean velocityTaken;
    private int velocityTicks;

    @Listen
    public void onPacket(PacketEvent event) {
        setSuffix(() -> String.format("%s", mode.getValue().name()));

        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

            if (packet.getEntityID() != mc.thePlayer.getEntityId())
                return;

            velocityTaken = true;

            if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
                event.Cancel();
            } else {
                switch (mode.getValue()) {
                    case Simple:
                        packet.setMotionX(packet.getMotionX() * horizontal.getValue() / 100);
                        packet.setMotionY(packet.getMotionY() * vertical.getValue() / 100);
                        packet.setMotionZ(packet.getMotionZ() * horizontal.getValue() / 100);
                        break;
                    case Random:
                        int randomXZ = MathUtil.randomBetween(horizontal.getValue(), randomHorizontal.getValue());
                        int randomY = MathUtil.randomBetween(vertical.getValue(), randomVertical.getValue());
                        packet.setMotionX(packet.getMotionX() * randomXZ / 100);
                        packet.setMotionY(packet.getMotionY() * randomY / 100);
                        packet.setMotionZ(packet.getMotionZ() * randomXZ / 100);
                        break;
                }
            }
        }
    }

    @Listen
    public void onMotion(MotionEvent event) {
        if (velocityTaken) {
            velocityTicks++;

            if (velocityTicks >= 30) {
                velocityTaken = false;
                velocityTicks = 0;
            }
        }

        if (mode.getValue().equals(Mode.Back)) {
            if (!velocityTaken) {
                lastPosition = mc.thePlayer.getPosition().toVector();
                return;
            }

            double x = lastPosition.x;
            double y = lastPosition.y;
            double z = lastPosition.z;

            if (MathUtil.endsWithDigit(velocityTicks, 5) && velocityTicks < 25) {
                System.out.println("Sent");
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            }

            if (velocityTicks != 29) return;

            event.setX(x);
            event.setY(y);
            event.setZ(z);
            mc.thePlayer.setPositionAndUpdate(x, y, z);
        }
    }

    private enum Mode {
        Simple,
        Random,
        Back
    }
}