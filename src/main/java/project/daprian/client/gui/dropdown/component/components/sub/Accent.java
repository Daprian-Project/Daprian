package project.daprian.client.gui.dropdown.component.components.sub;

import net.minecraft.client.gui.FontRenderer;
import project.daprian.client.gui.dropdown.component.components.Button;
import project.daprian.client.gui.dropdown.component.components.SetComp;
import project.daprian.systems.setting.ColorType;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.ColorUtils;
import project.daprian.utility.RenderUtil;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Accent extends SetComp<ColorType<?>> {
    public Accent(Setting<ColorType<?>> sat, Button button) {
        super(sat, button);

        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
    }

    @Override
    public void renderComponent(FontRenderer fontRenderer) {
        drawDefault();
        fontRenderer.drawString(getSetting().getName(), getParent().parent.getX() + (hovered ? 6 : 4), (getParent().parent.getY() + offset + 3), -1);

        String ajaj = getSetting().getValue().getColorOption().name();
        float stringWidth = fontRenderer.getStringWidth(ajaj);
        fontRenderer.drawString(ajaj, getParent().parent.getX() + getParent().parent.getWidth() - stringWidth - 14, (getParent().parent.getY() + offset + 3), -1);
        Color color = ColorUtils.interpolateColorsBackAndForth(10, 1, getSetting().getValue().getFirst(), getSetting().getValue().getSecond(), false);
        RenderUtil.drawRect(getParent().parent.getX() + getParent().parent.getWidth() - 12, (getParent().parent.getY() + offset + 3), 8, 8, color);
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = getParent().parent.getY() + offset;
        this.x = getParent().parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if(isMouseOnButton(mouseX, mouseY) && this.getParent().open) {
            getSetting().setValue(getSetting().getMode(button == 1));
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + getParent().parent.getWidth() && y > this.y && y < this.y + getHeight();
    }

    public static double round(double num, double increment) {
        if (increment < 0) {
            throw new IllegalArgumentException();
        }

        return new BigDecimal(num).setScale((int) increment, RoundingMode.HALF_UP).doubleValue();
    }
}