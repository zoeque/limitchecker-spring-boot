package zoeque.limitchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"zoeque.limitchecker"})
@EnableJpaRepositories(basePackages = "zoeque.limitchecker")
@ComponentScan(basePackages = {"zoeque.limitchecker"})
public class LimitCheckerApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext configurableApplicationContext
            = SpringApplication.run(LimitCheckerApplication.class, args);
  }
}
