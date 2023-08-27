package zoeque.limitchecker.domain.entity;

import io.vavr.control.Try;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zoeque.limitchecker.domain.entity.valueobject.ItemDetail;
import zoeque.limitchecker.domain.entity.valueobject.StoredItemIdentifier;
import zoeque.limitchecker.domain.model.AlertStatusFlag;

@Entity
@Slf4j
@Getter
@NoArgsConstructor
@Table(name = "stored_item")
public class StoredItem {
  @Id
  @Embedded
  @EqualsAndHashCode.Include
  StoredItemIdentifier identifier;
  @Embedded
  ItemDetail itemDetail;
  @Enumerated(EnumType.STRING)
  AlertStatusFlag alertStatusFlag;

  public StoredItem(StoredItemIdentifier identifier,
                    ItemDetail itemDetail,
                    AlertStatusFlag alertStatusFlag) {
    setStoredItemIdentifier(identifier);
    setItemDetail(itemDetail);
    setAlertStatusFlag(alertStatusFlag);
  }

  private void setStoredItemIdentifier(StoredItemIdentifier identifier) {
    if (identifier == null) {
      throw new IllegalArgumentException("Identifier must not be null");
    }
    this.identifier = identifier;
  }

  private void setItemDetail(ItemDetail itemDetail) {
    if (itemDetail == null) {
      throw new IllegalArgumentException("Item detail must not be null");
    }
    this.itemDetail = itemDetail;
  }

  private void setAlertStatusFlag(AlertStatusFlag alertStatusFlag) {
    if (alertStatusFlag == null) {
      throw new IllegalArgumentException("Alert status flag must not be null");
    }
    this.alertStatusFlag = alertStatusFlag;
  }

  /**
   * Change the status of alert
   *
   * @param alertStatusFlag new status
   * @return result with new Item instance
   */
  public Try<StoredItem> changeItemStatus(AlertStatusFlag alertStatusFlag) {
    if (this.alertStatusFlag == alertStatusFlag) {
      log.info("The status is same:{}", this.itemDetail.getItemName().getName()
              + alertStatusFlag);
      return Try.failure(new IllegalArgumentException());
    }
    setAlertStatusFlag(alertStatusFlag);
    return Try.success(this);
  }
}
