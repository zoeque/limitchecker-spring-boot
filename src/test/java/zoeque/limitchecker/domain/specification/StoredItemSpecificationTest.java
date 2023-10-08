package zoeque.limitchecker.domain.specification;

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
    StoredItem storedItem = factory.createStoredItem(factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().plusDays(2)).get(),
            AlertStatusFlag.NOT_REPORTED);
    StoredItem storedItemNotToFind = factory.createStoredItem(factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().plusDays(2)).get(),
            AlertStatusFlag.REPORTED);
    repository.save(storedItem);
    repository.save(storedItemNotToFind);
    List<StoredItem> items = repository.findAll(
            specification.warnedStandardItem(ItemTypeModel.EGG));
    List<StoredItem> allItems = repository.findAll();

    Assertions.assertEquals(1, items.size());
    Assertions.assertEquals(2, allItems.size());
  }

  @Test
  public void ifStoredItemWithExpiredConditionInDb_canFindExpiredItem() {
    String name = "test";
    StoredItem storedItem = factory.createStoredItem(factory.createItemDetail(name, ItemTypeModel.EGG, LocalDateTime.now().minusMinutes(1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(storedItem);
    List<StoredItem> items = repository.findAll(
            specification.expiredStandardItemByItemType(ItemTypeModel.EGG));

    Assertions.assertEquals(1, items.size());
  }

  @Test
  public void saveTwoItem_whenTheOneItemHasWarnedDefinition_ReturnOneItem() {
    String name = "test";
    StoredItem expiredItem = factory.createStoredItem(factory.createItemDetail(name,
                    ItemTypeModel.VEGETABLE, LocalDateTime.now().minusDays(4)).get(),
            AlertStatusFlag.NOT_REPORTED);
    StoredItem freshItem = factory.createStoredItem(factory.createItemDetail(name,
                    ItemTypeModel.VEGETABLE, LocalDateTime.now().minusDays(1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(expiredItem);
    repository.save(freshItem);
    List<StoredItem> allExpiredItems
            = repository.findAll(specification.warnedFreshItem(ItemTypeModel.VEGETABLE));
    List<StoredItem> allItems = repository.findAll();
    Assertions.assertEquals(1, allExpiredItems.size());
    Assertions.assertEquals(2, allItems.size());
  }

  @Test
  public void saveTwoItem_whenTheOneItemExpiredOverThanDefinition_ReturnOneItem() {
    String name = "test";
    StoredItem expiredItem = factory.createStoredItem(factory.createItemDetail(name,
                    ItemTypeModel.VEGETABLE, LocalDateTime.now().minusDays(
                            ItemTypeModel.VEGETABLE.getExpirationDate() + 1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    StoredItem freshItem = factory.createStoredItem(factory.createItemDetail(name,
                    ItemTypeModel.VEGETABLE, LocalDateTime.now().minusDays(
                            ItemTypeModel.VEGETABLE.getExpirationDate() - 1)).get(),
            AlertStatusFlag.NOT_REPORTED);
    repository.save(expiredItem);
    repository.save(freshItem);
    List<StoredItem> allExpiredItems
            = repository.findAll(specification.expiredFreshItemByItemType(ItemTypeModel.VEGETABLE));
    List<StoredItem> allItems = repository.findAll();
    Assertions.assertEquals(1, allExpiredItems.size());
    Assertions.assertEquals(2, allItems.size());
  }
}
