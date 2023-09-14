package zoeque.limitchecker.application.service.usecase;

import io.vavr.control.Try;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.adapter.StoredItemController;
import zoeque.limitchecker.application.dto.record.StoredItemJsonDto;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.factory.StoredItemFactory;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;

/**
 * Save or modify the stored item in Database.
 * The request is received from {@link StoredItemController}.
 */
@Slf4j
@Service
public class StoredItemService {
  IStoredItemRepository repository;
  StoredItemFactory factory;

  public StoredItemService(IStoredItemRepository repository,
                           StoredItemFactory factory) {
    this.repository = repository;
    this.factory = factory;
  }

  public Try<StoredItem> createNewStoredItem(StoredItemJsonDto jsonDto) {
    try {
      return Try.success(factory.createStoredItem(
              factory.createItemDetail(jsonDto.itemName(),
                      convertItemTypeModelByValue(jsonDto.itemType()),
                      convertStringDateToLocalDateTime(jsonDto.expiredDate())).get(),
              AlertStatusFlag.NOT_REPORTED));
    } catch (Exception e) {
      log.warn("Cannot convert JSON to StoredItem entity : {}", jsonDto);
      return Try.failure(e);
    }
  }

  private ItemTypeModel convertItemTypeModelByValue(String value) {
    ItemTypeModel[] models = ItemTypeModel.values();
    for (ItemTypeModel model : models) {
      if (model.getItemType().equals(value)) {
        return model;
      }
    }
    throw new IllegalArgumentException("Invalid Item type is received!! : " + value);
  }

  private LocalDateTime convertStringDateToLocalDateTime(String date) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    return LocalDateTime.parse(date, dtf);
  }
}
