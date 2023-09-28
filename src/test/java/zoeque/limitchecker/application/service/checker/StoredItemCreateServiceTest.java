package zoeque.limitchecker.application.service.checker;

import io.vavr.control.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zoeque.limitchecker.application.dto.record.StoredItemJsonDto;
import zoeque.limitchecker.application.service.usecase.StoredItemCreateService;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.testtool.DatabaseDropService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StoredItemCreateServiceTest {
  @Autowired
  DatabaseDropService databaseDropService;
  @Autowired
  StoredItemCreateService storedItemCreateService;

  @BeforeEach
  public void deleteAllDataInDb() {
    databaseDropService.deleteAllData();
  }

  @Test
  public void createNewItem_thenReturnSuccess() {
    StoredItemJsonDto item = new StoredItemJsonDto("test", "others", "2099/12/31");
    Try<StoredItem> itemDtoTry = storedItemCreateService.createNewStoredItem(item);
    Assertions.assertTrue(itemDtoTry.isSuccess());
  }
}
