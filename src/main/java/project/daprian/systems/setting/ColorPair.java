package project.daprian.systems.setting;

import lombok.Getter;

import java.awt.*;

@Getter
public class ColorPair {
    private final Color firstColor;
    private final Color secondColor;

    public ColorPair(Color firstColor, Color secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }
}
