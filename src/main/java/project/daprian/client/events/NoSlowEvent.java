package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.daprian.systems.event.Cancellable;

@Setter @Getter @AllArgsConstructor
public class NoSlowEvent extends Cancellable {

    private float strafeMultiplier, forwardMultiplier;

}
