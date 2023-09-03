package zoeque.limitchecker.application.service;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.configuration.ConstantModel;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.repository.StoredItemRepositoryImpl;
import zoeque.limitchecker.domain.specification.StoredItemSpecification;

/**
 * The extended class of {@link AbstractStoredItemCheckerService}.
 * The validation process runs by the {@link Scheduled} annotation.
 */
@Slf4j
@Service
public class StoredItemCheckerService
        extends AbstractStoredItemCheckerService {
  public StoredItemCheckerService(StoredItemRepositoryImpl repository,
                                  StoredItemSpecification<StoredItem> specification,
                                  ApplicationEventPublisher publisher) {
    super(repository, specification, publisher);
  }

  /**
   * The executor method to validate items in DB
   */
  @Override
  @Scheduled(cron = ConstantModel.SCHEDULED_PARAMETER)
  public void execute() {
    log.info("The validation process starts : {}", LocalDateTime.now());
    super.execute();
  }
}
