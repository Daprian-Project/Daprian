package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import project.daprian.client.events.JumpEvent;
import project.daprian.client.events.MotionEvent;
import project.daprian.client.events.StrafeEvent;
import project.daprian.systems.event.State;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.TimeUtil;

import java.time.Duration;

@Module.Info(name = "KillAura", category = Category.Combat)
public class KillAura extends Module {

    private final Setting<Double> range = Setting.create(setting -> setting.setValues("Range", 4D, 3D, 6D, .1));
    private final Setting<Double> blockRange = Setting.create(setting -> setting.setValues("Block Range", 4D, 3D, 6D, .1));
    private final Setting<Double> attackRange = Setting.create(setting -> setting.setValues("Attack Range", 4D, 3D, 6D, .1));
    private final Setting<Integer> cps = Setting.create(setting -> setting.setValues("CPS", 15, 5, 25, 1));
    private final Setting<AttackMode> attackMode = Setting.create(setting -> setting.setValues("Attack Mode", AttackMode.Pre));
    private final Setting<AttackType> attackType = Setting.create(setting -> setting.setValues("Attack Type", AttackType.Player));
    private final Setting<Boolean> strafeFix = Setting.create(setting -> setting.setValues("Strafe Fix", false));

    private EntityLivingBase currentTarget;
    private final TimeUtil stopwatch;
    @Getter
    private boolean blocking;

    public KillAura() {
        stopwatch = new TimeUtil();
        stopwatch.reset();
    }

    @Listen
    public void onMotion(MotionEvent event) {
        currentTarget = getClosest();

        if (currentTarget == null) return;

        float distanceToEntity = currentTarget.getDistanceToEntity(mc.thePlayer);
        blocking = distanceToEntity <= blockRange.getValue();

        if (distanceToEntity <= attackRange.getValue()) {
            if (stopwatch.hasReached(Duration.ofMillis(1000 / cps.getValue()))) {
                switch (attackMode.getValue()) {
                    case Pre:
                        if (event.getDirection().equals(State.Pre))
                            Attack();
                        break;
                    case Post:
                        if (event.getDirection().equals(State.Post))
                            Attack();
                        break;
                    case Both:
                        Attack();
                        break;
                }
                stopwatch.reset();
            }
        }
    }

    @Listen
    public void onStrafe(StrafeEvent event) {
        if (strafeFix.getValue() && currentTarget != null) {
            // TODO
        }
    }

    @Listen
    public void onJump(JumpEvent event) {
        if (strafeFix.getValue() && currentTarget != null) {
            // TODO
        }
    }

    private void Attack() {
        switch (attackType.getValue()) {
            case Packet:
                mc.thePlayer.swingItem();
                System.out.println("Attacking packet: " + currentTarget.getName());
                break;
            case Player:
                mc.thePlayer.swingItem();
                System.out.println("Attacking player: " + currentTarget.getName());
                break;
        }
    }

    public EntityLivingBase getClosest() {
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e.getDistanceToEntity(mc.thePlayer) <= range.getValue() && !e.isDead && e != mc.thePlayer && e instanceof EntityPlayer) {
                if (!mc.thePlayer.canEntityBeSeen(e))
                    continue;

                return (EntityLivingBase) e;
            }
        }
        return null;
    }

    private enum AttackMode { Pre, Post, Both }
    private enum AttackType { Player, Packet }
}