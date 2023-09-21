package project.daprian.client.commands;

import project.daprian.client.Main;
import project.daprian.systems.command.Command;

@Command.Info(name = "SetValue", usage = ".setvalue <module> <name> <value>", alias = {"sv"}, description = "Sets a fucking value")
public class SetValue extends Command {
    @Override
    public void Execute(String[] args) {
        Main.getInstance().chat("LAKDFSHJKADFJSLGHADFKPSGJADFHGKLJADFS", false);
    }
}