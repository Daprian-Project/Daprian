package project.daprian.systems.event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Cancellable extends Event {
    private boolean cancelled;

    public void Cancel() {
        cancelled = true;
    }

    public void Cycle() {
        cancelled = !cancelled;
    }
}