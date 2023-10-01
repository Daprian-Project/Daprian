package project.daprian.client.commands;

import project.daprian.client.Main;
import project.daprian.systems.command.Command;
import project.daprian.systems.friend.Friend;
import project.daprian.systems.manager.FriendManager;

@Command.Info(name = "friends", usage = ".friend <add/remove/list> <name>", alias = {"f", "friend"})
public class Friends extends Command {
    @Override
    public void Execute(String[] args) {
        Main main = Main.getInstance();
        FriendManager manager = main.getFriendManager();

        if (args[1] == null) {
            main.chat(getUsage(), false);
            return;
        }

        switch (args[1]) {
            case "add":
                if (args[2] == null) {
                    main.chat(getUsage(), false);
                    return;
                }

                if (manager.isFriend(args[2])) {
                    main.chat(String.format("%s is already a friend!", args[2]), false);
                    return;
                }

                main.chat(String.format("Successfully added %s to friend list!", args[2]), false);
                manager.add(args[2]);
                break;
            case "remove":
                if (args[2] == null) {
                    main.chat(getUsage(), false);
                    return;
                }

                if (!manager.isFriend(args[2])) {
                    main.chat(String.format("%s is not a friend!", args[2]), false);
                    return;
                }

                main.chat(String.format("Successfully removed %s from friend list!", args[2]), false);
                manager.remove(args[2]);
                break;
            case "list":
                if (manager.getFriends().isEmpty()) {
                    main.chat("You have no friends!", false);
                } else {
                    main.chat(String.format("You have %s friends:", manager.getFriends().size()), false);
                    for (Friend f : manager.getFriends()) {
                        main.chat("- " + f.getName(), false);
                    }
                }
                break;
            default:
                main.chat(getUsage(), false);
                break;
        }
    }

}