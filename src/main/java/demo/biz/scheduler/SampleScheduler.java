package demo.biz.scheduler; // 실제 패키지명으로 변경해주세요.

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Component
public class SampleScheduler {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 고정된 지연 시간(ms)을 두고 실행됩니다.
     * 이전 작업이 완료된 후 5초 뒤에 다음 작업이 실행됩니다.
     */
    @Scheduled(fixedDelay = 5000) // 5000ms = 5초
    public void scheduleTaskWithFixedDelay() {
        log.info("Fixed Delay Scheduler: 현재 시간 - {}", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        // 여기에 주기적으로 실행할 로직을 구현합니다.
        // 예: 데이터베이스 조회, 알림 발송 등
    }

    /**
     * 고정된 주기(ms)로 실행됩니다.
     * 이전 작업의 완료 여부와 관계없이 10초마다 실행됩니다.
     * 만약 작업 실행 시간이 주기보다 길면, 작업이 끝난 직후 다음 작업이 실행됩니다.
     */
    @Scheduled(fixedRate = 10000) // 10000ms = 10초
    public void scheduleTaskWithFixedRate() {
        log.info("Fixed Rate Scheduler: 현재 시간 - {}", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        // 여기에 주기적으로 실행할 로직을 구현합니다.
        try {
            // 예시로 2초간 대기
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("Fixed Rate Scheduler 작업 중 오류 발생", e);
            Thread.currentThread().interrupt(); // 인터럽트 상태를 다시 설정
        }
        log.info("Fixed Rate Scheduler: 작업 완료 - {}", LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    /**
     * Cron 표현식을 사용하여 특정 시간에 실행됩니다.
     * 아래 예시는 매분 0초에 실행됩니다 (예: 10:00:00, 10:01:00).
     * Cron 표현식: 초 분 시 일 월 요일 (년도 - 선택 사항)
     * "0 * * * * ?" : 매분 0초
     * "0 0 12 * * ?" : 매일 12시 0분 0초 (정오)
     * "0 0/5 14 * * ?" : 매일 오후 2시부터 5분 간격으로 실행 (2:00, 2:05, 2:10...)
     */
    @Scheduled(cron = "0 * * * * ?") // 매분 0초에 실행
    public void scheduleTaskWithCronExpression() {
        log.info("Cron Scheduler: 현재 시간 - {}", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        // 여기에 특정 시간에 실행할 로직을 구현합니다.
    }

    /**
     * 애플리케이션 시작 후 초기 지연 시간(ms)을 두고, 그 이후 고정된 지연 시간(ms)으로 실행됩니다.
     * 애플리케이션 시작 후 3초 뒤에 첫 실행, 그 이후 7초 간격으로 실행됩니다.
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 7000) // 초기 지연 3초, 이후 7초 간격
    public void scheduleTaskWithInitialDelay() {
        log.info("Initial Delay Scheduler: 현재 시간 - {}", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        // 여기에 주기적으로 실행할 로직을 구현합니다.
    }
}