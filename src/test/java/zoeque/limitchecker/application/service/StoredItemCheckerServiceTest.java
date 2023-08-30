package zoeque.limitchecker.application.service;

import io.vavr.control.Try;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.factory.StoredItemFactory;
import zoeque.limitchecker.domain.entity.valueobject.*;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.domain.specification.StoredItemSpecification;
import zoeque.limitchecker.testtool.DatabaseDropService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StoredItemCheckerServiceTest {
  @Autowired
  IStoredItemRepository repository;
  @Autowired
  DatabaseDropService databaseDropService;
  @Autowired
  StoredItemFactory factory;
  @Autowired
  StoredItemSpecification specification;
  @Mock
  ApplicationEventPublisher publisher;

  @BeforeEach
  public void cleanTestData() {
    databaseDropService.deleteAllData();
  }

  @Test
  public void whenSaveTwoWarnedItems_withDifferentTypes_foundTwoItemsInDb() {
    StoredItemCheckerService service = new StoredItemCheckerService(repository, specification, publisher);
    StoredItem storedItem1 = factory.createStoredItem(factory.createStoredItemIdentifier("test1").get(),
            factory.createItemDetail("test1", ItemTypeModel.EGG, LocalDateTime.now().plusDays(2)).get(),
            AlertStatusFlag.NOT_REPORTED);
    StoredItem storedItem2 = factory.createStoredItem(factory.createStoredItemIdentifier("test2").get(),
            factory.createItemDetail("test2", ItemTypeModel.SNACK, LocalDateTime.now().plusDays(5)).get(),
            AlertStatusFlag.NOT_REPORTED);

    repository.save(storedItem1);
    repository.save(storedItem2);

    Try<List<StoredItem>> warnedItemTry = service.findWarnedItem();
    Assertions.assertTrue(warnedItemTry.isSuccess());
    Assertions.assertEquals(2, warnedItemTry.get().size());
  }
}
