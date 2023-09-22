package zoeque.limitchecker.adapter;

import io.vavr.control.Try;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import zoeque.limitchecker.application.dto.record.StoredItemJsonDto;
import zoeque.limitchecker.application.service.usecase.StoredItemService;
import zoeque.limitchecker.domain.entity.StoredItem;

@RestController
@CrossOrigin(origins = "*")
@Component
public class StoredItemController {
  public StoredItemController(StoredItemService service) {
    this.service = service;
  }

  StoredItemService service;

  @PostMapping("/create")
  public ResponseEntity createNewStoredItem(@RequestBody StoredItemJsonDto dto) {
    Try<StoredItem> createTry = service.createNewStoredItem(dto);
    if (createTry.isFailure()) {
      return ResponseEntity.badRequest().body(createTry.getCause());
    }
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/find")
  public ResponseEntity findAllStoredItem() {
    Try<List<StoredItemJsonDto>> findTry = service.findAllStoredItem();
    if (findTry.isFailure()) {
      return ResponseEntity.badRequest().body(findTry.getCause());
    }
    return ResponseEntity.ok(findTry.get());
  }
}
