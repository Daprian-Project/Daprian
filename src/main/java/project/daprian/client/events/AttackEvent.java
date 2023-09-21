package project.daprian.client.events;

import lombok.Getter;
import net.minecraft.entity.Entity;
import project.daprian.systems.event.Event;

@Getter
public class AttackEvent extends Event {
    private final Entity entity;

    public AttackEvent(Entity entity) {
        this.entity = entity;
    }
}