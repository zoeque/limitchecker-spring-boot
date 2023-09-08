package zoeque.limitchecker.application.service.mailer;

import io.vavr.control.Try;

/**
 * The interface for mail services
 */
public interface IMailService {
  /**
   * Send e-mail with full parameter in application.properties
   */
   Try<String> sendMailToUser(String subject, String messageContent);
}
