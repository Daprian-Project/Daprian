package project.daprian.utility;

import lombok.Getter;

import java.time.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class TimeUtil {

    @Getter
    private Duration elapsedTime;
    private final Timer timer;

    public TimeUtil() {
        this.elapsedTime = Duration.ZERO;

        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        this.timer.scheduleAtFixedRate(timerTask, 10, 10);
    }

    public boolean hasReached(Duration duration) {
        return this.elapsedTime.compareTo(duration) >= 0;
    }

    public void reset() {
        this.elapsedTime = Duration.ZERO;
    }

    private void update() {
        this.elapsedTime = this.elapsedTime.plus(Duration.ofMillis(10));
    }

    public void stop() {
        this.timer.cancel();
    }

    @Override
    public String toString() {
        return this.elapsedTime.toString();
    }
}