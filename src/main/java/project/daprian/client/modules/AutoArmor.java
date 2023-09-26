package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import project.daprian.client.events.UpdateEvent;
import project.daprian.systems.event.State;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.PlayerUtil;
import project.daprian.utility.TimeUtil;

import java.time.Duration;

@Module.Info(name = "AutoArmor",category = Category.Player, bindable = true)
public class AutoArmor extends Module {


    private final Setting<Integer> delay = Setting.create(settings -> settings.setValues("Delay", 0, 0, 500, 1));
    private final Setting<Boolean> invOnly = Setting.create(settings -> settings.setValues("Inventory only", true));
    TimeUtil timer = new TimeUtil();

    @Listen
    public void onUpdate(UpdateEvent event) {
       if (event.getState() == State.Pre || (mc.thePlayer == null && mc.thePlayer.inventory == null)) return;

       if (invOnly.getValue() && !(mc.currentScreen instanceof GuiInventory)) return;
       if (mc.thePlayer.openContainer instanceof ContainerChest) timer.reset();

       if (timer.hasReached(Duration.ofMillis(delay.getValue().longValue()))) {
            for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
                if (equipBest(armorSlot)) {
                    timer.reset();
                    break;
                }
            }
        }
    }

    private boolean equipBest(int armorSlot) {
        int equipSlot = -1, currProt = -1;
        ItemArmor currItem = null;

        ItemStack slotStack = mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack();

        if (slotStack != null && slotStack.getItem() instanceof ItemArmor) {
            currItem = (ItemArmor) slotStack.getItem();

            currProt = currItem.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack());
        }

        for (int i = 9; i < 45; i++) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (is != null && is.getItem() instanceof ItemArmor) {
                int prot = ((ItemArmor) is.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, is);

                if ((currItem == null || currProt < prot) && isValidPiece(armorSlot, (ItemArmor) is.getItem())) {
                    currItem = (ItemArmor) is.getItem();
                    equipSlot = i;
                    currProt = prot;
                }
            }
        }
        if (equipSlot != -1) {
            if (slotStack != null) {
                PlayerUtil.drop(mc,armorSlot);
            } else {
                PlayerUtil.click(mc,equipSlot, 0, true);
            }
            return true;
        }
        return false;
    }

    private boolean isValidPiece(int armorSlot, ItemArmor item) {
        String unlocalizedName = item.getUnlocalizedName();
        return armorSlot == 5 && unlocalizedName.startsWith("item.helmet")
                || armorSlot == 6 && unlocalizedName.startsWith("item.chestplate")
                || armorSlot == 7 && unlocalizedName.startsWith("item.leggings")
                || armorSlot == 8 && unlocalizedName.startsWith("item.boots");
    }
}
