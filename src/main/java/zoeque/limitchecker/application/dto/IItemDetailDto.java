package zoeque.limitchecker.application.dto;

import zoeque.limitchecker.application.dto.record.ExpirationDateDto;
import zoeque.limitchecker.domain.model.ItemTypeModel;

public interface IItemDetailDto {
  String getItemName();
  ItemTypeModel getItemTypeModel();
  ExpirationDateDto getExpirationDate();
}
