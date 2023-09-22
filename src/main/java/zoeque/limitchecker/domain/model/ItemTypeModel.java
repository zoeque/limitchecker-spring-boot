package zoeque.limitchecker.domain.model;

import lombok.Getter;

/**
 * Define item type
 */
public enum ItemTypeModel {
  SPICE("spice", 30, "調味料"),
  EGG("egg", 3, "卵"),
  MEAT("meat", 1, "肉類"),
  JUICE("juice", 3, "飲料"),
  DAIRY("dairy", 2, "乳製品"),
  SNACK("snack", 10, "菓子類"),
  OTHERS("others", 7, "その他");

  @Getter
  String itemType;
  @Getter
  Integer expirationDate;
  @Getter
  String label;

  ItemTypeModel(String itemType, Integer expirationDate, String label) {
    this.itemType = itemType;
    this.expirationDate = expirationDate;
    this.label = label;
  }
}
