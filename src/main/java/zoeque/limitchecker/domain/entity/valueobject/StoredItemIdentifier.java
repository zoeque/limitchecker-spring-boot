package zoeque.limitchecker.domain.entity.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zoeque.limitchecker.domain.entity.StoredItem;

/**
 * Identifier for {@link StoredItem}.
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@Embeddable
public class StoredItemIdentifier {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  String identifier;
  public StoredItemIdentifier(String identifier) {
    setIdentifier(identifier);
  }

  private void setIdentifier(String identifier) {
    if (identifier == null) {
      throw new IllegalArgumentException("Identifier of item cannot be null!");
    }
    this.identifier = identifier;
  }
}
