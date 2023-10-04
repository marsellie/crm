package kpfu.crm.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "admin.startup")
public class AppConfig {

    private String username = "admin";

    private String password = "admin";
}
