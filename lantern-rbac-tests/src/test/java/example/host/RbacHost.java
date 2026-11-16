package example.host;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/** A business host that imports neither Lantern configuration nor Lantern component packages. */
@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration(excludeName = "com.lamp.decoration.core.spring.DecorationAutoConfiguration")
@Import(HostProbeController.class)
public class RbacHost {

    @Bean
    public DataSource businessDataSource(Environment environment) {
        DruidDataSource source = new DruidDataSource();
        source.setUrl(environment.getProperty("test.datasource.url", "jdbc:h2:mem:lantern-host;MODE=MySQL"));
        source.setUsername(environment.getProperty("test.datasource.username", "sa"));
        source.setPassword(environment.getProperty("test.datasource.password", ""));
        source.setInitialSize(0);
        source.setMinIdle(0);
        source.setMaxActive(4);
        source.setTestWhileIdle(false);
        return source;
    }
}
