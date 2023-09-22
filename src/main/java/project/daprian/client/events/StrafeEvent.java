package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.daprian.systems.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class StrafeEvent extends Event {
    private float forward, strafe;
    private float friction, attributeSpeed;
    private float yaw;
}