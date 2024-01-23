package co.leapwise.banking.batch;

import co.leapwise.banking.common.Properties;
import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonthlyTurnoverTask extends RecurringTask<Void> {
  public static final String TASK_NAME = "MonthlyTurnoverTask";

  private final JobLauncher jobLauncher;
  private final Job monthlyTurnoverJob;

  public MonthlyTurnoverTask(
      JobLauncher jobLauncher, Job monthlyTurnoverJob, Properties properties) {
    super(TASK_NAME, Schedules.cron(properties.monthlyTurnoverJobCron()), Void.class);
    this.jobLauncher = jobLauncher;
    this.monthlyTurnoverJob = monthlyTurnoverJob;
  }

  @Override
  public void executeRecurringly(
      TaskInstance<Void> taskInstance, ExecutionContext executionContext) {
    log.debug("Starting {}", TASK_NAME);

    try {
      jobLauncher.run(monthlyTurnoverJob, jobParameters());
    } catch (JobExecutionException e) {
      log.error("Error starting job", e);
    }

    log.debug("Ending {}", TASK_NAME);
  }

  public static JobParameters jobParameters() {
    return new JobParametersBuilder().toJobParameters();
  }
}
