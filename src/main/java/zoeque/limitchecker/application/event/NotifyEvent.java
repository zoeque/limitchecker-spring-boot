package zoeque.limitchecker.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.application.dto.record.StoredItemDto;

import java.util.List;

/**
 * The event to send the information of e-mail
 */
@Getter
@AllArgsConstructor
public class NotifyEvent {
  @NonNull
  List<StoredItemDto> itemList;
  @NonNull
  NotifyTypeModel notifyTypeModel;
}
