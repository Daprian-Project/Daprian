package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.daprian.systems.event.Event;

@AllArgsConstructor @Getter
public class TickEvent extends Event {
    private boolean isInWorld;
    private int ticks;
}