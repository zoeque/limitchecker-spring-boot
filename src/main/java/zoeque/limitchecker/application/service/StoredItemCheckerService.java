package zoeque.limitchecker.application.service;

import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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

  @Scheduled(cron = ConstantModel.SCHEDULED_PARAMETER)
  public void execute() {
    validateWarnedItem();
  }

  private Try<List<StoredItem>> validateWarnedItem() {
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
}
