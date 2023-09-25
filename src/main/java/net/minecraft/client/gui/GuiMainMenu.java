package net.minecraft.client.gui;

import lombok.SneakyThrows;
import net.minecraft.client.resources.I18n;
import project.daprian.client.Main;
import project.daprian.client.gui.alt.AltLogin;
import project.daprian.utility.RenderUtil;
import project.daprian.utility.ScrollUtil;
import project.daprian.utility.URLUtil;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiMainMenu extends GuiScreen {
    private static final int BUTTON_OPTIONS_ID = 0;
    private static final int BUTTON_QUIT_ID = 4;
    private static final int BUTTON_SINGLEPLAYER_ID = 1;
    private static final int BUTTON_MULTIPLAYER_ID = 2;
    private static final int BUTTON_ALT_LOGIN_ID = 14;
    ScrollUtil scrollHelper = new ScrollUtil();

    public void initGui() {
        int centerY = this.height / 4 + 48;
        int yOffset = 24;

        this.addSingleplayerMultiplayerButtons(centerY, yOffset);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int colorBackground = new Color(15, 15, 15).getRGB();
        drawRect(0, 0, width, height, colorBackground);
        drawChangelog();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SneakyThrows
    private void drawChangelog() {
        scrollHelper.setStep(50);
        scrollHelper.setElementsHeight(200);
        scrollHelper.setMaxScroll(200);
        scrollHelper.setFlag(true);
        float sc = scrollHelper.getScroll();

        float x = 5;
        float y = 6;
        float width = 150;
        float height = scrollHelper.getElementsHeight();

        RenderUtil.drawRect(x, y, width, height, new Color(0, 0, 0, 100));
        fontRendererObj.drawString(String.format("Changelog (%s - %s)", Main.getInstance().getVersion(), Main.getInstance().getBuild()), x + 1, y + 1, -1);

        List<String> texts = URLUtil.getContent("https://raw.githubusercontent.com/Daprian-Project/Daprian/main/Changelog");

        RenderUtil.pushScissor(x, y + fontRendererObj.FONT_HEIGHT, width, height - fontRendererObj.FONT_HEIGHT);
        RenderUtil.scissorRect(x, y + fontRendererObj.FONT_HEIGHT, width, height - fontRendererObj.FONT_HEIGHT);

        float textY = y + fontRendererObj.FONT_HEIGHT + 2;

        int offset = (int) textY;
        for (String text : texts) {
            Color color = text.contains("+") ? Color.green : text.contains("-") ? Color.red : text.contains("~") ? Color.yellow : new Color(190, 235, 255);
            fontRendererObj.drawString(text, x + 1, offset + sc, color.getRGB());
            offset += fontRendererObj.FONT_HEIGHT;
        }

        RenderUtil.popScissor();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void addSingleplayerMultiplayerButtons(int yPosition, int yOffset) {
        this.buttonList.add(new GuiButton(BUTTON_SINGLEPLAYER_ID, this.width / 2 - 85 / 2, yPosition, 85, 20, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(BUTTON_MULTIPLAYER_ID, this.width / 2 - 85 / 2, yPosition + yOffset, 85, 20, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(BUTTON_ALT_LOGIN_ID, this.width / 2 - 85 / 2, yPosition + yOffset * 2, 85, 20, "Alt Login"));
        this.buttonList.add(new GuiButton(BUTTON_QUIT_ID, this.width / 2 - 85 / 2, yPosition + yOffset * 3, 85, 20, I18n.format("menu.quit")));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == BUTTON_OPTIONS_ID) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == BUTTON_SINGLEPLAYER_ID) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == BUTTON_MULTIPLAYER_ID) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == BUTTON_ALT_LOGIN_ID) {
            this.mc.displayGuiScreen(new AltLogin(this));
        }

        if (button.id == BUTTON_QUIT_ID) {
            this.mc.shutdown();
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}