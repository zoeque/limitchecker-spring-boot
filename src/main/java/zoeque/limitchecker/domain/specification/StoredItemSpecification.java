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
import zoeque.limitchecker.domain.entity.valueobject.ExpirationDate_;
import zoeque.limitchecker.domain.entity.valueobject.ItemDetail_;
import zoeque.limitchecker.domain.entity.valueobject.ItemType_;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;

/**
 * The specification class for {@link zoeque.limitchecker.domain.entity.StoredItem}.
 * The query process can be called by findAll method on
 * {@link IStoredItemRepository}.
 *
 * @param <StoredItem> Items to be managed.
 */
@Component
public class StoredItemSpecification<StoredItem> {
  /**
   * Find the standard items, which has an expired date, that has been expired.
   * Items are returned as Expired items and must be notified to the user.
   *
   * @param model {@link ItemTypeModel} with the definition for item attribute.
   * @return {@link StoredItem} with an expired date
   */
  public Specification<StoredItem> expiredStandardItemByItemType(ItemTypeModel model) {
    return new Specification<StoredItem>() {
      @Override
      public Predicate toPredicate(Root<StoredItem> root, CriteriaQuery<?> query,
                                   CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.lessThan(
                root.get(StoredItem_.ITEM_DETAIL)
                        .get(ItemDetail_.EXPIRATION_DATE).get(ExpirationDate_.DATE),
                LocalDateTime.now()));
        predicates.add(criteriaBuilder.equal(root.get(StoredItem_.ITEM_DETAIL)
                .get(ItemDetail_.ITEM_TYPE).get(ItemType_.MODEL), model));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      }
    };
  }

  /**
   * Find the items, which has no expired date, that has been expired.
   * Items are returned as Expired items and must be notified to the user.
   *
   * @param model {@link ItemTypeModel} with the expiration definition.
   * @return {@link StoredItem} with an expired date
   */
  public Specification<StoredItem> expiredFreshItemByItemType(ItemTypeModel model) {
    return new Specification<StoredItem>() {
      @Override
      public Predicate toPredicate(Root<StoredItem> root, CriteriaQuery<?> query,
                                   CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // :warnedDate = :today - (expiration date)/2
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(model.getExpirationDate());

        // WHERE :createdDate < :expirationDate
        predicates.add(criteriaBuilder.lessThan(root.get(StoredItem_.ITEM_DETAIL)
                .get(ItemDetail_.EXPIRATION_DATE)
                .get(ExpirationDate_.DATE), expirationDate));
        // AND item_type = :model
        predicates.add(criteriaBuilder.equal(root.get(StoredItem_.ITEM_DETAIL)
                .get(ItemDetail_.ITEM_TYPE).get(ItemType_.MODEL), model));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      }
    };
  }

  /**
   * Find the items that has been expired and reported.
   * This condition is specified that items have to be deleted.
   *
   * @return {@link StoredItem} list that have to be deleted.
   */
  public Specification<StoredItem> itemToDrop() {
    return new Specification<StoredItem>() {
      @Override
      public Predicate toPredicate(Root<StoredItem> root, CriteriaQuery<?> query,
                                   CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // WHERE expiration_date < :today
        predicates.add(criteriaBuilder.lessThan(
                root.get(StoredItem_.ITEM_DETAIL)
                        .get(ItemDetail_.EXPIRATION_DATE).get(ExpirationDate_.DATE),
                LocalDateTime.now()));

        // AND alert_status_flag = REPORTED
        predicates.add(criteriaBuilder.equal(root.get(StoredItem_.ALERT_STATUS_FLAG),
                AlertStatusFlag.REPORTED));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      }
    };
  }

  /**
   * Find the item that has specified the condition,
   * (expiration date)/2 > Today - (alert definition date).
   * Items are returned as warned items.
   * This method does not find the item which has an REPORTED state
   * of {@link AlertStatusFlag}.
   *
   * @param model The target {@link ItemTypeModel} to validate.
   * @return {@link StoredItem} with full specified the condition.
   */
  public Specification<StoredItem> warnedStandardItem(ItemTypeModel model) {
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

  /**
   * Find the item that has specified the condition,
   * Today  > (Created date) + (expiration date)/2.
   * Items are returned as warned items.
   * This method does not find the item which has an REPORTED state
   * of {@link AlertStatusFlag}.
   *
   * @param model The target {@link ItemTypeModel} to validate.
   * @return {@link StoredItem} with full specified the condition.
   */
  public Specification<StoredItem> warnedFreshItem(ItemTypeModel model) {
    return new Specification<StoredItem>() {
      @Override
      public Predicate toPredicate(Root<StoredItem> root, CriteriaQuery<?> query,
                                   CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // :warnedDate = :today - (expiration date)/2
        LocalDateTime warnedDate = LocalDateTime.now().minusDays(model.getExpirationDate() / 2);

        // WHERE :createdDate < :warnedDate
        predicates.add(criteriaBuilder.lessThan(root.get(StoredItem_.ITEM_DETAIL)
                .get(ItemDetail_.EXPIRATION_DATE)
                .get(ExpirationDate_.DATE), warnedDate));
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
