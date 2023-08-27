package zoeque.limitchecker.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NonNull;
import zoeque.limitchecker.domain.model.ItemTypeModel;

import java.time.LocalDateTime;

/**
 * Data transfer class of items
 */
@Data
@JsonDeserialize(as = ItemDto.class)
public class ItemDto {
  @NonNull
  String itemName;
  @NonNull
  ItemTypeModel itemType;
  @NonNull
  LocalDateTime expirationDate;
}
