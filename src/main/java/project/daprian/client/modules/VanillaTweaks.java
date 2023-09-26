package project.daprian.client.modules;

import lombok.Getter;
import org.lwjgl.input.Keyboard;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;

@Getter
@Module.Info(name = "VanillaTweaks", description = "Basic Vanilla changes.", category = Category.Player, bindable = false)
public class VanillaTweaks extends Module {
    private final Setting<Double> entityCullingDistance = Setting.create(setting -> setting.setValues("Culling Chunks", 20D, 1D, 64D, 1D));
    private final Setting<Double> horizontalBob = Setting.create(setting -> setting.setValues("Horizontal Bob", 1D, 0D, 5D, .10));
    private final Setting<Double> verticalBob = Setting.create(setting -> setting.setValues("Vertical Bob", 1D, 0D, 5D, .10));
    private final Setting<Boolean> hurtCam = Setting.create(setting -> setting.setValues("Hurt Cam", true));
    private final Setting<Boolean> camBobbing = Setting.create(setting -> setting.setValues("Camera Bobbing", true));

    @Override
    protected void onDisable() {
        Toggle();
    }
}