package project.daprian.systems.module;

import lombok.Getter;
import project.daprian.client.Main;
import project.daprian.systems.setting.Bind;
import project.daprian.systems.setting.Setting;

import java.lang.annotation.*;
import java.util.ArrayList;

@Getter
public class Module {

    private final Info info;

    private final String name;
    private final String description;
    private final Category category;
    private final Setting<Bind> bind;

    private final ArrayList<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;

    public Module() {
        this.info = getClass().getAnnotation(Info.class);

        if (this.info == null) {
            throw new IllegalArgumentException("Module class must be annotated with @Info dumbass");
        }

        name = info.name();
        description = info.description();
        category = info.category();
        bind = Setting.create(bindSetting -> {
            bindSetting.setValues("Bind", new Bind(info.bind()));
        });
        settings.add(bind);
    }

    protected void onEnable() {
        Main.getInstance().getLogger().info(String.format("Enabled %s.", name));
    }
    protected void onDisable() {
        Main.getInstance().getLogger().info(String.format("Disabled %s.", name));
    }

    public void Toggle() {
        enabled = !enabled;

        if (enabled) {
            Main.getInstance().getPubSub().subscribe(this);
        } else {
            Main.getInstance().getPubSub().unsubscribe(this);
        }

        commonToggleAction();
    }

    public void Toggle(boolean state) {
        enabled = state;

        if (enabled) {
            Main.getInstance().getPubSub().subscribe(this);
        } else {
            Main.getInstance().getPubSub().unsubscribe(this);
        }

        commonToggleAction();
    }

    private void commonToggleAction() {
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();
        String description() default "My man so dumb he cant make a description for a fokin module 😭";
        Category category();
        int bind() default 0;
    }
}