package project.daprian.client.gui.clickgui.dropdown.component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import project.daprian.client.Main;
import project.daprian.client.gui.clickgui.dropdown.component.components.Button;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;

public class Frame {

	@Getter
	private final List<Component> components;
	private final Category category;
	@Getter
	private boolean open;
	@Getter @Setter
	private boolean wasOpen;
	@Getter
	private int width;
	@Getter
	private int y;
	@Getter
	private int x;
	public int tY;
	private int barHeight;
	private int compHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;
	
	public Frame(Category cat) {
		this.components = new ArrayList<>();
		this.category = cat;
		this.width = 125;
		this.x = 5;
		this.y = 5;
		this.barHeight = 18;
		this.compHeight = 16;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		tY = this.barHeight;

		for(Module mod : Main.getInstance().getModuleManager().getModuleHashMap().values()) {
			if (mod.getCategory() != cat) continue;

			project.daprian.client.gui.clickgui.dropdown.component.components.Button modButton = new Button(width, compHeight, mod, this, tY);
			this.components.add(modButton);
			tY += modButton.getHeight();
		}
	}

	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public void setOpen(boolean open) {
		wasOpen = this.open;
		this.open = open;
	}
	
	public void renderFrame(FontRenderer fontRenderer) {
		this.refresh();

		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, new Color(32, 32, 32).getRGB());

		GL11.glPushMatrix();
		 fontRenderer.drawString(this.category.name(), (this.x + 2) + 2, (this.y + 5), -1);
		 fontRenderer.drawString(this.open ? "-" : "+", (this.x + this.width - 10), (this.y + 5), -1);
		GL11.glPopMatrix();

		if (this.open && (!this.components.isEmpty())) {
			for (Component component : components) {
				component.renderComponent(fontRenderer);
			}

		}
	}

	public void refresh() {
		tY = components.size() * compHeight + this.barHeight;
		int off = this.barHeight;
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		if(x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}
	
}
