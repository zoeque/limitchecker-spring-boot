package zoeque.limitchecker.application.event;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import zoeque.limitchecker.application.dto.record.StoredItemDto;

/**
 * The event class to propagate the stored item to delete.
 */
@Getter
@AllArgsConstructor
public class StoredItemDropRequest {
  List<StoredItemDto> storedItemDtoList;
}
