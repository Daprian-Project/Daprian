package project.daprian.client.gui.dropdown.component.components.sub;

import net.minecraft.client.gui.FontRenderer;
import project.daprian.client.gui.dropdown.component.components.Button;
import project.daprian.client.gui.dropdown.component.components.SetComp;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.MathUtil;
import project.daprian.utility.RenderUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends SetComp<Number> {
	
	private boolean drag;

	public Slider(Setting<Number> set, Button button) {
		super(set, button);

		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
	}

	@Override
	public void renderComponent(FontRenderer fontRenderer) {
		drawDefault();

		double value = MathUtil.roundDecimalPlaces(getSetting().getValue().doubleValue(), MathUtil.getDecimalPlaces(getSetting().getIncrementation().doubleValue()));
		fontRenderer.drawString(getSetting().getName(), getParent().parent.getX() + (hovered ? 6 : 4), (getParent().parent.getY() + offset + 3), -1);

		String ajaj = String.valueOf(value);
		float stringWidth = fontRenderer.getStringWidth(ajaj);
		fontRenderer.drawString(ajaj, getParent().parent.getX() + getParent().parent.getWidth() - stringWidth - 4, (getParent().parent.getY() + offset + 3), -1);

		float min = getSetting().getMinimum().floatValue();
		float max = getSetting().getMaximum().floatValue();
		float current = getSetting().getValue().floatValue();

		float sliderWidth = getParent().parent.getWidth();
		float renderWidth = (sliderWidth) * (current - min) / (max - min);

		RenderUtil.drawRect(getParent().parent.getX(), getParent().parent.getY() + offset + getHeight() - 1, renderWidth, 1, -1);
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = getParent().parent.getY() + offset;
		this.x = getParent().parent.getX();

		float min = getSetting().getMinimum().floatValue();
		float max = getSetting().getMaximum().floatValue();
		float step = getSetting().getIncrementation() != null ? getSetting().getIncrementation().floatValue() : 1;

		float sliderWidth = getParent().parent.getWidth();

		float diff = Math.min(sliderWidth, Math.max(0, mouseX - (getParent().parent.getX())));

		int places;

		if (step != 1)
			places = MathUtil.getDecimalPlaces(getSetting().getIncrementation().doubleValue());
		else
			places = 1;

		if (drag) {
			float value = (float) round(((diff / sliderWidth) * (max - min) + min), 2);
			value = Math.round(Math.max(min, Math.min(max, value)) * (1 / step)) / (1 / step);

			float finalValue = diff == 0 ? min : value;

			if (getSetting().getValue() instanceof Double) {
				getSetting().setValue(MathUtil.roundDecimalPlaces(finalValue, places));
			} else if (getSetting().getValue() instanceof Float) {
				getSetting().setValue(MathUtil.roundDecimalPlaces(finalValue, places));
			} else if (getSetting().getValue() instanceof Integer) {
				getSetting().setValue((int) MathUtil.roundDecimalPlaces(finalValue, places));
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);
		if(isMouseOnButton(mouseX, mouseY) && this.getParent().open) {
			if (button == 0)
				drag = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		if (drag)
			drag = false;
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