package zoeque.limitchecker.domain.entity.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zoeque.limitchecker.domain.model.ItemTypeModel;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@Embeddable
public class ItemType {
  @Enumerated(EnumType.STRING)
  ItemTypeModel model;
  @Embedded
  AlertDefinition alertDefinition;

  public ItemType(ItemTypeModel model, AlertDefinition alertDefinition) {
    setItemTypeModel(model);
    setAlertDefinition(alertDefinition);
  }

  private void setItemTypeModel(ItemTypeModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Item type model must not be null");
    }
    this.model = model;
  }

  private void setAlertDefinition(AlertDefinition alertDefinition) {
    if (alertDefinition == null) {
      throw new IllegalArgumentException("Alert definition must not be null");
    } else if (alertDefinition.getAlertDate() <= 0) {
      throw new IllegalArgumentException("Alert definition is given invalid value : " +
              alertDefinition.getAlertDate());
    }
    this.alertDefinition = alertDefinition;
  }

}
