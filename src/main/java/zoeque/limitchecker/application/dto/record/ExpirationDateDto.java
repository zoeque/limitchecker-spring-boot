package zoeque.limitchecker.application.dto.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import zoeque.limitchecker.application.dto.IExpirationDateDto;

@JsonDeserialize
public record ExpirationDateDto(LocalDateTime date) implements IExpirationDateDto {
  public LocalDateTime getDate() {
    return this.date;
  }
}
