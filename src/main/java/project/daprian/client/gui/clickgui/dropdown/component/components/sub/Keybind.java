package project.daprian.client.gui.clickgui.dropdown.component.components.sub;

import net.minecraft.client.gui.FontRenderer;
import project.daprian.client.gui.clickgui.dropdown.component.components.Button;
import project.daprian.client.gui.clickgui.dropdown.component.components.SetComp;
import project.daprian.systems.setting.Bind;
import project.daprian.systems.setting.Setting;

public class Keybind extends SetComp<Bind> {

	private boolean binding;
	
	public Keybind(Setting<Bind> setting, Button button) {
		super(setting, button);

		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent(FontRenderer fontRenderer) {
		drawDefault();
		fontRenderer.drawString(binding ? "Press a key" : getSetting().getName(), getParent().parent.getX() + (hovered ? 6 : 4), (getParent().parent.getY() + offset + 3), -1);

		String ajaj = binding ? "..." : getSetting().getValue().getButtonName();
		float stringWidth = fontRenderer.getStringWidth(ajaj);
		fontRenderer.drawString(ajaj, getParent().parent.getX() + getParent().parent.getWidth() - stringWidth - 4, (getParent().parent.getY() + offset + 3), -1);
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
			if(button == 0) this.binding = !this.binding;
			if(button == 1 && this.binding) {
				this.getSetting().setValue(new Bind(0));
				this.binding = false;
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		if(this.binding) {
			this.getSetting().setValue(new Bind(key));
			this.binding = false;
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + getParent().parent.getWidth() && y > this.y && y < this.y + getHeight();
	}
}
