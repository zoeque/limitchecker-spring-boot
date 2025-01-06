package zoeque.limitchecker.adapter;

import io.vavr.control.Try;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zoeque.limitchecker.application.dto.record.StoredItemJsonDto;
import zoeque.limitchecker.application.service.usecase.StoredItemCreateService;
import zoeque.limitchecker.application.service.usecase.StoredItemDropService;
import zoeque.limitchecker.domain.entity.StoredItem;

@RestController
@CrossOrigin(origins = "*")
@Component
public class StoredItemController {
  StoredItemCreateService service;

  StoredItemDropService dropService;

  public StoredItemController(StoredItemCreateService service) {
    this.service = service;
  }

  /**
   * The method to create a new stored item.
   *
   * @param dto The target stored item to create.
   * @return The response entity with the result of the creation process.
   */
  @PostMapping("/create")
  public ResponseEntity createNewStoredItem(@RequestBody StoredItemJsonDto dto) {
    Try<StoredItem> createTry = service.createNewStoredItem(dto);
    if (createTry.isFailure()) {
      return ResponseEntity.badRequest().body(createTry.getCause());
    }
    return ResponseEntity.ok(dto);
  }

  /**
   * The method to find all stored items.
   *
   * @return The response entity with the list of stored items.
   */
  @GetMapping("/find")
  public ResponseEntity findAllStoredItem() {
    Try<List<StoredItemJsonDto>> findTry = service.findAllStoredItem();
    if (findTry.isFailure()) {
      return ResponseEntity.badRequest().body(findTry.getCause());
    }
    return ResponseEntity.ok(findTry.get());
  }

  /**
   * The method to drop the stored item.
   *
   * @param dto The target stored item to drop.
   * @return The response entity with the result of the drop process.
   */
  @PostMapping("/drop")
  public ResponseEntity dropStoredItem(@RequestBody StoredItemJsonDto dto) {
    Try<Long> dropTry = dropService.dropRequestedStoredItem(dto.storedItemIdentifier());
    if (dropTry.isFailure()) {
      return ResponseEntity.badRequest().body(dropTry.getCause());
    }
    return ResponseEntity.ok(dropTry.get());
  }
}
