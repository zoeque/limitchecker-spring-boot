package zoeque.limitchecker.application.service.mailer;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.configuration.MailService;
import zoeque.limitchecker.configuration.MailServiceProviderModel;

/**
 * The class to send the mail via {@link JavaMailSender}.
 */
@Service
@MailService(v = MailServiceProviderModel.OTHERS)
public class MailSenderService extends AbstractMailSenderService{
  public MailSenderService(String toMailAddress,
                           String fromMailAddress,
                           JavaMailSender javaMailSender) {
    super(toMailAddress, fromMailAddress, javaMailSender);
  }
}
