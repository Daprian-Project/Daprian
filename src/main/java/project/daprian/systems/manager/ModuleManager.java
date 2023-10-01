package project.daprian.systems.manager;

import lombok.Getter;
import me.daprian.tasks.Tasked;
import project.daprian.client.Main;
import project.daprian.client.modules.combat.KillAura;
import project.daprian.client.modules.combat.Velocity;
import project.daprian.client.modules.movement.Flight;
import project.daprian.client.modules.movement.NoSlow;
import project.daprian.client.modules.movement.Speed;
import project.daprian.client.modules.player.AutoArmor;
import project.daprian.client.modules.player.ChestStealer;
import project.daprian.client.modules.player.FastPlace;
import project.daprian.client.modules.render.Chams;
import project.daprian.client.modules.render.ESP;
import project.daprian.client.modules.render.HUD;
import project.daprian.client.modules.render.VanillaTweaks;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;

import java.lang.reflect.Field;
import java.util.*;

@Getter
public class ModuleManager {
    private final HashMap<Class<? extends Module>, Module> moduleHashMap = new HashMap<>();

    @Tasked(taskName = "Init Module Manager")
    public void Init() {
        add(new VanillaTweaks());
        add(new HUD());
        add(new KillAura());
        add(new ESP());
        add(new Speed());
        add(new Flight());
        add(new Velocity());
        add(new Chams());
        add(new NoSlow());
        add(new ChestStealer());
        add(new AutoArmor());
        add(new FastPlace());
        moduleHashMap.values().forEach(this::addOptionsFromFields);

        getModule(VanillaTweaks.class).Toggle();
        getModule(HUD.class).Toggle();
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