package project.daprian.client.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.ScaledResolution;
import project.daprian.systems.event.Event;

@Getter @AllArgsConstructor
public class RenderUIEvent extends Event {
    private ScaledResolution scaledResolution;
    private float partialTicks;
}