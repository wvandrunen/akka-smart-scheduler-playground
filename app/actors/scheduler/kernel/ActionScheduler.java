package actors.scheduler.kernel;

import actors.scheduler.BaseActor;
import actors.scheduler.messages.SchedulerCommand;
import akka.actor.Cancellable;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.Creator;
import play.libs.F;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.escalate;

public class ActionScheduler extends BaseActor {

    public static Props props(SchedulerCommand command) {

        return Props.create(new Creator<ActionScheduler>() {
            private static final long serialVersionUID = 1L;

            @Override
            public ActionScheduler create() throws Exception {
                return new ActionScheduler(command);
            }
        });
    }

    private Cancellable cancellable;
    private final SchedulerCommand command;

    public ActionScheduler(SchedulerCommand command) {
        this.command = command;
    }

    @Override
    public void preStart() throws Exception {
        cancellable = getContext()
                .system()
                .scheduler()
                .scheduleOnce(
                        Duration.create(command.delay, TimeUnit.SECONDS),
                        getContext().actorOf(Props.create(command.clazz)),
                        command,
                        getContext().dispatcher(), getSelf());
    }

    @Override
    public void postStop() throws Exception {
        Logger.info("Stopping this ActionScheduler for {}", command.target);
        cancellable.cancel();
    }


    @Override
    public SupervisorStrategy supervisorStrategy() {
        return ActionScheduler.ESCLATE_ALWAYS;
    }

    public static SupervisorStrategy ESCLATE_ALWAYS = new OneForOneStrategy(0, Duration.Zero(), t -> escalate());
}
