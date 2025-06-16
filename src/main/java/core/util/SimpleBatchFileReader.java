package core.util;

import core.AbstractItemJobDefinition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author junyeong.jo .
 * @since 2025-06-16
 */
@Deprecated
@Component
public class SimpleBatchFileReader extends AbstractItemJobDefinition<String, String> {

    @Setter
    String filePath = "";

    @JobScope
    @Bean
    public Job fileReadJob(JobRepository jobRepository, Step sampleStep) {
        return super.job("fileReadJob", jobRepository, sampleStep);
    }

    @StepScope
    @Bean
    public Step fileReadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return super.step("fileReadStep", jobRepository, transactionManager);
    }

    @Override
    protected ItemReader<String> reader() {
        return new FlatFileItemReaderBuilder<String>()
                .name("fileReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper(new PassThroughLineMapper())
                .encoding("UTF-8")
                .build();
    }

    @Override
    protected ItemProcessor<String, String> processor() {
        return null;
    }

    @Override
    protected ItemWriter<String> writer() {
        return null;
    }



}




