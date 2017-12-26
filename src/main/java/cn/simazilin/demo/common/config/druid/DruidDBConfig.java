package cn.simazilin.demo.common.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-26 10:12
 * @Project: 阳光物业V1.0
 * @Description:
 */
@Configuration
public class DruidDBConfig {
    private static final Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);

    @Value("${demo.datasource.url}")
    private String dbUrl;

    @Value("${demo.datasource.username}")
    private String username;

    @Value("${demo.datasource.password}")
    private String password;

    @Value("${demo.datasource.driverClassName}")
    private String driverClassName;

    @Value("${demo.datasource.initialSize}")
    private int initialSize;

    @Value("${demo.datasource.minIdle}")
    private int minIdle;

    @Value("${demo.datasource.maxActive}")
    private int maxActive;

    @Value("${demo.datasource.maxWait}")
    private int maxWait;

    @Value("${demo.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${demo.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${demo.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${demo.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${demo.datasource.filters}")
    private String filters;

    @Value("${demo.datasource.connectionProperties}")
    private String connectionProperties;

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(connectionProperties);

        return datasource;
    }
}
