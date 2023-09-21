package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import project.daprian.systems.event.Cancellable;
import project.daprian.systems.event.State;

@AllArgsConstructor @Setter @Getter
public class PacketEvent extends Cancellable {
    private final State state;
    private Packet packet;
}