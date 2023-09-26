package project.daprian.client.modules;

import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;

@Module.Info(name = "FastPlace",category = Category.Player, bindable = true)
public class FastPlace extends Module {
    public static final Setting<Integer> delay = Setting.create(setting -> setting.setValues("Delay",0,0,4,1));
    public static final Setting<Modes> modes = Setting.create(setting -> setting.setValues("Modes",Modes.Block));
    public enum Modes{Block,All}
}
