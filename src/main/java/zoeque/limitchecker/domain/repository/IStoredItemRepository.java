package zoeque.limitchecker.domain.repository;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zoeque.limitchecker.domain.entity.StoredItem;

@Repository
public interface IStoredItemRepository
        extends JpaRepository<StoredItem, String>,
        JpaSpecificationExecutor<StoredItem> {
  List<StoredItem> findAll(Specification<StoredItem> spec);
}
