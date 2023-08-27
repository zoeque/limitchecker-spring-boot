package zoeque.limitchecker.domain.entity.factory;

import io.vavr.control.Try;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.valueobject.*;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.model.AlertStatusFlag;

import java.util.UUID;

/**
 * Factory class to create new Item and its value object
 */
@Slf4j
public abstract class AbstractStoredItemFactory {
  public StoredItem createStoredItem(StoredItemIdentifier identifier,
                                     ItemDetail itemDetail,
                                     AlertStatusFlag alertStatusFlag) {
    return new StoredItem(identifier, itemDetail, alertStatusFlag);
  }

  /**
   * Create new identifier for the {@link StoredItem}
   *
   * @param name the name of the identifier. use {@link UUID} and else.
   * @return Result {@link Try} with the created instance or the exception.
   */
  public Try<StoredItemIdentifier> createStoredItemIdentifier(String name) {
    try {
      return Try.success(new StoredItemIdentifier(name));
    } catch (Exception e) {
      return Try.failure(e);
    }
  }

  /**
   * Create new item detail instance for the {@link StoredItem}
   *
   * @param name  the name of the item.
   * @param model item type with the {@link ItemTypeModel}
   * @param date  the expiration {@link java.time.LocalDateTime} defined on each item.
   * @return Result {@link Try} with the created instance or the exception.
   */
  public Try<ItemDetail> createItemDetail(String name,
                                          ItemTypeModel model,
                                          LocalDateTime date) {
    try {
      return Try.success(new ItemDetail(createItemName(name).get(),
              createExpirationDate(date).get(),
              createItemType(model).get()));
    } catch (Exception e) {
      return Try.failure(e);
    }
  }

  private Try<ItemName> createItemName(String name) {
    try {
      return Try.success(new ItemName(name));
    } catch (Exception e) {
      return Try.failure(e);
    }
  }

  private Try<ItemType> createItemType(ItemTypeModel model) {
    try {
      return Try.success(new ItemType(model,
              new AlertDefinition(model.getExpirationDate())));
    } catch (Exception e) {
      return Try.failure(e);
    }
  }

  private Try<ExpirationDate> createExpirationDate(LocalDateTime date) {
    try {
      return Try.success(new ExpirationDate(date));
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
