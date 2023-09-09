package zoeque.limitchecker.testtool;

import org.springframework.stereotype.Service;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;

/**
 * The service class to drop the all data in H2DB.
 */
@Service
public class DatabaseDropService {
  IStoredItemRepository repository;

  public DatabaseDropService(IStoredItemRepository repository) {
    this.repository = repository;
  }

  /**
   * Delete all data inserted in {@link zoeque.limitchecker.domain.entity.StoredItem}.
   */
  public void deleteAllData() {
    repository.deleteAll();
  }
}
