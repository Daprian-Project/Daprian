package project.daprian.systems.command;

import lombok.Getter;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Getter
public abstract class Command {
    private final Info info;

    private final String name;
    private final String usage;
    private final String[] alias;
    private final String description;

    public Command() {
        this.info = getClass().getAnnotation(Info.class);

        if (this.info == null) {
            throw new IllegalArgumentException("Command class must be annotated with @Info dumbass");
        }

        name = info.name();
        description = info.description();
        usage = info.usage();
        alias = info.alias();
    }

    public abstract void Execute(String[] args);


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();
        String usage();
        String[] alias();
        String description() default "Once again too dumb to make descriptions";
    }
}