package actors.scheduler.action;

import actors.scheduler.BaseActor;
import actors.scheduler.kernel.ActionScheduler;
import actors.scheduler.messages.SchedulerCommand;
import akka.actor.SupervisorStrategy;
import play.libs.F;

public abstract class BaseActionActor extends BaseActor {

    @Override
    public final void onReceive(Object message) throws Exception {
        if(message instanceof SchedulerCommand){
            SchedulerCommand command = (SchedulerCommand) message;

            F.Promise.pure(executeCommand(command)).wrapped().onComplete();
        } else {
            unhandled(message);
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return ActionScheduler.ESCLATE_ALWAYS;
    }

    public abstract F.Promise executeCommand(SchedulerCommand command);

}
