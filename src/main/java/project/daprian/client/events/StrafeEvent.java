package project.daprian.client.events;

import lombok.Getter;
import lombok.Setter;
import project.daprian.systems.event.Cancellable;

@Getter
@Setter
public class StrafeEvent extends Cancellable {
    private float forward, strafe;
    private float friction, attributeSpeed;
    private float yaw;

    public StrafeEvent(float forward, float strafe, float friction, float attributeSpeed, float yaw) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.attributeSpeed = attributeSpeed;
        this.yaw = yaw;
    }
}