package zoeque.limitchecker.application.dto.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import zoeque.limitchecker.application.dto.IStoredItemDto;

/**
 * Data transfer class of items
 */
@JsonDeserialize
public record StoredItemDto(ItemDetailDto itemDetail) implements IStoredItemDto {
  public ItemDetailDto getItemDetail(){
    return this.itemDetail;
  }
}
