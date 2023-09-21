package project.daprian.systems.command;

import lombok.Getter;
import me.daprian.tasks.Tasked;
import project.daprian.client.Main;
import project.daprian.client.commands.SetValue;

import java.util.ArrayList;

@Getter
public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList<>();

    @Tasked(taskName = "Init Command Manager")
    public void Init() {
        add(new SetValue());
    }

    private void add(Command command) {
        Main.getInstance().getLogger().info(String.format("Registered %s: %s", command.getName(), command.getDescription()));
        commands.add(command);
    }
}