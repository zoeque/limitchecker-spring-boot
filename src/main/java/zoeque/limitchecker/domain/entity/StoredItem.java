package zoeque.limitchecker.domain.entity;

import io.vavr.control.Try;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zoeque.limitchecker.domain.entity.valueobject.ItemDetail;
import zoeque.limitchecker.domain.model.AlertStatusFlag;

/**
 * StoredItem is an entity of the food item.
 * {@link AlertStatusFlag} is a variable status about reporting the item.
 * Other fields perform as a value object.
 */
@Entity
@Slf4j
@Getter
@NoArgsConstructor
@Table(name = "stored_item")
public class StoredItem {
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.AUTO)
  long identifier;
  @Embedded
  ItemDetail itemDetail;
  @Enumerated(EnumType.STRING)
  AlertStatusFlag alertStatusFlag;

  public StoredItem(ItemDetail itemDetail,
                    AlertStatusFlag alertStatusFlag) {
    setItemDetail(itemDetail);
    setAlertStatusFlag(alertStatusFlag);
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
