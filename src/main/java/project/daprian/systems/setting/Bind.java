package project.daprian.systems.setting;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;

/**
 * @author Surge
 * @modified Daprian
 * @since 08/08/2022
 */
public class Bind {
    @Setter @Getter
    private int buttonCode;

    public Bind(int buttonCode) {
        this.buttonCode = buttonCode;
    }

    public boolean isPressed() {
        if (buttonCode == 0) {
            return false;
        }

        return Keyboard.isKeyDown(buttonCode);
    }

    public String getButtonName() {
        return Keyboard.getKeyName(buttonCode);
    }
}