package project.daprian.systems.module;

import lombok.Getter;
import project.daprian.client.Main;

@Getter
public class Toggleable {

    protected boolean enabled;

    protected void onEnable() {}
    protected void onDisable() {}

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

}