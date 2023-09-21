package project.daprian.client.commands;

import com.sun.jna.IntegerType;
import project.daprian.client.Main;
import project.daprian.systems.command.Command;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;

@Command.Info(name = "SetValue", usage = ".setvalue <module> <name> <value>", alias = {"sv"}, description = "Sets a fucking value")
public class SetValue extends Command {
    @Override
    public void Execute(String[] args) {
        if (args.length < 4) {
            Main.getInstance().chat(getUsage(), false);
            return;
        }

        for (Module module : Main.getInstance().getModuleManager().getModuleHashMap().values()) {
            if (module.getName().equalsIgnoreCase(args[1])) {
                for (Setting setting : module.getSettings()) {
                    if (setting.getName().equalsIgnoreCase(args[2])) {
                        if (setting.getValue() instanceof Number) {
                            if (setting.getValue() instanceof Integer) {
                                setSettingValue(setting, Integer.valueOf(args[3]));
                            } else if (setting.getValue() instanceof Double) {
                                setSettingValue(setting, Double.valueOf(args[3]));
                            } else if (setting.getValue() instanceof Float) {
                                setSettingValue(setting, Float.valueOf(args[3]));
                            }
                        } else if (setting.getValue() instanceof Boolean) {
                            if (args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false")) {
                                setSettingValue(setting, Boolean.valueOf(args[3]));
                            }
                        }
                    }
                }
            }
        }
    }

    private <T> void setSettingValue(Setting<T> setting, T value) {
        setting.setValue(value);
        Main.getInstance().chat(String.format("Successfully set %s to %s!", setting.getName(), value), false);
    }
}