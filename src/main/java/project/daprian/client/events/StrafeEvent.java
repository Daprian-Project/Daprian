package project.daprian.client.events;

import lombok.Getter;
import lombok.Setter;
import project.daprian.systems.event.Event;

@Getter
@Setter
public class StrafeEvent extends Event {
    float strafe, forward, friction, yaw;

    public StrafeEvent(float strafe, float forward, float friction, float yaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }
}