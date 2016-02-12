package actors.scheduler.messages;

public class SchedulerResult {

    public SchedulerCommand originalCommand;

    public SchedulerResult(SchedulerCommand originalCommand) {
        this.originalCommand = originalCommand;
    }
}
