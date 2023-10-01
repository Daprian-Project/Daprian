package project.daprian.systems.manager;

import lombok.Getter;
import project.daprian.systems.friend.Friend;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class FriendManager {
    private final ArrayList<Friend> friends = new ArrayList<>();

    public void add(String name) {
        friends.add(new Friend(name));
    }

    public void remove(String name) {
        friends.remove(getFriend(name));
    }

    public boolean isFriend(String name) {
        return getFriend(name) != null;
    }

    public Friend getFriend(String name) {
        for (Friend friend : friends) {
            if (!Objects.equals(friend.getName(), name))
                continue;

            return friend;
        }

        return null;
    }
}