package zoeque.limitchecker.adapter;

import io.vavr.control.Try;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zoeque.limitchecker.application.dto.record.StoredItemJsonDto;
import zoeque.limitchecker.application.service.usecase.StoredItemService;
import zoeque.limitchecker.domain.entity.StoredItem;

@RestController("/item")
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
}
