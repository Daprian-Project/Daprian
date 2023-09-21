package project.daprian.client;

import io.github.nevalackin.radbus.PubSub;
import lombok.Getter;
import me.daprian.tasks.TaskLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import project.daprian.client.gui.dropdown.ClickGui;
import project.daprian.systems.command.CommandManager;
import project.daprian.systems.event.Event;
import project.daprian.systems.module.ModuleManager;

@Getter
public class Main {
    @Getter
    private static Main instance;

    private final String name = "Daprian";
    private final String version = "0.0.1";
    private final String build = "092123";

    private final Logger logger = LogManager.getLogger();
    private final PubSub<Event> pubSub = PubSub.newInstance(logger::error);
    private final ModuleManager moduleManager = new ModuleManager();
    private final CommandManager commandManager = new CommandManager();
    private final ClickGui clickGui;

    public Main() {
        instance = this;

        TaskLoader taskLoader = new TaskLoader();
        taskLoader.scanTasks(moduleManager);
        taskLoader.scanTasks(commandManager);
        taskLoader.executeTasks(true);

        clickGui = new ClickGui();
    }

    public void chat(String message, boolean raw) {
        if (raw)
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        else
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Daprian > " + message));
    }

}