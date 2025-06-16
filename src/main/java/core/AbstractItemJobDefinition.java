package core;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import org.springframework.transaction.PlatformTransactionManager;

public abstract class AbstractItemJobDefinition<I, O> {

    protected abstract ItemReader<I> reader();
    protected abstract ItemProcessor<I, O> processor();
    protected abstract ItemWriter<O> writer();

    /**
     * JobRepository와 Step Bean을 Spring DI 컨테이너로부터 직접 주입받습니다.
     */

    protected Job job(String jobName, JobRepository jobRepository, Step step) {
        return new JobBuilder(jobName, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    /**
     * JobRepository와 PlatformTransactionManager를 직접 주입받습니다.
     * 자식 클래스가 구현한 Reader, Processor, Writer를 자동으로 연결합니다.
     */
    protected Step step(String stepName, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(stepName, jobRepository)
                .<I, O>chunk(100, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}