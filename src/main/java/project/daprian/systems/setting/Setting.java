package project.daprian.systems.setting;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
public class Setting<T> {
    // Tafak u want pervert
    private String name;
    private T value;
    private T defaultValue;
    private T minimum;
    private T maximum;
    private T incrementation;
    private Supplier<Boolean> visible = () -> true;
    private int index = 0;

    private Setting() {
        if (this.getValue() instanceof Enum) {
            this.index = ((Enum<?>) value).ordinal();
        }
    }

    public T getMode(boolean previous) {
        if (this.getValue() instanceof Enum) {
            Enum<?> enumeration = (Enum<?>) this.getValue();
            String[] values = Arrays.stream(enumeration.getClass().getEnumConstants()).map(Enum::name).toArray(String[]::new);
            this.index = !previous ? (this.index + 1 > values.length - 1 ? 0 : this.index + 1) : (this.index - 1 < 0 ? values.length - 1 : this.index - 1);
            return (T) Enum.valueOf(enumeration.getClass(), values[this.index]);
        }
        return null;
    }

    public static class SettingBuilder<T> {
        private final Setting<T> setting = new Setting<>();

        public SettingBuilder<T> setValues(String name, T defaultValue, T minimum, T maximum, T incrementation) {
            setting.name = name;
            setting.value = defaultValue;
            setting.defaultValue = defaultValue;
            setting.minimum = minimum;
            setting.maximum = maximum;
            setting.incrementation = incrementation;
            return this;
        }

        public SettingBuilder<T> setValues(String name, T defaultValue, T minimum, T maximum) {
            setting.name = name;
            setting.value = defaultValue;
            setting.defaultValue = defaultValue;
            setting.minimum = minimum;
            setting.maximum = maximum;
            setting.incrementation = defaultValue;
            return this;
        }

        public SettingBuilder<T> setValues(String name, T defaultValue) {
            setting.name = name;
            setting.value = defaultValue;
            setting.defaultValue = defaultValue;
            return this;
        }

        public void setVisible(Supplier<Boolean> visible) {
            setting.visible = visible;
        }
        public Setting<T> build() {
            return setting;
        }
    }

    public static <T> Setting<T> create(Consumer<SettingBuilder<T>> configurer) {
        SettingBuilder<T> builder = new SettingBuilder<>();
        configurer.accept(builder);
        return builder.build();
    }
}