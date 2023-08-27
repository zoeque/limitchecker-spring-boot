package zoeque.limitchecker.application.service;

import io.vavr.control.Try;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.application.event.NotifyEvent;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.factory.StoredItemFactory;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.application.dto.ItemDto;

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
   * @param itemDto
   */
  public Try<ItemDto> create(ItemDto itemDto) {
    try {
      StoredItem storedItem = storedItemFactory.createStoredItem(
              storedItemFactory.createStoredItemIdentifier(UUID.randomUUID().toString()).get(),
              storedItemFactory.createItemDetail(itemDto.getItemName(),
                      itemDto.getItemType(),
                      itemDto.getExpirationDate()).get(),
              AlertStatusFlag.NOT_REPORTED);
      storedItemRepository.save(storedItem);
      return Try.success(itemDto);
    } catch (Exception e) {
      log.warn("Failed to create Item : {}", itemDto.getItemName());
      return Try.failure(e);
    }
  }

  /**
   * Update the reported status to Reported
   *
   * @param event items that is needed to update
   */
  @EventListener
  public void updateItemStatusToReported(NotifyEvent event) {
    if (event.getNotifyTypeModel() == NotifyTypeModel.WARN) {
      event.getItemList().forEach(itemDto -> {
        // TODO ここで報告状態の更新を行う
      });
    }
  }
}
