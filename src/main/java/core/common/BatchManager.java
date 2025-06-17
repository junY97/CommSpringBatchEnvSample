package core.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchManager {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final ApplicationContext applicationContext;

    public JobExecution runJob(String jobName, JobParametersBuilder jobParametersBuilder) {
        try {
            Map<String, Job> jobsMap = applicationContext.getBeansOfType(Job.class);
            Job jobToRun = jobsMap.values().stream()
                    .filter(job -> jobName.equals(job.getName()))
                    .findFirst()
                    .orElseThrow(() -> {
                        String availableJobsInfo = jobsMap.entrySet().stream()
                                .map(entry -> String.format("'%s' (bean: '%s')", entry.getValue().getName(), entry.getKey()))
                                .collect(Collectors.joining(", "));
                        if (availableJobsInfo.isEmpty()) {
                            availableJobsInfo = "No jobs found in application context.";
                        }
                        log.warn("Job with name '{}' not found. Available jobs: [{}]", jobName, availableJobsInfo);
                        return new NoSuchElementException(
                                String.format("Job with name '%s' not found. Available jobs: [%s]", jobName, availableJobsInfo)
                        );
                    });

            jobParametersBuilder.addLong("timestamp", System.currentTimeMillis());

            JobExecution jobExecution = jobLauncher.run(jobToRun, jobParametersBuilder.toJobParameters());
            log.info("Job '{}' launched successfully. JobExecutionId: {}, Status: {}",
                    jobName, jobExecution.getId(), jobExecution.getStatus());
            return jobExecution;

        } catch (NoSuchElementException e) {
            // 로그는 orElseThrow 내부에서 이미 처리되었으므로, 여기서는 그대로 예외를 다시 던집니다.
            throw e;
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Spring Batch specific error while running job '{}': {}", jobName, e.getMessage(), e);
            throw new RuntimeException("Failed to run job '" + jobName + "' due to Spring Batch execution issue: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to run job '{}': {}", jobName, e.getMessage(), e);
            throw new RuntimeException("Failed to run job '" + jobName + "'. Cause: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getJobResult(Long jobExecutionId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);

        if (jobExecution == null) {
            throw new IllegalArgumentException("JobExecution ID " + jobExecutionId + "를 찾을 수 없습니다.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("jobName", jobExecution.getJobInstance().getJobName());
        result.put("status", jobExecution.getStatus());
        result.put("exitStatus", jobExecution.getExitStatus().getExitCode());
        result.put("startTime", jobExecution.getStartTime());
        result.put("endTime", jobExecution.getEndTime());

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            // Job이 성공적으로 완료되었으면, Step의 ExecutionContext에서 저장된 데이터를 꺼냅니다.
            // 이 부분은 특정 Job의 Step 결과에 따라 달라질 수 있습니다.
            // 여기서는 첫 번째 Step의 결과를 가져오는 것으로 가정합니다.
            jobExecution.getStepExecutions().stream()
                    .findFirst()
                    .ifPresent(stepExecution -> {
                        ExecutionContext stepContext = stepExecution.getExecutionContext();
                        if (stepContext.containsKey("vipUsernames")) { // "vipUsernames" 키가 있는지 확인
                            result.put("resultData", stepContext.get("vipUsernames"));
                        } else {
                            log.warn("Key 'vipUsernames' not found in ExecutionContext for jobExecutionId: {}", jobExecutionId);
                            result.put("resultData", "No 'vipUsernames' found in step context.");
                        }
                    });
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            result.put("errors", jobExecution.getAllFailureExceptions());
        }

        return result;
    }
}