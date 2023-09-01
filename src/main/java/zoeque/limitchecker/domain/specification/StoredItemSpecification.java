package zoeque.limitchecker.domain.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import zoeque.limitchecker.domain.entity.StoredItem_;
import zoeque.limitchecker.domain.entity.valueobject.AlertDefinition;
import zoeque.limitchecker.domain.entity.valueobject.ExpirationDate_;
import zoeque.limitchecker.domain.entity.valueobject.ItemDetail_;
import zoeque.limitchecker.domain.entity.valueobject.ItemType_;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;

/**
 * The specification class for {@link zoeque.limitchecker.domain.entity.StoredItem}.
 * The query process can be called by findAll method on
 * {@link zoeque.limitchecker.domain.repository.IStoredItemRepository}.
 *
 * @param <StoredItem> Items to be managed.
 */
@Component
public class StoredItemSpecification<StoredItem> {
  /**
   * Find the items that has been expired.
   * Items are returned as Expired items and must be notified to the user.
   *
   * @return {@link StoredItem} with an expired date
   */
  public Specification<StoredItem> expiredItem() {
    return ((root, query, criteriaBuilder) -> {
      return criteriaBuilder.lessThan(
              root.get(StoredItem_.ITEM_DETAIL)
                      .get(ItemDetail_.EXPIRATION_DATE).get(ExpirationDate_.DATE),
              LocalDateTime.now());
    });
  }

  /**
   * Find the item that has specified the condition,
   * (expiration date) > Today - (alert definition date).
   * Items are returned as warned items.
   * This method does not find the item which has an REPORTED state
   * of {@link AlertStatusFlag}.
   *
   * @param model The target {@link ItemTypeModel} to validate.
   * @return {@link StoredItem} with full specified the condition.
   */
  public Specification<StoredItem> warnedItem(ItemTypeModel model) {
    return new Specification<StoredItem>() {
      @Override
      public Predicate toPredicate(Root<StoredItem> root, CriteriaQuery<?> query,
                                   CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // WHERE expiration_date > :today - alert definition date
        predicates.add(criteriaBuilder.greaterThan(root.get(StoredItem_.ITEM_DETAIL)
                        .get(ItemDetail_.EXPIRATION_DATE)
                        .get(ExpirationDate_.DATE),
                LocalDateTime.now().minusDays(model.getExpirationDate())));
        // AND alert_status_flag = NOT_REPORTED (not to include reported items)
        predicates.add(criteriaBuilder.equal(root.get(StoredItem_.ALERT_STATUS_FLAG),
                AlertStatusFlag.NOT_REPORTED));
        // AND item_type = :model
        predicates.add(criteriaBuilder.equal(root.get(StoredItem_.ITEM_DETAIL)
                .get(ItemDetail_.ITEM_TYPE).get(ItemType_.MODEL), model));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      }
    };
  }
}
