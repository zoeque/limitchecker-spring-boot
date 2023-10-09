package zoeque.limitchecker.application.service.usecase;

import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import zoeque.limitchecker.application.dto.record.ItemDetailDto;
import zoeque.limitchecker.application.dto.record.StoredItemDto;
import zoeque.limitchecker.application.event.MailNotificationEvent;
import zoeque.limitchecker.application.service.mailer.AbstractMailSenderService;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.domain.specification.StoredItemSpecification;

/**
 * The main business logic class for LimitChecker application.
 * The validation process runs by the {@link Scheduled} annotation.
 */
@Slf4j
public abstract class AbstractStoredItemService {
  IStoredItemRepository repository;
  StoredItemSpecification<StoredItem> specification;
  ApplicationEventPublisher publisher;

  public AbstractStoredItemService(IStoredItemRepository repository,
                                   StoredItemSpecification<StoredItem> specification,
                                   ApplicationEventPublisher publisher) {
    this.repository = repository;
    this.specification = specification;
    this.publisher = publisher;
  }

  /**
   * The scheduled task to validate saved items in Database.
   * If expired items or warned items are found, publish {@link MailNotificationEvent}
   * to the {@link AbstractMailSenderService} to send e-mail to notify the caution.
   */
  @Transactional
  protected void execute() {
    Try<List<StoredItem>> warnedItemTry = findWarnedItem();
    Try<List<StoredItem>> expiredItemTry = findExpiredItem();
    if (warnedItemTry.isFailure()) {
      log.warn("Cannot find the warned item because of the exception! "
              + warnedItemTry.getCause());
    } else if (expiredItemTry.isFailure()) {
      log.warn("Cannot find the expired item because of the exception! "
              + expiredItemTry.getCause());
    }

    // publish the event to the mailer service
    if (!warnedItemTry.get().isEmpty()) {
      publisher.publishEvent(
              new MailNotificationEvent(convertEntityToDto(warnedItemTry.get()).get(),
                      NotifyTypeModel.WARN));
    }
    List<StoredItem> expiredItemList = expiredItemTry.get();
    if (!expiredItemList.isEmpty()) {
      publisher.publishEvent(
              new MailNotificationEvent(convertEntityToDto(expiredItemList).get(),
                      NotifyTypeModel.ALERT));
    }
    expiredItemList.forEach(item -> {
      Try<StoredItem> handledItemTry
              = item.changeItemStatus(AlertStatusFlag.REPORTED);
      repository.save(handledItemTry.get());
    });
  }

  /**
   * Find the warned item and collect all standard items in the same list.
   *
   * @return all warned items with result {@link Try}.
   */
  protected Try<List<StoredItem>> findWarnedItem() {
    try {
      List<StoredItem> warnedItemList = new ArrayList<>();
      // validate all type of the item
      for (ItemTypeModel model : ItemTypeModel.values()) {
        // Add only items with expiration date
        if (model.getHasExpirationDate()) {
          List<StoredItem> standardItemList
                  = repository.findAll(specification.warnedStandardItem(model));
          if (!standardItemList.isEmpty()) {
            // add item to list for notification if the warned item exists
            warnedItemList.addAll(standardItemList);
          }
        } else {
          // Add fresh items in a warned item list.
          List<StoredItem> freshItemList
                  = repository.findAll(specification.warnedFreshItem(model));
          if (!freshItemList.isEmpty()) {
            warnedItemList.addAll(freshItemList);
          }
        }
      }
      return Try.success(warnedItemList);
    } catch (Exception e) {
      log.warn("Cannot access to the database!");
      return Try.failure(e);
    }
  }

  /**
   * Collect all expired item
   *
   * @return Result {@link Try} with the list of expired items or an exception.
   */
  protected Try<List<StoredItem>> findExpiredItem() {
    try {
      List<StoredItem> expiredItemList = new ArrayList<>();
      for (ItemTypeModel model : ItemTypeModel.values()) {
        if (model.getHasExpirationDate()) {
          expiredItemList.addAll(
                  repository.findAll(specification.expiredStandardItemByItemType(model)));
        } else {
          expiredItemList.addAll(
                  repository.findAll(specification.expiredFreshItemByItemType(model)));
        }
      }
      return Try.success(expiredItemList);
    } catch (Exception e) {
      log.warn("Cannot access to the database!");
      return Try.failure(e);
    }
  }

  /**
   * Find items that are already reported and needed to delete.
   *
   * @return The list of {@link StoredItem}.
   */
  protected Try<List<StoredItem>> findItemsToDrop() {
    try {
      List<StoredItem> items = repository.findAll(specification.itemToDrop());
      return Try.success(items);
    } catch (Exception e) {
      return Try.failure(e);
    }
  }

  protected Try<List<StoredItemDto>> convertEntityToDto(List<StoredItem> entityList) {
    try {
      List<StoredItemDto> dtoList = new ArrayList<>();
      for (StoredItem entity : entityList) {
        StoredItemDto dto = new StoredItemDto(new ItemDetailDto(
                entity.getItemDetail().getItemName().getName(),
                entity.getItemDetail().getItemType().getModel(),
                entity.getItemDetail().getExpirationDate().getDate()
        ));
        dtoList.add(dto);
      }
      return Try.success(dtoList);
    } catch (Exception e) {
      return Try.failure(e);
    }
  }
}
