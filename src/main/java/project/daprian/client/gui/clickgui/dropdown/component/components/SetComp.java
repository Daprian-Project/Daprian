package project.daprian.client.gui.clickgui.dropdown.component.components;


import lombok.Getter;
import net.minecraft.client.gui.Gui;
import project.daprian.client.gui.clickgui.dropdown.component.Component;
import project.daprian.systems.setting.Setting;

import java.awt.*;

@Getter
public class SetComp<T> extends Component {

    private final Setting<T> setting;
    private final Button parent;

    protected boolean hovered;
    protected boolean open;
    protected int offset;
    protected int x;
    protected int y;

    public SetComp(Setting<T> setting, Button parent) {
        super(parent.getWidth(), 16);
        this.parent = parent;
        this.setting = setting;
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + getParent().parent.getWidth() && y > this.y && y < this.y + getHeight();
    }

    public void drawDefault() {
        boolean enabled = parent.getMod().isEnabled();
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + getHeight(), enabled ? new Color(34,34,34).getRGB() : new Color(30,30,30).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 1)
            open = !open;
    }
}