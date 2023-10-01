package project.daprian.client.modules.render;

import io.github.nevalackin.radbus.Listen;
import net.minecraft.entity.EntityLivingBase;
import project.daprian.client.Main;
import project.daprian.client.events.MotionEvent;
import project.daprian.client.events.RenderUIEvent;
import project.daprian.client.events.RenderWorldEvent;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.MovementUtil;
import project.daprian.utility.RenderUtil;

import java.awt.*;

@Module.Info(name = "ESP", category = Category.Render, bindable = false)
public class ESP extends Module {
    @Listen
    public void onRender(RenderUIEvent event) {
        if (mc.currentScreen != null) return;
        for (EntityLivingBase entity : mc.theWorld.getLivingEntities()) {
            if (entity == mc.thePlayer) continue;
            RenderUtil.drawESP(entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ)
                    .offset(interpolate(entity.lastTickPosX, entity.posX), interpolate(entity.lastTickPosY, entity.posY), interpolate(entity.lastTickPosZ, entity.posZ))
                    .offset(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ()), entity.isInvisible() ? Color.red : entity.isInvisibleToPlayer(mc.thePlayer) ? Color.yellow : Color.white, 2f);
        }
    }

    private double interpolate(double lastPos, double pos) {
        return lastPos + (pos - lastPos) * mc.timer.renderPartialTicks;
    }
}