package project.daprian.client.gui.dropdown.component.components;

import java.awt.*;
import java.util.ArrayList;

import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import project.daprian.client.gui.dropdown.component.Component;
import project.daprian.client.gui.dropdown.component.Frame;
import project.daprian.client.gui.dropdown.component.components.sub.Checkbox;
import project.daprian.client.gui.dropdown.component.components.sub.Keybind;
import project.daprian.client.gui.dropdown.component.components.sub.Mode;
import project.daprian.client.gui.dropdown.component.components.sub.Slider;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.*;


public class Button extends Component {

	@Getter
	public Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	public ArrayList<SetComp<?>> subcomponents;
	public boolean open;
	private final int height;

	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<>();
		this.open = false;
		height = 14;

		for (Setting<?> setting : mod.getSettings()) {
			if (setting.getValue() instanceof Boolean)
				subcomponents.add(new Checkbox((Setting<Boolean>) setting, this));
			if (setting.getValue() instanceof Number)
				this.subcomponents.add(new Slider((Setting<Number>) setting, this));
			if (setting.getValue() instanceof Enum<?>)
				this.subcomponents.add(new Mode((Setting<Enum<?>>) setting, this));
			if (setting.getValue() instanceof Bind)
				this.subcomponents.add(new Keybind((Setting<Bind>) setting, this));
		}
	}

	@Override
	public void setOff(int newOff) {
		int kekY = 0;
		for(SetComp<?> comp : openComps()) {
			kekY += comp.getHeight();
		}
		if(this.open) parent.tY = parent.tY + kekY;
		offset = newOff;
		int opY = offset + height;
		for(SetComp<?> comp : this.openComps()) {
			comp.setOff(opY);
			opY += comp.getHeight();
		}
	}

	@Override
	public void renderComponent(FontRenderer fr) {
		Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 14 + this.offset, this.mod.isEnabled() ? new Color(28,28,28).getRGB() : new Color(24,24,24).getRGB());

		GL11.glPushMatrix();
		fr.drawString(this.mod.getName(), (parent.getX() + (isHovered ? 5  : 3)), (parent.getY() + offset + 4), -1);
		if(this.subcomponents.size() > 2)
			fr.drawString(this.open ? "-" : "+", (parent.getX() + parent.getWidth() - (this.open ? 8.5f : 10)), (parent.getY() + offset + 4), -1);
		GL11.glPopMatrix();

		if(this.open) {
			if(!this.subcomponents.isEmpty()) {
				for(SetComp<?> comp : openComps()) {
					comp.renderComponent(fr);
				}
			}
		}
	}

	@Override
	public int getHeight() {
		if(this.open) {
			int height = this.height;
			for(int i = 0; i < openComps().size(); i++) {
				height += openComps().get(i).getHeight();
			}
			return (height);
		}
		return height;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			if(!this.openComps().isEmpty()){
				for (SetComp<?> comp : this.openComps()) {
					comp.updateComponent(mouseX, mouseY);
				}
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.Toggle();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for(SetComp<?> comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(SetComp<?> comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int key) {
		for(SetComp<?> comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 14 + this.offset;
    }

	public ArrayList<SetComp<?>> openComps() {
		ArrayList<SetComp<?>> open = new ArrayList<>();
		for(SetComp<?> comp : subcomponents) {
			if(comp.getSetting() == null) {
				open.add(comp);
			} else if (comp.getSetting().getVisible().get()) {
				open.add(comp);
			}
		}
		return open;
	}
}
