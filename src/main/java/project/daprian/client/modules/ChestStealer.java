package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import project.daprian.client.events.UpdateEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.TimeUtil;

import java.time.Duration;

@Module.Info(name = "ChestStealer",category = Category.Player,description = "Chest stealer?",bindable = true)
public class ChestStealer extends Module{

    private final Setting<Integer> delay = Setting.create(settings -> settings.setValues("Delay", 0, 0, 500, 1));
    private final Setting<Boolean> autoClose = Setting.create(settings -> settings.setValues("Auto Close",true));
    TimeUtil timer = new TimeUtil();

    @Listen
    public void onUpdate(UpdateEvent event){
        if (mc.thePlayer.openContainer != null && (mc.thePlayer.openContainer instanceof ContainerChest)) {
            ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null) {
                    ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);
                    if (this.timer.hasReached(Duration.ofMillis(delay.getValue())) && isValidItem(stack)) {
                        mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                        this.timer.reset();
                    }
                }
                if (isChestEmpty(container) && autoClose.getValue()) {
                    mc.thePlayer.closeScreen();
                }

            }

        }
    }

    private boolean isChestEmpty(ContainerChest chest) {
        for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);
            if (stack != null && this.isValidItem(stack)) {
                return false;
            }
        }

        return true;
    }
    private boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemBow || itemStack.getItem() == Items.arrow;
    }

}
