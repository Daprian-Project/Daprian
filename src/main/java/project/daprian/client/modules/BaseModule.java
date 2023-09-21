package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import org.lwjgl.input.Keyboard;
import project.daprian.client.events.TickEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;

@Module.Info(name = "Test", bind = Keyboard.KEY_F, description = "I'm testing man", category = Category.Misc)
public class BaseModule extends Module {

    private Setting<Double> doubleSetting = Setting.create(setting -> setting.setValues("Double Setting", 10D, 0D, 10D, 1D));
    private Setting<Boolean> booleanSetting = Setting.create(setting -> setting.setValues("Boolean Setting", true));
    private Setting<Mode> modeSetting = Setting.create(setting -> setting.setValues("Mode Setting", Mode.Mode1));
    private Setting<Boolean> secondBooleanSetting = Setting.create(setting -> {
        setting.setVisible(() -> booleanSetting.getValue());
        setting.setValues("Second Bool Setting", true);
    });

    @Listen
    public void onTick(TickEvent e) {
        System.out.printf("%s / %s / %s%n", doubleSetting.getValue(), booleanSetting.getValue(), modeSetting.getValue());
    }

    private enum Mode { Mode1, Mode2, Mode3 }

}