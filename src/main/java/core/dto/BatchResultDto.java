package core.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author junyeong.jo .+++++++++++++++++++++++++++++++++++++++++
 * @since 2025-06-16
 */
@Data
public class BatchResultDto {
    private String jobName;
    private Long jobExecutionId;
    private String status; // 예: "COMPLETED", "FAILED"
    private String exitCode; // 예: "COMPLETED", "NOOP", "FAILED"
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMillis;

    private long readCount;
    private long writeCount;
    private long filterCount; // Processor에서 필터링된 건수
    private long skipCount;   // Read, Process, Write 단계에서 스킵된 총 건수

    private boolean hasErrors = false;
    private List<String> errorMessages;

    private String summaryMessage; // 결과 요약 메시지
}
