package zoeque.limitchecker.application.service.mailer;

import io.micrometer.common.util.StringUtils;
import io.vavr.control.Try;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import zoeque.limitchecker.application.dto.record.StoredItemDto;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.application.event.MailNotificationEvent;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Class to send e-mail
 */
@Slf4j
public abstract class AbstractMailSenderService {
  protected String toMailAddress;
  protected String fromMailAddress;
  JavaMailSender javaMailSender;

  public AbstractMailSenderService(@Value("${mail.address.to:null}") String toMailAddress,
                                   @Value("${mail.address.from:null}") String fromMailAddress,
                                   JavaMailSender javaMailSender) {
    this.toMailAddress = toMailAddress;
    this.fromMailAddress = fromMailAddress;
    this.javaMailSender = javaMailSender;
  }

  /**
   * The event listener for {@link MailNotificationEvent}.
   *
   * @param event with a list of {@link StoredItemDto} and its level
   *              described as {@link NotifyTypeModel}.
   */
  @EventListener
  public void sendMail(MailNotificationEvent event) {
    if (StringUtils.isEmpty(toMailAddress) || StringUtils.isEmpty(fromMailAddress)) {
      log.error("The mail address must not be null!! Failed to send email!!");
      return;
    }
    sendMailToUser(buildSubject(event), buildMessage(event));
  }

  /**
   * Send e-mail with full parameter in application.properties
   */
  protected Try<String> sendMailToUser(String subject,
                                       String messageContent) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(fromMailAddress);
      helper.setTo(toMailAddress);
      helper.setSubject(subject);
      helper.setText(messageContent);
      javaMailSender.send(message);
      return Try.success(messageContent);
    } catch (Exception e) {
      return Try.failure(e);
    }
  }

  private String buildMessage(MailNotificationEvent event) {
    AtomicReference<StringBuilder> reference = new AtomicReference<>();
    reference.get().append("消費期限管理アプリケーションより");
    if (event.getNotifyTypeModel() == NotifyTypeModel.WARN) {
      reference.get().append("消費期限が間近なものがあるため警告メールを送付しています。\r");
    } else if (event.getNotifyTypeModel() == NotifyTypeModel.ALERT) {
      reference.get().append("消費期限経過の通知メールを送付します。\r");
    }
    reference.get().append("以下が対象になります。\r");

    event.getItemList().forEach(itemDto -> {
      reference.get().append(itemDto.itemDetail().itemName());
      reference.get().append(" : ");
      reference.get().append(itemDto.itemDetail().expirationDate());
      reference.get().append("\r");
    });
    return reference.get().toString();
  }

  private String buildSubject(MailNotificationEvent event) {
    return event.getNotifyTypeModel() == NotifyTypeModel.WARN ? "【注意】消費期限間近なものがあります。"
            : "【警告】消費期限が経過したものがあります。";
  }
}
