package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.daprian.systems.event.Cancellable;
import project.daprian.systems.event.State;

@AllArgsConstructor @Getter
public class UpdateEvent extends Cancellable {
    private State state;
}