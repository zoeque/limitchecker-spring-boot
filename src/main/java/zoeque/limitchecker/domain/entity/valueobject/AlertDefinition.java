package zoeque.limitchecker.domain.entity.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class AlertDefinition {
  Integer alertDate;

  public AlertDefinition(Integer alertDate) {
    setAlertDate(alertDate);
  }

  private void setAlertDate(Integer alertDate) {
    if (alertDate == null || alertDate <= 0) {
      throw new IllegalArgumentException("Invalid expire date is set! : " + alertDate);
    }
    this.alertDate = alertDate;
  }
}
