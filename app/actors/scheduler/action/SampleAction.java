package actors.scheduler.action;

import actors.scheduler.messages.SchedulerCommand;

public class SampleAction extends BaseActionActor {

    @Override
    public void executeCommand(SchedulerCommand command) {

        Logger.info("Actor started...");
        try {
            Thread.sleep(10000);

        } catch (InterruptedException e) {
            Logger.info("Actor interrupted...");
            throw new RuntimeException(e);
        }
        finally {
            Logger.info("Actor done...");
        }
    }

}
