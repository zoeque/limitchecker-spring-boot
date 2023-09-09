package zoeque.limitchecker.application.service.checker;

import io.vavr.control.Try;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.application.event.MailNotificationEvent;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.factory.StoredItemFactory;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.application.dto.record.StoredItemDto;

@Service
@Slf4j
public class StoredItemService {
  IStoredItemRepository storedItemRepository;
  StoredItemFactory storedItemFactory;

  public StoredItemService(IStoredItemRepository storedItemRepository,
                           StoredItemFactory storedItemFactory) {
    this.storedItemRepository = storedItemRepository;
    this.storedItemFactory = storedItemFactory;
  }

  /**
   * Save item via hibernate
   *
   * @param storedItemDto
   */
  public Try<StoredItemDto> create(StoredItemDto storedItemDto) {
    try {
      StoredItem storedItem = storedItemFactory.createStoredItem(
              storedItemFactory.createStoredItemIdentifier(UUID.randomUUID().toString()).get(),
              storedItemFactory.createItemDetail(storedItemDto.getItemDetail().getItemName(),
                      storedItemDto.getItemDetail().getItemTypeModel(),
                      storedItemDto.getItemDetail().getExpirationDate()).get(),
              AlertStatusFlag.NOT_REPORTED);
      storedItemRepository.save(storedItem);
      return Try.success(storedItemDto);
    } catch (Exception e) {
      log.warn("Failed to create Item : {}", storedItemDto.getItemDetail().getItemName());
      return Try.failure(e);
    }
  }

  /**
   * Update the reported status to Reported
   *
   * @param event items that is needed to update
   */
  @EventListener
  public void updateItemStatusToReported(MailNotificationEvent event) {
    if (event.getNotifyTypeModel() == NotifyTypeModel.WARN) {
      event.getItemList().forEach(storedItemDto -> {
        // TODO ここで報告状態の更新を行う
      });
    }
  }
}
