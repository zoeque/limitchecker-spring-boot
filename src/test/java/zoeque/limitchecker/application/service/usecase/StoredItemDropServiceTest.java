package zoeque.limitchecker.application.service.usecase;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.factory.StoredItemFactory;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.testtool.DatabaseDropService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StoredItemDropServiceTest {
  @Autowired
  StoredItemDropService service;
  @Autowired
  IStoredItemRepository repository;
  @Autowired
  StoredItemFactory factory;
  @Autowired
  DatabaseDropService databaseDropService;

  @BeforeEach
  public void deleteAllDataInDb() {
    databaseDropService.deleteAllData();
  }

  @Test
  public void attemptToDeleteStoredItemViaScheduledAnnotation_deleteCompletely() {
    String name = "test";
    StoredItem storedItem = factory.createStoredItem(factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().minusMinutes(1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(storedItem);

    service.dropReportedStoredItem();

    List<StoredItem> items = repository.findAll();
    Assertions.assertEquals(0, items.size());
  }

  @Test
  public void attemptToDeleteStoredItemViaRestApi_deleteCompletely() {
    String name = "test";
    StoredItem storedItem = factory.createStoredItem(factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().minusMinutes(1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(storedItem);
    List<StoredItem> itemsBeforeDelete = repository.findAll();
    Assertions.assertEquals(1, itemsBeforeDelete.size());

    long identifier = storedItem.getIdentifier();
    service.dropRequestedStoredItem(identifier);
    List<StoredItem> itemsAfterDelete = repository.findAll();
    Assertions.assertEquals(0, itemsAfterDelete.size());
  }
}
