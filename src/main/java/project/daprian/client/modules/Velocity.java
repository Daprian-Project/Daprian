package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
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

    @Listen
    public void onPacket(PacketEvent event) {
        setSuffix(() -> String.format("%s (%s)", mode.getValue().name(), mc.thePlayer.hurtTime));

        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

            if (packet.getEntityID() != mc.thePlayer.getEntityId())
                return;

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

    private enum Mode {
        Simple,
        Random
    }
}