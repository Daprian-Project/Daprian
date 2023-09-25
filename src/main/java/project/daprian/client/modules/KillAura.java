package project.daprian.client.modules;

import io.github.nevalackin.radbus.Listen;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Keyboard;
import project.daprian.client.events.JumpEvent;
import project.daprian.client.events.MotionEvent;
import project.daprian.client.events.StrafeEvent;
import project.daprian.systems.event.State;
import project.daprian.systems.module.Category;
import project.daprian.systems.module.Module;
import project.daprian.systems.setting.Setting;
import project.daprian.utility.MathUtil;
import project.daprian.utility.MovementUtil;
import project.daprian.utility.TimeUtil;
import project.daprian.utility.rotation.Angle;
import project.daprian.utility.rotation.FixedRotations;
import project.daprian.utility.rotation.Rotations;

import java.time.Duration;
import java.util.Random;

@Module.Info(name = "KillAura", category = Category.Combat, bindable = true, bind = Keyboard.KEY_R)
public class KillAura extends Module {

    private final Setting<Double> range = Setting.create(setting -> setting.setValues("Range", 4D, 3D, 6D, .1));
    private final Setting<Double> blockRange = Setting.create(setting -> setting.setValues("Block Range", 4D, 3D, 6D, .1));
    private final Setting<Double> attackRange = Setting.create(setting -> setting.setValues("Attack Range", 4D, 3D, 6D, .1));
    private final Setting<Integer> cps = Setting.create(setting -> setting.setValues("CPS", 15, 5, 25, 1));
    private final Setting<RotsModes> rotsModes = Setting.create(setting -> setting.setValues("Rotations", RotsModes.Normal));
    private final Setting<subRotsModes> subRotations = Setting.create(setting -> setting.setValues("SubRotations", subRotsModes.Normal)
            .setVisible(() -> rotsModes.getValue().equals(RotsModes.Jitter)));
    private final Setting<Integer> jitterRandomValue = Setting.create(setting -> setting.setValues("Jitter Value", 10, 1, 50, 1)
            .setVisible(() -> (rotsModes.getValue().equals(RotsModes.Jitter) || rotsModes.getValue().equals(RotsModes.Testing))));
    private final Setting<AttackMode> attackMode = Setting.create(setting -> setting.setValues("Attack Mode", AttackMode.Pre));
    private final Setting<AttackType> attackType = Setting.create(setting -> setting.setValues("Attack Type", AttackType.Player));
    private final Setting<Boolean> strafeFix = Setting.create(setting -> setting.setValues("Strafe Fix", false));

    private EntityLivingBase currentTarget;
    private Rotations currentRotations;
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

        blocking = false;

        setSuffix(() -> "");

        if (currentTarget == null) return;

        setSuffix(() -> String.format("%s (%s %s)", currentTarget.getName(), MathUtil.roundDecimalPlaces(currentTarget.getHealth(), 2), currentTarget.hurtTime));

        float distanceToEntity = currentTarget.getDistanceToEntity(mc.thePlayer);
        blocking = distanceToEntity <= blockRange.getValue();

        switch (rotsModes.getValue()) {
            case Normal:
                currentRotations = Angle.calcRotationToEntity(currentTarget);
                event.setYaw(currentRotations.getYaw());
                event.setPitch(currentRotations.getPitch());
                rotate(event);
                break;

            case Smooth:
                currentRotations = Angle.smoothRotations(currentTarget, currentRotations);
                event.setYaw(currentRotations.getYaw());
                event.setPitch(currentRotations.getPitch());
                rotate(event);
                break;

            case Jitter:
                currentRotations = subRotations(currentTarget, currentRotations);

                currentRotations.setYaw(currentRotations.getYaw() + new Random().nextInt(jitterRandomValue.getValue()));
                currentRotations.setPitch(currentRotations.getPitch() + new Random().nextInt(jitterRandomValue.getValue()));

                event.setYaw(currentRotations.getYaw());
                event.setPitch(currentRotations.getPitch());
                rotate(event);
                break;
        }

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
            event.setYaw(Angle.calcRotationToEntity(currentTarget).getYaw());
        }
    }

    @Listen
    public void onJump(JumpEvent event) {
        if (strafeFix.getValue() && currentTarget != null) {
            event.setYaw(Angle.calcRotationToEntity(currentTarget).getYaw());
        }
    }

    private void Attack() {
        switch (attackType.getValue()) {
            case Packet:
                mc.thePlayer.swingItem();
                mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(currentTarget, C02PacketUseEntity.Action.ATTACK));
                break;
            case Player:
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, currentTarget);
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

    private Rotations subRotations(EntityLivingBase currentTarget, Rotations currentRotations) {
        switch (subRotations.getValue()) {
            case Normal:
                currentRotations = Angle.calcRotationToEntity(currentTarget);
                break;
            case Smooth:
                currentRotations = Angle.smoothRotations(currentTarget, currentRotations);
                break;
        }
        return currentRotations;
    }

    private void rotate(MotionEvent event) {
        event.setYaw(currentRotations.getYaw());
        event.setPitch(currentRotations.getPitch());

        mc.thePlayer.rotationPitchHead = currentRotations.getPitch();
        mc.thePlayer.rotationYawHead = currentRotations.getYaw();
        mc.thePlayer.renderYawOffset = currentRotations.getYaw();
    }

    private enum RotsModes {Normal, Smooth, Jitter, Testing}
    private enum subRotsModes {Normal, Smooth}
    private enum AttackMode {Pre, Post, Both}
    private enum AttackType {Player, Packet}
}