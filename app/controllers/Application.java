package controllers;

import actors.scheduler.action.SampleAction;
import actors.scheduler.messages.SchedulerCommand;
import actors.scheduler.kernel.SchedulerKernel;
import actors.scheduler.messages.Target;
import actors.scheduler.messages.ActionType;
import akka.actor.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class Application extends Controller {

    public static ActorRef schedulerKernel;
    private final ActorSystem system;

    @Inject
    public Application(ActorSystem system) {
        this.system = system;
        schedulerKernel = system.actorOf(SchedulerKernel.props(), "smart-scheduler");
    }

    public Result index() {
        return ok(index.render());
    }

    public Result newEvent(Long linkId, String structureId, String actionType) {

        Target target = new Target(linkId, structureId, ActionType.valueOf(actionType));
        SchedulerCommand command = SchedulerCommand.delayed(SampleAction.class, target, Duration.ofSeconds(19));

        schedulerKernel.tell(command, ActorRef.noSender());

        return ok("command is scheduled...");
    }

    public Result listState() {

        Map<Target, ActorRef> currentScheduledActors = SchedulerKernel.listScheduledActors();

        List<String> list = new ArrayList<>();

        currentScheduledActors.entrySet().stream().forEach(entry -> {
            list.add(entry.getKey() + " -> " + entry.getValue().path());
        });

        return ok(Json.toJson(list));
    }

}
