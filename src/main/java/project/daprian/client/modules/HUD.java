package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import project.daprian.client.Main;
import project.daprian.client.events.RenderUIEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.ColorUtils;

import java.awt.*;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@Module.Info(name = "HUD", category = Category.Render)
public class HUD extends Module {

    private final Setting<ColorMode> colorMode = Setting.create(setting -> setting.setValues("Color", ColorMode.Red));
    private final Setting<Boolean> lowerCase = Setting.create(setting -> setting.setValues("Lower Case", false));
    FontRenderer fr = mc.fontRendererObj;

    @Listen
    public void onRender(RenderUIEvent event) {
        drawWatermark();
        drawArraylist();
    }

    private void drawWatermark() {
        fr.drawString(String.format("%s %s", Main.getInstance().getName(), Main.getInstance().getVersion()), 5, 5, -1);
    }

    private void drawArraylist() {
        ScaledResolution sr = new ScaledResolution(mc);
        AtomicInteger offset = new AtomicInteger();

        Main.getInstance().getModuleManager().getModuleHashMap().values().stream().sorted(Comparator.comparing(m -> fr.getStringWidth(((Module) m).getDisplayName(lowerCase.getValue()))).reversed()).forEach(module -> {
            if (!module.isEnabled()) return;
            String moduleName = module.getDisplayName(lowerCase.getValue());
            float stringWidth = fr.getStringWidth(moduleName);
            float stringHeight = fr.FONT_HEIGHT;
            float posX = (sr.getScaledWidth() - stringWidth) - 5;
            float posY = 5 + offset.get() * stringHeight;

            Color interpolatedColor = ColorUtils.interpolateColorsBackAndForth(10, offset.get() + offset.get(), colorMode.getValue().getFirst(), colorMode.getValue().getSecond(), false);
            fr.drawString(moduleName, posX, posY, interpolatedColor.getRGB());

            offset.getAndIncrement();
        });
    }

    @Getter
    private enum ColorMode {
        Red(new Color(127, 0, 0), new Color(255, 0, 0)),
        Green(new Color(0, 127, 0), new Color(0, 255, 0)),
        Blue(new Color(0, 0, 127), new Color(0, 0, 255));

        private final Color first;
        private final Color second;

        ColorMode(Color first, Color second) {
            this.first = first;
            this.second = second;
        }
    }
}