package zoeque.limitchecker.domain.specification;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
public class StoredItemSpecificationTest {
  @Autowired
  DatabaseDropService databaseDropService;
  @Autowired
  IStoredItemRepository repository;
  @Autowired
  StoredItemFactory factory;
  @Autowired
  StoredItemSpecification specification;

  @BeforeEach
  public void setup() {
    databaseDropService.deleteAllData();
  }

  @Test
  public void ifStoredItemWithSpecifiedConditionInDb_canFindWarnItem() {
    String name = "test";
    StoredItem storedItem = factory.createStoredItem(factory.createStoredItemIdentifier(name).get(),
            factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().plusDays(2)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(storedItem);
    List<StoredItem> items = repository.findAll(
            specification.warnedItem(ItemTypeModel.EGG));

    Assertions.assertEquals(1, items.size());
  }

  @Test
  public void ifStoredItemWithExpiredConditionInDb_canFindExpiredItem() {
    String name = "test";
    StoredItem storedItem = factory.createStoredItem(factory.createStoredItemIdentifier(name).get(),
            factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().minusDays(1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(storedItem);
    List<StoredItem> items = repository.findAll(
            specification.expiredItem());

    Assertions.assertEquals(1, items.size());
  }
}
