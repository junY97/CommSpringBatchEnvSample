package core.util;

import core.AbstractItemJobDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author junyeong.jo .
 * @since 2025-06-17
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchFileHandlerService {

    // 1. Job 정의
    @Bean
    public Job dynamicFileJob(JobRepository jobRepository, Step dynamicFileStep) {
        return new JobBuilder("dynamicFileJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // Job을 반복 실행할 수 있도록 설정
                .start(dynamicFileStep)
                .build();
    }

    // 2. Step 정의
    @Bean
    public Step dynamicFileStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                ItemReader<String> dynamicFileReader, // 3번에서 만든 @StepScope Reader를 주입받음
                                ItemProcessor<String, String> simpleProcessor,
                                ItemWriter<String> logWriter) {
        return new StepBuilder("dynamicFileStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(dynamicFileReader)
                .processor(simpleProcessor)
                .writer(logWriter)
                .build();
    }

    // 3. ItemReader 정의 (@StepScope 적용)
    @Bean
    @StepScope // ✨ 핵심: 이 Bean은 Step 실행 범위 내에서만 생성된다.
    public FlatFileItemReader<String> dynamicFileReader(
            // ✨ 핵심: JobParameters에서 'fileName'이라는 key로 넘어온 값을 주입받는다.
            @Value("#{jobParameters['fullPathFileName']}") String fullPathFileName
    ) {
        String cleanedFileName = fullPathFileName.trim();

        // 2. 앞뒤에 큰따옴표가 있으면 제거
        if (cleanedFileName.startsWith("\"") && cleanedFileName.endsWith("\"")) {
            cleanedFileName = cleanedFileName.substring(1, cleanedFileName.length() - 1);
        }

        log.info(">>>>>> 원본 파일명 (Original): {}", fullPathFileName);
        log.info(">>>>>> 정리된 파일명 (Cleaned): {}", cleanedFileName);

        return new FlatFileItemReaderBuilder<String>()
                .name("dynamicFileReader")
                // ✨ 핵심: 주입받은 fileName으로 리소스를 생성한다.
                .resource(new FileSystemResource(cleanedFileName))
                .encoding("UTF-8")
                .lineMapper(new PassThroughLineMapper()) // 각 줄을 그대로 String으로 읽어옴
                .build();
    }

    // 4. 간단한 Processor와 Writer 정의
    @Bean
    public ItemProcessor<String, String> simpleProcessor() {
        // 읽어온 데이터를 대문자로 변경하는 간단한 로직
        return item -> {
            log.info("Processing item: {}", item);
            return item.toUpperCase();
        };
    }

    @Bean
    public ItemWriter<String> logWriter() {
        // 처리된 데이터를 로그로 출력
        return items -> log.info("<<<<<< Writing items: {}", items);
    }

}
