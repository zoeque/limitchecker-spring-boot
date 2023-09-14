package zoeque.limitchecker.domain.model;

import lombok.Getter;

/**
 * Define item type
 */
public enum ItemTypeModel {
  SPICE("spice", 30),
  EGG("egg", 3),
  MEAT("meat", 1),
  JUICE("juice", 3),
  DAIRY("dairy", 2),
  SNACK("snack", 10),
  OTHERS("others", 7);

  @Getter
  String itemType;
  @Getter
  Integer expirationDate;

  ItemTypeModel(String itemType, Integer expirationDate) {
    this.itemType = itemType;
    this.expirationDate = expirationDate;
  }
}
