package zoeque.limitchecker.domain.entity.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class ExpirationDate {
  @Column(name = "expiration_date_for_item")
  LocalDateTime date;

  public ExpirationDate(LocalDateTime expirationDate) {
    setExpirationDate(expirationDate);
  }

  private void setExpirationDate(LocalDateTime date) {
    if (date == null) {
      throw new IllegalArgumentException("Invalid expire date is set! : " + date);
    }
    this.date = date;
  }
}
