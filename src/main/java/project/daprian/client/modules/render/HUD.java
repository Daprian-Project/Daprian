package project.daprian.client.modules.render;

import com.google.common.util.concurrent.AtomicDouble;
import io.github.nevalackin.radbus.Listen;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import project.daprian.client.Main;
import project.daprian.client.events.RenderUIEvent;
import project.daprian.systems.font.CBFontRenderer;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.notification.NotificationManager;
import project.daprian.systems.setting.ColorPair;
import project.daprian.systems.setting.ColorType;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.ColorUtils;
import project.daprian.utility.MathUtil;
import project.daprian.utility.MovementUtil;
import project.daprian.utility.RenderUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Module.Info(name = "HUD", category = Category.Render, bindable = false)
public class HUD extends Module {
    private final Setting<BackMode> backMode = Setting.create(setting -> setting.setValues("Color", BackMode.Black));
    private final Setting<Integer> bgAlpha = Setting.create(setting -> {
        setting.setValues("Alpha", 100, 50, 255, 5);
        setting.setVisible(() -> !backMode.getValue().equals(BackMode.None));
    });
    private final Setting<ColorMode> colorMode = Setting.create(setting -> setting.setValues("Color", ColorMode.Cyan));
    private final Setting<Boolean> info = Setting.create(setting -> setting.setValues("Player Info", false));
    private final Setting<Boolean> lowerCase = Setting.create(setting -> setting.setValues("Lower Case", true));
    private final Setting<Boolean> sideLine = Setting.create(setting -> setting.setValues("Side Line", true));
    private final Setting<Boolean> notifications = Setting.create(setting -> setting.setValues("Notifications", true));
    FontRenderer fr = mc.fontRendererObj;
    NotificationManager notificationManager = new NotificationManager();

    @Listen
    public void onRender(RenderUIEvent event) {
        setSuffix(() -> colorMode.getValue().name());
        drawWatermark();
        drawArraylist();
        drawInfo();

        if (notifications.getValue())
            notificationManager.render();
    }

    private void drawInfo() {
        if (!info.getValue()) return;

        ScaledResolution sr = new ScaledResolution(mc);

        CBFontRenderer fr = Main.getInstance().getFontManager().getMinecraft();

        float posX = 5;
        float posYOffset = fr.getHeight();
        float posY = sr.getScaledHeight() - posYOffset;

        fr.drawStringWithShadow(String.format("FPS %s", Minecraft.getDebugFPS()), posX, posY - 7.5 - posYOffset, -1);
        fr.drawStringWithShadow(String.format("Speed %s", MathUtil.roundDecimalPlaces(MovementUtil.getSpeed(), 3)), posX, posY - 5, -1);
    }

    private void drawWatermark() {
        String waterMark = String.format("%s %s", Main.getInstance().getName(), Main.getInstance().getVersion());
        CBFontRenderer fr = Main.getInstance().getFontManager().getMinecraft();

        int offset = 0;
        for (char c : waterMark.toCharArray()) {
            Color interpolatedColor = ColorUtils.interpolateColorsBackAndForth(10, offset + offset, colorMode.getValue().getPair().getFirstColor(), colorMode.getValue().getPair().getSecondColor(), false);
            fr.drawStringWithShadow(String.valueOf(c), 4 + offset, 5, interpolatedColor.getRGB());
            offset += fr.getStringWidth(String.valueOf(c));
        }
    }

    private void drawArraylist() {
        ScaledResolution sr = new ScaledResolution(mc);
        AtomicDouble offset = new AtomicDouble();

        CBFontRenderer fr = Main.getInstance().getFontManager().getMinecraft();

        Main.getInstance().getModuleManager().getModuleHashMap().values().stream().sorted(Comparator.comparing(m -> fr.getStringWidth(((Module) m).getDisplayName(lowerCase.getValue()))).reversed()).forEach(module -> {
            if (!module.isEnabled() || module instanceof VanillaTweaks) return;
            String moduleName = module.getDisplayName(lowerCase.getValue());
            float stringWidth = fr.getStringWidth(moduleName);
            float stringHeight = fr.getHeight();
            float posX = (sr.getScaledWidth() - stringWidth) - (sideLine.getValue() ? 6 : 5);
            float posY = (float) (5 + offset.get() * stringHeight);

            Color interpolatedColor = ColorUtils.interpolateColorsBackAndForth(10, (int) (offset.get() * -10), colorMode.getValue().getPair().getFirstColor(), colorMode.getValue().getPair().getSecondColor(), false);

            switch (backMode.getValue()) {
                case None:
                    break;
                case Black:
                    RenderUtil.drawRect(posX - 2, posY - 2, stringWidth + 4, stringHeight + 4, new Color(0, 0, 0, bgAlpha.getValue()));
                    break;
                case Follow:
                    RenderUtil.drawRect(posX - 2, posY - 2, stringWidth + 4, stringHeight + 4, ColorUtils.modifyAlpha(interpolatedColor, bgAlpha.getValue()));
                    break;
            }

            if (sideLine.getValue()) {
                RenderUtil.drawRect(sr.getScaledWidth() - 4, posY - 2, 1, stringHeight + 4, interpolatedColor);
            }

            fr.drawStringWithShadow(moduleName, posX, posY, interpolatedColor.getRGB());

            offset.set(offset.get() + 1.57);
        });
    }

    @Getter
    private enum BackMode {
        None,
        Black,
        Follow;
    }

    @Getter
    private enum ColorMode implements ColorType<ColorMode> {
        Red(new ColorPair(new Color(127, 0, 0), new Color(255, 0, 0))),
        Green(new ColorPair(new Color(0, 127, 0), new Color(0, 255, 0))),
        Blue(new ColorPair(new Color(0, 0, 127), new Color(0, 0, 255))),
        Cyan(new ColorPair(new Color(0, 80, 140), new Color(0, 160, 255)));

        private final ColorPair pair;

        ColorMode(ColorPair pair) {
            this.pair = pair;
        }

        @Override
        public ColorMode getColorOption() {
            return this;
        }

        @Override
        public Color getFirst() {
            return pair.getFirstColor();
        }

        @Override
        public Color getSecond() {
            return pair.getSecondColor();
        }
    }
}