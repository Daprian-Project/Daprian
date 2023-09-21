package project.daprian.client.gui.dropdown.component.components.sub;


import net.minecraft.client.gui.FontRenderer;
import project.daprian.client.gui.dropdown.component.components.Button;
import project.daprian.client.gui.dropdown.component.components.SetComp;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.RenderUtil;

public class Checkbox extends SetComp<Boolean> {
	
	public Checkbox(Setting<Boolean> option, Button button) {
		super(option, button);

		this.x = getParent().parent.getX() + button.parent.getWidth();
		this.y = getParent().parent.getY() + getParent().offset;
	}

	@Override
	public void renderComponent(FontRenderer fontRenderer) {
		drawDefault();

		fontRenderer.drawString(this.getSetting().getName(), getParent().parent.getX() + (hovered ? 6 : 4), (getParent().parent.getY() + offset + 3), -1);

		float width = 4;
		float height = 4;
		RenderUtil.drawRect(getParent().parent.getX() + getParent().parent.getWidth() - width - 4, (getParent().parent.getY() + offset + height / 2), width, height, 0xFF999999);

		if(this.getSetting().getValue())
			RenderUtil.drawRect(getParent().parent.getX() + getParent().parent.getWidth() - width - 3.5, (getParent().parent.getY() + offset + height - 1.5), width - 1, height - 1, 0xFF666666);
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
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.getParent().open) {
			this.getSetting().setValue(!getSetting().getValue());;
		}
	}
}