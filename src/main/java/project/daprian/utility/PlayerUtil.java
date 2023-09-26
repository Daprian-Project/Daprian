package project.daprian.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class PlayerUtil {

    public static void drop(Minecraft mc, int slotIndex) {
        if (mc.thePlayer != null && mc.thePlayer.inventory != null) {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotIndex, 1, 4, mc.thePlayer);
        }
    }

    public static void click(Minecraft mc, int slotIndex, int mouseButton, boolean shiftClick) {
        Container container = mc.thePlayer.openContainer;

        if (container != null && container.getSlot(slotIndex) != null) {
            ItemStack itemStack = container.getSlot(slotIndex).getStack();

            if (itemStack != null && itemStack.stackSize > 0) {
                mc.playerController.windowClick(container.windowId, slotIndex, mouseButton, shiftClick ? 1 : 0, mc.thePlayer);
            }
        }
    }
}
