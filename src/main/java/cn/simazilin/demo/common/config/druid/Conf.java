package cn.simazilin.demo.common.config.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-26 11:20
 * @Project: 阳光物业V1.0
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "conf")
public class Conf {

    private String env;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
