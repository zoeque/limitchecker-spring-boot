package zoeque.limitchecker.testtool;

import org.springframework.stereotype.Service;
import zoeque.limitchecker.domain.repository.StoredItemRepositoryImpl;

/**
 * The service class to drop the all data in H2DB.
 */
@Service
public class DatabaseDropService {
  StoredItemRepositoryImpl repository;

  public DatabaseDropService(StoredItemRepositoryImpl repository) {
    this.repository = repository;
  }

  /**
   * Delete all data inserted in {@link zoeque.limitchecker.domain.entity.StoredItem}.
   */
  public void deleteAllData() {
    repository.deleteAll();
  }
}
