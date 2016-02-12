package actors.scheduler;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public abstract class BaseActor extends UntypedActor {
    protected LoggingAdapter Logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {

    }
}
