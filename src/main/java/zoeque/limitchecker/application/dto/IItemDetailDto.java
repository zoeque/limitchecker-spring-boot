package zoeque.limitchecker.application.dto;

import java.time.LocalDateTime;
import zoeque.limitchecker.application.dto.record.ExpirationDateDto;
import zoeque.limitchecker.domain.model.ItemTypeModel;

/**
 * The DTO interface for item detail.
 */
public interface IItemDetailDto {
  String getItemName();
  ItemTypeModel getItemTypeModel();
  LocalDateTime getExpirationDate();
}
