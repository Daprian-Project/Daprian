package project.daprian.utility.rotation;

import lombok.Getter;

@Getter
public class FixedRotations {
    private final Rotations currentRotations;
    private final Rotations lastRotations;

    public FixedRotations(Rotations startingRotations) {
        currentRotations = new Rotations(startingRotations.getYaw(), startingRotations.getPitch());
        lastRotations = new Rotations(startingRotations.getYaw(), startingRotations.getPitch());
    }

    public void updateRotations(Rotations requestedRotations) {
        lastRotations.setYaw(currentRotations.getYaw());
        lastRotations.setPitch(currentRotations.getPitch());

        float gcd = Rotations.getGCD();

        float yawDiff = (requestedRotations.getYaw() - currentRotations.getYaw());
        float pitchDiff = (requestedRotations.getPitch() - currentRotations.getPitch());

        float fixedYawDiff = yawDiff - (yawDiff % gcd);
        float fixedPitchDiff = pitchDiff - (pitchDiff % gcd);

        currentRotations.setYaw(currentRotations.getYaw() + fixedYawDiff);

        float pitch = currentRotations.getPitch() + fixedPitchDiff;
        currentRotations.setPitch(Math.max(-90, Math.min(90, pitch)));
    }
}