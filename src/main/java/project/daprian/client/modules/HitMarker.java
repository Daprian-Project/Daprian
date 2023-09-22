package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import project.daprian.client.events.RenderUIEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.utility.MathUtil;

import static org.lwjgl.opengl.GL11.*;

@Module.Info(name = "HitMarker", category = Category.Render, description = "Fancy Hit Markers", bindable = false)
public class HitMarker extends Module {

    private double currentAlpha = 255;

    @Listen
    public void onRender(RenderUIEvent e) {
        double dif = Math.abs(currentAlpha);
        int fps = Minecraft.getDebugFPS();
        if (dif > 0.0D) {
            double animationSpeed = MathUtil.roundDecimalPlaces(Math.min(10.0D, Math.max(0.005D, 144.0D / fps * 4)), 3);
            if (dif < animationSpeed)
                animationSpeed = dif;
            if (currentAlpha < 0)
                currentAlpha = currentAlpha + animationSpeed;
            if (currentAlpha > 0)
                currentAlpha = currentAlpha - animationSpeed;
        }
        for (int i = 0; i < 4; i++) {
            drawHitMarker(e.getScaledResolution());
        }
    }

    private void drawHitMarker(ScaledResolution scaledResolution) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(2.0F);
        glColor4f(1f, 1f, 1f, (float) currentAlpha / 255.0F);
        glBegin(GL11.GL_LINES);

        glVertex2d(scaledResolution.getScaledWidth() / 2.0 - 6, scaledResolution.getScaledHeight() / 2.0 - 7);
        glVertex2d(scaledResolution.getScaledWidth() / 2.0 - 3, scaledResolution.getScaledHeight() / 2.0 - 4);

        glVertex2d(scaledResolution.getScaledWidth() / 2.0 + 7, scaledResolution.getScaledHeight() / 2.0 + 7);
        glVertex2d(scaledResolution.getScaledWidth() / 2.0 + 4, scaledResolution.getScaledHeight() / 2.0 + 4);

        glVertex2d(scaledResolution.getScaledWidth() / 2.0 - 6, scaledResolution.getScaledHeight() / 2.0 + 7);
        glVertex2d(scaledResolution.getScaledWidth() / 2.0 - 3, scaledResolution.getScaledHeight() / 2.0 + 4);

        glVertex2d(scaledResolution.getScaledWidth() / 2.0 + 7, scaledResolution.getScaledHeight() / 2.0 - 7);
        glVertex2d(scaledResolution.getScaledWidth() / 2.0 + 4, scaledResolution.getScaledHeight() / 2.0 - 4);

        glEnd();
        glEnable(GL_TEXTURE_2D);
    }
}