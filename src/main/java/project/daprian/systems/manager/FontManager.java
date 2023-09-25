package project.daprian.systems.manager;

import lombok.Getter;
import me.daprian.tasks.Tasked;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import project.daprian.systems.font.CBFontRenderer;

import java.awt.*;
import java.io.InputStream;

@Getter
public class FontManager {

    private CBFontRenderer minecraft;

    @Tasked(taskName = "Init Font Manager")
    public void Init() {
        minecraft = new CBFontRenderer(getFont("Minecraft", 18), true, false);
    }

    private static Font getFont(String name, int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("daprian/font/" + name + ".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception a) {

            try {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("daprian/font/" + name + ".otf")).getInputStream();
                font = Font.createFont(0, is);
                font = font.deriveFont(Font.PLAIN, size);

            } catch (Exception b) {
                font = new Font("default", Font.PLAIN, size);
            }
        }

        return font;
    }
}