package demo.biz.batch;

import core.AbstractTaskJobDefinition;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Log4j2
public class SampleJobStep extends AbstractTaskJobDefinition {

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return super.job("sampleJob", jobRepository, sampleStep);
    }

    @Bean
    public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return super.step("sampleStep", jobRepository, transactionManager);
    }

    @Override
    @Bean
    @StepScope
    protected Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            log.info(">>>>> 30일이 지난 임시 파일 삭제 작업을 시작합니다...");

            // 여기에 실제 파일 삭제 로직을 구현
            // 예: FileUtils.deleteDirectory(new File("/path/to/temp"));

            log.info(">>>>> 임시 파일 삭제 작업이 성공적으로 완료되었습니다.");
            return RepeatStatus.FINISHED;
        };
    }


}

