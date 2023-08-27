package zoeque.limitchecker.domain.entity.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@Embeddable
public class ItemName {
  String name;

  public ItemName(String name) {
    setName(name);
  }

  private void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Identifier of item cannot be null!");
    }
    this.name = name;
  }
}
