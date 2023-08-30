package zoeque.limitchecker.application.service;

import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.application.dto.record.ItemDetailDto;
import zoeque.limitchecker.application.dto.record.StoredItemDto;
import zoeque.limitchecker.application.event.MailNotificationEvent;
import zoeque.limitchecker.configuration.ConstantModel;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.domain.specification.StoredItemSpecification;

/**
 * The main business logic class for LimitChecker application.
 * The validation process runs by the {@link Scheduled} annotation.
 */
@Slf4j
@Service
public class StoredItemCheckerService {
  IStoredItemRepository repository;
  StoredItemSpecification<StoredItem> specification;
  ApplicationEventPublisher publisher;

  public StoredItemCheckerService(IStoredItemRepository repository,
                                  StoredItemSpecification<StoredItem> specification,
                                  ApplicationEventPublisher publisher) {
    this.repository = repository;
    this.specification = specification;
    this.publisher = publisher;
  }

  /**
   * The scheduled task to validate saved items in Database.
   * If expired items or warned items are found, publish {@link MailNotificationEvent}
   * to the {@link MailSenderService} to send e-mail to notify the caution.
   */
  @Scheduled(cron = ConstantModel.SCHEDULED_PARAMETER)
  public void execute() {
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
      publisher.publishEvent(convertEntityToDto(warnedItemTry.get()).get());
    }
    if (!expiredItemTry.get().isEmpty()) {
      publisher.publishEvent(convertEntityToDto(expiredItemTry.get()).get());
    }
  }

  /**
   * Find the warned item and collect all items in the same list.
   *
   * @return all warned items with result {@link Try}.
   */
  protected Try<List<StoredItem>> findWarnedItem() {
    try {
      List<StoredItem> warnedItemList = new ArrayList<>();
      // validate all type of the item
      for (ItemTypeModel model : ItemTypeModel.values()) {
        List<StoredItem> itemList
                = repository.findAll(specification.warnedItem(model.getExpirationDate()));
        if (!itemList.isEmpty()) {
          // add item to list for notification if the warned item exists
          warnedItemList.addAll(itemList);
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
      return Try.success(repository.findAll(specification.expiredItem()));
    } catch (Exception e) {
      log.warn("Cannot access to the database!");
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
