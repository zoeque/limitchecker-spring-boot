package zoeque.limitchecker.domain.entity.valueobject;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Entity class for Item
 */
@Slf4j
@Getter
@Embeddable
@NoArgsConstructor
public class ItemDetail {
  @Embedded
  ItemName itemName;
  @Embedded
  ItemType itemType;
  @Embedded
  ExpirationDate expirationDate;

  public ItemDetail(ItemName itemName,
                    ExpirationDate expirationDate,
                    ItemType itemType) {
    setItemName(itemName);
    setExpirationDate(expirationDate);
    setItemType(itemType);
  }

  private void setItemName(ItemName itemName) {
    if (itemName == null) {
      throw new IllegalArgumentException("Item name cannot be null!");
    }
    this.itemName = itemName;
  }

  private void setItemType(ItemType itemType){
    if(itemType == null){
      throw new IllegalArgumentException("Item type must not be null!");
    }
    this.itemType = itemType;
  }

  private void setExpirationDate(ExpirationDate expirationDate) {
    if (expirationDate == null) {
      throw new IllegalArgumentException("Expiration date cannot be null!");
    }
    this.expirationDate = expirationDate;
  }

  /**
   * Change the name of item
   *
   * @param itemDetail new item data
   * @return Item modified item data
   */
  public ItemDetail changeItemName(ItemDetail itemDetail) {
    if (itemDetail == null) {
      throw new IllegalArgumentException("Item cannot be null!");
    }
    if (this.itemName == itemDetail.getItemName()) {
      log.warn("Item Name is completely same as that in repository!");
    }
    this.itemName = itemName;
    return this;
  }
}
