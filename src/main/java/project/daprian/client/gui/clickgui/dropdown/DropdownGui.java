package project.daprian.client.gui.clickgui.dropdown;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import project.daprian.client.Main;
import project.daprian.client.gui.clickgui.dropdown.component.Component;
import project.daprian.client.gui.clickgui.dropdown.component.components.Button;
import project.daprian.client.gui.clickgui.dropdown.component.Frame;
import project.daprian.systems.font.CBFontRenderer;
import project.daprian.systems.module.Category;
import project.daprian.utility.RenderUtil;

public class DropdownGui extends GuiScreen {

	public ArrayList<Frame> frames;
	private String typedString = "";
	private boolean typed;

	public DropdownGui() {
		this.frames = new ArrayList<>();
		int frameX = 5;
		for(Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 5;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		if (mc.currentScreen instanceof DropdownGui) {
			this.drawDefaultBackground();
		}

		this.drawSearchBar();

		for(Frame frame : frames) {
			frame.renderFrame(this.fontRendererObj);
			frame.updatePosition(mouseX, mouseY);
			for(Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	CBFontRenderer fr = Main.getInstance().getFontManager().getMinecraft();

	private void drawSearchBar() {
		String string = typed ? "Press Return to stop searching!" : "Press Return to search!";
		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
		float centerX = resolution.getScaledWidth() / 2f;
		float centerY = resolution.getScaledHeight() / 2f;

		fr.drawCenteredStringWithShadow(string, centerX, centerY - 20, -1);

		if (typed) {
			fr.drawCenteredStringWithShadow(typedString, centerX, centerY - 10, -1);

			if (!typedString.isEmpty()) {
				filterButtons();
			} else {
				renderAll();
			}
		} else {
			typedString = "";
			renderAll();
		}
	}

	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for (Frame frame : frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}

			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}

			if (frame.isOpen() && (!frame.getComponents().isEmpty())) {
				for (Component component : frame.getComponents()) {
					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {

		if (keyCode == Keyboard.KEY_RETURN) {
			typed = !typed;
		}

		if (typed) {
			if (keyCode == Keyboard.KEY_BACK) {
				if (!typedString.isEmpty()) {
					typedString = typedString.substring(0, typedString.length() - 1);
				}
			} else if (keyCode != Keyboard.KEY_RETURN){
				KeyEvent keyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, typedChar);

				if (Character.isLetter(keyEvent.getKeyChar()))
					typedString += keyEvent.getKeyChar();
			}
		}

		for (Frame frame : frames) {
			if (frame.isOpen() && keyCode != 1 && (!frame.getComponents().isEmpty())) {
				for (Component component : frame.getComponents()) {
					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : frames) {
			frame.setDrag(false);
		}
		for (Frame frame : frames) {
			if (frame.isOpen() && (!frame.getComponents().isEmpty())) {
				for (Component component : frame.getComponents()) {
					component.mouseReleased(mouseX, mouseY, state);
				}
			}
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		typedString = "";
		typed = false;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void filterButtons() {
		for (Frame frame : frames) {
			for (Component component : frame.getComponents()) {
				if (component instanceof Button) {
					Button button = (Button) component;
					button.setFocused(RenderUtil.containsIgnoreCase(button.getMod().getName(), typedString));
					frame.setOpen(RenderUtil.containsIgnoreCase(button.getMod().getName(), typedString));
				}
			}
		}
	}

	private void renderAll() {
		for (Frame frame : frames) {
			for (Component component : frame.getComponents()) {
				if (component instanceof Button) {
					Button button = (Button) component;
					button.setFocused(false);
				}
			}
		}
	}
}