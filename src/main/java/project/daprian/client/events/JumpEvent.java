package project.daprian.client.events;

import lombok.Getter;
import lombok.Setter;
import project.daprian.systems.event.Event;

@Getter @Setter
public class JumpEvent extends Event {
    double motion;
    float yaw;
    boolean sprinting;

    public JumpEvent(double motion, float yaw, boolean sprinting) {
        this.motion = motion;
        this.yaw = yaw;
        this.sprinting = sprinting;
    }
}