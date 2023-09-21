package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.client.Minecraft;
import project.daprian.client.events.TickEvent;
import project.daprian.client.events.UpdateEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;

@Module.Info(name = "Test", category = Category.Movement)
public class TestThing extends Module {

    private int ticks;

    @Listen
    public void onUpdate(UpdateEvent event) {
        if (ticks == 2) {
            event.setCancelled(true);

            Minecraft.getMinecraft().thePlayer.motionZ = -Minecraft.getMinecraft().thePlayer.motionZ;
            Minecraft.getMinecraft().thePlayer.motionX = -Minecraft.getMinecraft().thePlayer.motionX;

            ticks = 0;
        }
    }

    @Listen
    public void onTicks(TickEvent event) {
        if (event.isInWorld() && Minecraft.getMinecraft().thePlayer.hurtTime > 0) {
            ticks++;
        }
    }

}