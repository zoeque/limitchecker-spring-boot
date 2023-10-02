package zoeque.limitchecker.application.dto.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import zoeque.limitchecker.adapter.StoredItemController;

/**
 * The DTO class received via REST API.
 * This class is given by POST request received
 * {@link StoredItemController}.
 *
 * @param storedItemIdentifier the identifier for each stored items.
 * @param itemName             the name of stored item.
 * @param itemType             the selected item type.
 * @param expiredDate          the selected expired date.
 */
@JsonDeserialize(as = StoredItemJsonDto.class)
public record StoredItemJsonDto(
        Long storedItemIdentifier,
        String itemName,
        String itemType,
        String expiredDate) {
}
