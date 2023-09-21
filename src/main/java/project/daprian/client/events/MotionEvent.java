package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.daprian.systems.event.Event;
import project.daprian.systems.event.State;

@Getter @Setter
public class MotionEvent extends Event {
    private final State direction;
    private float yaw;
    private float pitch;
    private float prevYaw;
    private float prevPitch;
    private double x, y, z;
    private boolean onGround;

    public MotionEvent(State direction, double x, double y, double z, float yaw, float pitch, float prevYaw, float prevPitch, boolean onGround) {
        this.direction = direction;
        this.yaw = yaw;
        this.pitch = pitch;
        this.prevYaw = prevYaw;
        this.prevPitch = prevPitch;
        this.onGround = onGround;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}