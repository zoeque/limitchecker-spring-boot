package zoeque.limitchecker.application.dto.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import zoeque.limitchecker.application.dto.IItemDetailDto;
import zoeque.limitchecker.domain.model.ItemTypeModel;

@JsonDeserialize
public record ItemDetailDto(String itemName,
                            ItemTypeModel itemTypeModel,
                            ExpirationDateDto expirationDate)
        implements IItemDetailDto {
  public String getItemName() {
    return this.itemName;
  }

  public ItemTypeModel getItemTypeModel() {
    return this.itemTypeModel;
  }

  public ExpirationDateDto getExpirationDate() {
    return this.expirationDate;
  }
}
