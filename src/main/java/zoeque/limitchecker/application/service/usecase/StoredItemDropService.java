package zoeque.limitchecker.application.service.usecase;

import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.adapter.StoredItemController;
import zoeque.limitchecker.application.event.StoredItemDropRequest;
import zoeque.limitchecker.configuration.ConstantModel;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.domain.specification.StoredItemSpecification;

/**
 * The service class to delete the {@link StoredItem} in a DB.
 */
@Slf4j
@Service
public class StoredItemDropService extends AbstractStoredItemService {
  public StoredItemDropService(IStoredItemRepository repository,
                               StoredItemSpecification<StoredItem> specification,
                               ApplicationEventPublisher publisher) {
    super(repository, specification, publisher);
  }

  /**
   * The deletion process for reported {@link StoredItem} via
   * {@link StoredItemDropRequest}.
   * The process is triggered by the Scheduled API in Spring boot.
   */
  @Scheduled(cron = ConstantModel.CRON_FOR_DELETION)
  public void dropReportedStoredItem() {
    try {
      log.info("Clean up the stored item records that already reported.");
      Try<List<StoredItem>> expiredItem = findExpiredItem();
      repository.deleteAll(expiredItem.get());
      log.info("Delete all reported items.");
    } catch (Exception e) {
      log.warn("Exception is occurred in deletion process!! : {}",
              e.getCause().toString());
      throw new IllegalStateException(e);
    }
  }

  /**
   * The deletion process requested from {@link StoredItemController}.
   *
   * @param id ID for {@link StoredItem}. The ID is received from REST request.
   * @return {@link Try} with ID of deleted item or some exception.
   */
  public Try<Long> dropRequestedStoredItem(Long id) {
    try {
      Optional<StoredItem> byIdentifier = repository.findByIdentifier(id);
      if (byIdentifier.isEmpty()) {
        log.warn("There is no StoredItem with ID : {}", id);
        return Try.failure(new IllegalArgumentException("There is no requested StoredItem in DB"));
      }
      repository.delete(byIdentifier.get());
      return Try.success(id);
    } catch (Exception e) {
      log.warn("Unexpected exception occurred in deletion process : {}", e.getCause());
      return Try.failure(e);
    }
  }
}
