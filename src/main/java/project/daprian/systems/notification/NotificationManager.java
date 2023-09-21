package project.daprian.systems.notification;

import java.util.LinkedList;

public class NotificationManager {
    static LinkedList<Notification> notifications = new LinkedList<>();

    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void render() {
        float fuck = 0;
        for (Notification not : notifications) {
            if (not.hasReached) {
                continue;
            }

            not.setOffset(fuck);

            if (not.getPosition().equals(Notification.Position.BOTTOM))
                fuck -= 20;
            else
                fuck += 20;

            not.render();
        }
    }
}