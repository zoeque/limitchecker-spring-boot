package zoeque.limitchecker.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zoeque.limitchecker.domain.entity.StoredItem;

/**
 * The repository class to access the stored item.
 */
@Repository
public interface IStoredItemRepository
        extends JpaRepository<StoredItem, String>,
        JpaSpecificationExecutor<StoredItem> {
  /**
   * Find all stored items by the specification.
   *
   * @param spec The specification of {@link StoredItem}, must not be {@literal null}.
   * @return the list of stored items
   */
  List<StoredItem> findAll(Specification<StoredItem> spec);

  /**
   * Find the stored item by the identifier.
   *
   * @param id The identifier of the stored item
   * @return the stored item
   */
  Optional<StoredItem> findByIdentifier(long id);
}
