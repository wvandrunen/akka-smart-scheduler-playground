package actors.scheduler.messages;

import java.time.Duration;

public class SchedulerCommand<T> {

    public static <Y> SchedulerCommand<Y> immediate(Class clazz, Target target) {
       return new SchedulerCommand<Y>(clazz, target, 0, null);
    }

    public static <Y> SchedulerCommand<Y> immediate(Class clazz, Target target, Y data) {
        return new SchedulerCommand<Y>(clazz, target, 0, data);
    }

    public static <Y> SchedulerCommand<Y> delayed(Class clazz, Target target, Duration delay) {
        return new SchedulerCommand<Y>(clazz, target, delay.getSeconds(), null);
    }

    public static <Y> SchedulerCommand<Y> delayed(Class clazz, Target target, Duration delay, Y data) {
        return new SchedulerCommand<Y>(clazz, target, delay.getSeconds(), data);
    }

    public final Class clazz;
    public final Target target;
    public final long delay;
    public final T data;

    private SchedulerCommand(Class clazz, Target target, long delay, T data) {
        this.target = target;
        this.delay = delay;
        this.data = data;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "SchedulerCommand{" +
                ", target=" + target +
                ", clazz=" + clazz.getName() +
                '}';
    }
}
