package demo.biz.batch;

import core.AbstractTaskJobDefinition;

import demo.biz.dto.SampleDto;
import demo.biz.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class SampleJobStep extends AbstractTaskJobDefinition {

    private final SampleMapper sampleMapper;

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
    protected Tasklet tasklet()  {
        return (contribution, chunkContext) -> {
            // 여기에 실제 비지니스 로직을 구현
            List<SampleDto> sampleDto = sampleMapper.selectDummy();
            sampleMapper.insertHardcodedDummyData();
            log.info(sampleDto.get(0).getSampleName());
            log.info(sampleDto.get(1).getSampleName());


            return RepeatStatus.FINISHED;
        };
    }


}
