package zoeque.limitchecker.domain.model;

import lombok.Getter;

/**
 * The enum class to use {@link MailService} annotation.
 */
public enum MailServiceProviderModel {
  GMAIL("gmail"), OTHERS("others");
  @Getter
  String provider;

  MailServiceProviderModel(String provider) {
    this.provider = provider;
  }
}
