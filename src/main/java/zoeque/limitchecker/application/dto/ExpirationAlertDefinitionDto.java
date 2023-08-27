package zoeque.limitchecker.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NonNull;
import zoeque.limitchecker.domain.model.ItemTypeModel;

/**
 * Data transfer class of Expiration Alert Date
 */
@Data
@JsonDeserialize(as = ExpirationAlertDefinitionDto.class)
public class ExpirationAlertDefinitionDto {
  @NonNull ItemTypeModel itemTypeModel;
  @NonNull Integer expirationDays;
}
