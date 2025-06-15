package core.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchExecutionService {

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final ApplicationContext applicationContext;

    public JobExecution runJob(String jobName, Map<String, Object> jobParametersMap) {
        try {
            Job jobToRun = applicationContext.getBean(jobName, Job.class);
            JobParametersBuilder builder = new JobParametersBuilder();
            return jobLauncher.run(jobToRun, builder.toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getJobResult(Long jobExecutionId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);

        if (jobExecution == null) {
            throw new IllegalArgumentException("JobExecution ID " + jobExecutionId + "를 찾을 수 없습니다.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("jobName", jobExecution.getJobInstance().getJobName());
        result.put("status", jobExecution.getStatus()); // Job의 현재 상태 (COMPLETED, FAILED, RUNNING...)
        result.put("exitStatus", jobExecution.getExitStatus().getExitCode());
        result.put("startTime", jobExecution.getStartTime());
        result.put("endTime", jobExecution.getEndTime());

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            // Job이 성공적으로 완료되었으면, Step의 ExecutionContext에서 저장된 데이터를 꺼냅니다.
            ExecutionContext stepContext = jobExecution.getStepExecutions().iterator().next().getExecutionContext();
            result.put("resultData", stepContext.get("vipUsernames"));
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            result.put("errors", jobExecution.getAllFailureExceptions());
        }

        return result;
    }
}