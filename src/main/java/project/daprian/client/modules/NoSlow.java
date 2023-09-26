package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;
import project.daprian.client.events.MotionEvent;
import project.daprian.client.events.NoSlowEvent;
import project.daprian.client.events.PacketEvent;
import project.daprian.client.events.UpdateEvent;
import project.daprian.systems.event.State;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;

@Module.Info(name = "NoSlow", category = Category.Movement,description = "blocking and walking", bindable = true)
public class NoSlow extends Module {

    Setting<Modes> modes = Setting.create(modesSettingBuilder -> modesSettingBuilder.setValues("Modes", Modes.Vanilla));

    @Listen
    public void onNoslow(NoSlowEvent event) {
        if (!mc.thePlayer.isUsingItem() || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
        switch (modes.getValue()) {
            case NCP:
            case NCP2:
            case Vanilla:
                event.setForwardMultiplier(1);
                event.setForwardMultiplier(1);
                event.setCancelled(true);
                break;
        }
    }
    @Listen
    public void onMotion(MotionEvent event){
        if (!mc.thePlayer.isUsingItem() || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
        switch (modes.getValue()){

        }
    }
    @Listen
    public void onUpdate(UpdateEvent event) {
        if (!mc.thePlayer.isUsingItem() || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
        switch (modes.getValue()) {
        }
    }
    @Listen
    public void onPacket(PacketEvent event) {
        if (!mc.thePlayer.isUsingItem() || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
        switch (modes.getValue()) {
        }
    }

    private enum Modes {Vanilla, NCP, NCP2}
}
