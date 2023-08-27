package zoeque.limitchecker;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import zoeque.limitchecker.testtool.DatabaseDropService;

@SpringBootTest
class LimitCheckerApplicationTests {

  @Test
  void contextLoads() {
  }
}
