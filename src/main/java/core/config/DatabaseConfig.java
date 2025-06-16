package core.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing(
        dataSourceRef = "dataSource", // 이 이름은 아래 @Bean 메서드 이름과 일치해야 합니다.
        transactionManagerRef = "transactionManager" // 이 이름은 아래 @Bean 메서드 이름과 일치해야 합니다.
)
public class DatabaseConfig {

    // application.properties 또는 application.yml 파일에서 다음 속성들을 설정해야 합니다.
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverClassName;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbDriverClassName); // MS-SQL 드라이버
        dataSource.setJdbcUrl(dbUrl); // MS-SQL 연결 URL
        dataSource.setUsername(dbUsername); // MS-SQL 사용자 이름
        dataSource.setPassword(dbPassword); // MS-SQL 비밀번호

        // HikariCP 설정을 추가할 수 있습니다 (선택 사항).
        // 예: dataSource.setMaximumPoolSize(10);
        //     dataSource.setMinimumIdle(5);
        //     dataSource.setConnectionTimeout(30000);
        //     dataSource.setIdleTimeout(600000);
        //     dataSource.setMaxLifetime(1800000);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        // DataSourceTransactionManager는 대부분의 JDBC DataSource와 호환됩니다.
        return new DataSourceTransactionManager(dataSource);
    }
}