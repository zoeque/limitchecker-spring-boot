package zoeque.limitchecker.application.service;

import io.vavr.control.Try;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zoeque.limitchecker.application.dto.record.ItemDetailDto;
import zoeque.limitchecker.application.dto.record.StoredItemDto;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.testtool.DatabaseDropService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StoredItemServiceTest {
  @Autowired
  DatabaseDropService databaseDropService;
  @Autowired
  StoredItemService storedItemService;

  @BeforeEach
  public void deleteAllDataInDb() {
    databaseDropService.deleteAllData();
  }

  @Test
  public void createNewItem_thenReturnSuccess() {
    StoredItemDto item = new StoredItemDto(new ItemDetailDto("test",
            ItemTypeModel.OTHERS, LocalDateTime.now()));
    Try<StoredItemDto> itemDtoTry = storedItemService.create(item);
    Assertions.assertTrue(itemDtoTry.isSuccess());
  }
}
