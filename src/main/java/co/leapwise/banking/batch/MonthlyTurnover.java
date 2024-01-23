package co.leapwise.banking.batch;

import co.leapwise.banking.model.Account;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MonthlyTurnover {
  private final EntityManagerFactory entityManagerFactory;
  private final MonthlyTurnoverProcessor processor;

  @Bean
  public Job monthlyTurnoverJob(JobRepository jobRepository, Step monthlyTurnoverStep) {
    return new JobBuilder("monthlyTurnoverJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(monthlyTurnoverStep)
        .build();
  }

  @Bean
  public Step monthlyTurnoverStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("monthlyTurnoverStep", jobRepository)
        .<Account, Account>chunk(10, transactionManager)
        .reader(monthlyTurnoverReader())
        .processor(processor)
        .writer(monthlyTurnoverWriter())
        .build();
  }

  @Bean
  public ItemReader<Account> monthlyTurnoverReader() {
    var reader = new JpaPagingItemReader<Account>();
    reader.setQueryString("SELECT a FROM Account a");
    reader.setEntityManagerFactory(entityManagerFactory);
    return reader;
  }

  @Bean
  public ItemWriter<Account> monthlyTurnoverWriter() {
    var writer = new JpaItemWriter<Account>();
    writer.setEntityManagerFactory(entityManagerFactory);
    return writer;
  }
}
