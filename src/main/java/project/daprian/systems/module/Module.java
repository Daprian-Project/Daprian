package project.daprian.systems.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import project.daprian.client.Main;
import project.daprian.systems.notification.Notification;
import project.daprian.systems.notification.NotificationManager;
import project.daprian.systems.setting.Bind;
import project.daprian.systems.setting.Setting;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.function.Supplier;

@Getter
public class Module extends Toggleable {
    private final Info info;

    private final String name;
    private final String description;
    private final Category category;
    protected final Minecraft mc = Minecraft.getMinecraft();
    private Setting<Bind> bind = null;

    @Setter
    private Supplier<String> suffix = () -> "";

    private final ArrayList<Setting<?>> settings = new ArrayList<>();

    public Module() {
        this.info = getClass().getAnnotation(Info.class);

        if (this.info == null) {
            throw new IllegalArgumentException("Module class must be annotated with @Info dumbass");
        }

        name = info.name();
        description = info.description();
        category = info.category();

        if (info.bindable()) {
            bind = Setting.create(bindSetting -> {
                bindSetting.setValues("Bind", new Bind(info.bind()));
                bindSetting.setVisible(info::bindable);
            });

            settings.add(bind);
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

    protected void Check(Module module) {
        if (this.isEnabled() && module.isEnabled()) {
            NotificationManager.addNotification(new Notification(String.format("Disabled %s to prevent errors!", module.getName()), 2, Notification.Type.WARNING, Notification.Position.CROSSHAIR));
            module.Toggle();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();
        String description() default "No description provided.";
        Category category();
        boolean bindable();
        int bind() default 0;
    }
}