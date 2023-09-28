package zoeque.limitchecker.application.service.usecase;

import io.vavr.control.Try;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class StoredItemCreateService {
  IStoredItemRepository repository;
  StoredItemFactory factory;

  public StoredItemCreateService(IStoredItemRepository repository,
                                 StoredItemFactory factory) {
    this.repository = repository;
    this.factory = factory;
  }

  /**
   * The business logic to create new {@link StoredItem}/
   *
   * @param jsonDto {@link StoredItemJsonDto} given via {@link StoredItemController}.
   * @return The result of {@link Try} with a created instance.
   */
  public Try<StoredItem> createNewStoredItem(StoredItemJsonDto jsonDto) {
    try {
      StoredItem storedItem = factory.createStoredItem(
              factory.createItemDetail(jsonDto.itemName(),
                      convertItemTypeModelByValue(jsonDto.itemType()),
                      convertStringDateToLocalDateTime(jsonDto.expiredDate())).get(),
              AlertStatusFlag.NOT_REPORTED);
      repository.save(storedItem);
      return Try.success(storedItem);
    } catch (Exception e) {
      log.warn("Cannot convert JSON to StoredItem entity : {}", jsonDto);
      return Try.failure(e);
    }
  }

  /**
   * The service method to find the all saved {@link StoredItem}.
   *
   * @return The list instance with result of {@link Try} or an exception.
   */
  public Try<List<StoredItemJsonDto>> findAllStoredItem() {
    try {
      List<StoredItem> itemList = repository.findAll();
      List<StoredItemJsonDto> jsonList = new ArrayList<>();
      for (StoredItem item : itemList) {
        StoredItemJsonDto json
                = new StoredItemJsonDto(item.getItemDetail().getItemName().getName(),
                item.getItemDetail().getItemType().getModel().getLabel(),
                convertLocalDateTimeToString(
                        item.getItemDetail().getExpirationDate().getDate()));
        jsonList.add(json);
      }
      return Try.success(jsonList);
    } catch (Exception e) {
      log.warn("Cannot find the stored item caused by : " + e.getCause());
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

  private LocalDateTime convertStringDateToLocalDateTime(String date) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Date formatDate = sdf.parse(date);
    return LocalDateTime.ofInstant(formatDate.toInstant(), ZoneId.systemDefault());
  }

  private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    return localDateTime.format(formatter);
  }
}
