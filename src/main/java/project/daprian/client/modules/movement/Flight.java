package project.daprian.client.modules.movement;

import io.github.nevalackin.radbus.Listen;
import project.daprian.client.Main;
import project.daprian.client.events.MotionEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.MovementUtil;

@Module.Info(name = "Flight", category = Category.Movement, bindable = true)
public class Flight extends Module {

    private final Setting<Double> speed = Setting.create(setting -> setting.setValues("Speed", 5D, 1D, 10D, .1));
    private final Setting<Boolean> autoJump = Setting.create(setting -> setting.setValues("Auto Jump", true));

    @Override
    protected void onEnable() {
        Check(Main.getInstance().getModuleManager().getModule(Speed.class));
        if (mc.thePlayer.onGround && autoJump.getValue()) mc.thePlayer.jump();
    }

    @Listen
    public void onMotion(MotionEvent event) {
        setSuffix(() -> "Basic");
        mc.thePlayer.motionY = 0;

        if (MovementUtil.isMoving()) {
            MovementUtil.Strafe(speed.getValue() / 10);
        }
    }
}