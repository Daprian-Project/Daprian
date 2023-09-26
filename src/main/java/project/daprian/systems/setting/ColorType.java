package project.daprian.systems.setting;

import java.awt.*;

public interface ColorType<T extends Enum<T>> {
    T getColorOption();
    Color getFirst();
    Color getSecond();
}