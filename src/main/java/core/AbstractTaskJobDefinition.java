package core;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractTaskJobDefinition {

    protected abstract Tasklet tasklet();

    /**
      * Job 객체를 생성하는 공통 메서드.
      * @param jobName 자식 클래스에서 직접 전달받은 Job의 이름
      * @param jobRepository Spring DI 컨테이너로부터 주입받은 JobRepository
      * @param step 아래 step() 메서드가 생성한 Step 객체
      * @return 완성된 Job 객체
      */
    protected Job job(String jobName, JobRepository jobRepository, Step step) {
        return new JobBuilder(jobName, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    protected Step step(String stepName, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(stepName, jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();
    }
}
