package project.daprian.client.gui.clickgui.dropdown.component;

import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;

@Getter
public class Component {

	protected int width;
	@Getter
	protected int height;

	public Component(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void renderComponent(FontRenderer fontRenderer) {
		
	}
	
	public void updateComponent(int mouseX, int mouseY) {

	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		
	}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	public void keyTyped(char typedChar, int key) {
		
	}
	
	public void setOff(int newOff) {

	}
}
