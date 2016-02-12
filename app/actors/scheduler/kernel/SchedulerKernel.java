package actors.scheduler.kernel;

import actors.scheduler.BaseActor;
import actors.scheduler.messages.SchedulerCommand;
import actors.scheduler.messages.SchedulerResult;
import actors.scheduler.messages.Target;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SchedulerKernel extends BaseActor {

    private static ConcurrentHashMap<Target, ActorRef> scheduleRegistry = new ConcurrentHashMap<>();

    public static Props props() {
        return Props.create(SchedulerKernel.class);
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof SchedulerCommand) {

            SchedulerCommand command = (SchedulerCommand) message;

            tryCancel(command.target);
            schedule(command);

        } else if(message instanceof SchedulerResult) {

            SchedulerResult result = (SchedulerResult) message;
            tryCancel(result.originalCommand.target);

        } else {
            unhandled(message);
        }

    }

    private void tryCancel(Target target) {
        Stream.of(scheduleRegistry.remove(target))
            .filter(actorRef -> {
                boolean hasActor = actorRef != null;
                Logger.info("An actor is found in the registry: {}", hasActor);
                return hasActor;
            })
            .filter(actorRef -> {
                boolean isTerminated = actorRef.isTerminated();
                Logger.info("An actor is found in the registry but is already terminated: {}", isTerminated);
                return !isTerminated;
            })
            .forEach(actorRef -> {
                actorRef.tell(PoisonPill.getInstance(), ActorRef.noSender());
                Logger.info("Terminating {}", actorRef);
            });
    }

    private void schedule(SchedulerCommand command) {
        scheduleRegistry.put(
                command.target,
                getContext().actorOf(ActionScheduler.props(command)));
    }


    @Override
    public SupervisorStrategy supervisorStrategy() {
        return SupervisorStrategy.stoppingStrategy();
    }

    public static Map<Target, ActorRef> listScheduledActors() {
        return new HashMap<>(scheduleRegistry);
    }

}
