package project.daprian.systems.command;

import project.daprian.client.Main;

public class CommandException extends RuntimeException {
    public CommandException(String message, String usage) {
        Main.getInstance().chat(String.format("%s - %s", message, usage), false);
    }
}