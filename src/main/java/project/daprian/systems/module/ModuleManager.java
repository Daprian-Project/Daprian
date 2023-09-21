package project.daprian.systems.module;

import lombok.Getter;
import me.daprian.tasks.Tasked;
import project.daprian.client.Main;
import project.daprian.client.modules.KillAura;
import project.daprian.client.modules.TestThing;
import project.daprian.client.modules.VanillaTweaks;
import project.daprian.systems.setting.Setting;

import java.lang.reflect.Field;
import java.util.HashMap;

@Getter
public class ModuleManager {
    private final HashMap<Class<? extends Module>, Module> moduleHashMap = new HashMap<>();

    @Tasked(taskName = "Init Module Manager")
    public void Init() {
        add(new VanillaTweaks());
        add(new TestThing());
        add(new KillAura());

        moduleHashMap.values().forEach(this::addOptionsFromFields);

        getModule(VanillaTweaks.class).Toggle();
    }

    private void addOptionsFromFields(Module module) {
        Field[] fields = module.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (!Setting.class.isAssignableFrom(field.getType()))
                continue;

            try {
                Setting<?> option = (Setting<?>) field.get(module);
                module.getSettings().add(option);
            } catch (IllegalAccessException e) {
                Main.getInstance().getLogger().error("Couldn't register option in " + module.getName());
            }
        }
    }

    public Module getModule(Class<? extends Module> clasz) {
        return moduleHashMap.get(clasz);
    }

    private void add(Module module) {
        Main.getInstance().getLogger().info(String.format("Registered %s: %s", module.getName(), module.getDescription()));
        moduleHashMap.put(module.getClass(), module);
    }
}