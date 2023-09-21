package project.daprian.systems.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import project.daprian.client.Main;
import project.daprian.systems.setting.Bind;
import project.daprian.systems.setting.Setting;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.function.Supplier;

@Getter
public class Module {

    private final Info info;

    private final String name;
    private final String description;
    private final Category category;
    private final Setting<Bind> bind;
    protected final Minecraft mc = Minecraft.getMinecraft();

    @Setter
    private Supplier<String> suffix = () -> "";

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

    public String getDisplayName(boolean lower) {
        if (lower) {
            if (suffix.get().isEmpty())
                return name.toLowerCase();
            else
                return (name + " " + EnumChatFormatting.WHITE + suffix.get()).toLowerCase();
        } else {
            if (suffix.get().isEmpty())
                return name;
            else
                return name + " " + EnumChatFormatting.WHITE + suffix.get();
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