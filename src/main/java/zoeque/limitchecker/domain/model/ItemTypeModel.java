package zoeque.limitchecker.domain.model;

import lombok.Getter;

/**
 * Define item type
 */
public enum ItemTypeModel {
  VEGETABLE("vegetable", 3, "野菜", false),
  FRUIT("fruit", 6, "果物", false),
  MEAT("meat", 1, "肉類", true),

  SPICE("spice", 30, "調味料", true),
  EGG("egg", 3, "卵", true),
  JUICE("juice", 3, "飲料", true),
  DAIRY("dairy", 2, "乳製品", true),
  SNACK("snack", 10, "菓子類", true),
  OTHERS("others", 7, "その他", true);

  @Getter
  String itemType;
  @Getter
  Integer expirationDate;
  @Getter
  String label;
  @Getter
  Boolean hasExpirationDate;

  ItemTypeModel(String itemType, Integer expirationDate, String label, Boolean hasExpirationDate) {
    this.itemType = itemType;
    this.expirationDate = expirationDate;
    this.label = label;
    this.hasExpirationDate = hasExpirationDate;
  }
}
