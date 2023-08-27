package zoeque.limitchecker.application.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.application.event.NotifyEvent;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Class to send e-mail
 */
public class MailSenderService {

  @Value("${mail.address.to:null}")
  String toMailAddress;
  @Value("${mail.address.from:null}")
  String fromMailAddress;
  @Autowired(required = true)
  JavaMailSender javaMailSender;


  @EventListener
  public void sendMail(NotifyEvent event) {
    sendMailToUser(buildSubject(event), buildMessage(event));
  }

  /**
   * Send e-mail with full parameter in application.properties
   */
  public void sendMailToUser(String subject, String messageContent) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      if (toMailAddress == null || fromMailAddress == null) {
        throw new IllegalArgumentException("Mail address must not be null");
      }
      helper.setFrom(fromMailAddress);
      helper.setTo(toMailAddress);
      helper.setSubject(subject);
      helper.setText(messageContent);
      javaMailSender.send(message);
    } catch (Exception e) {
      // TODO Exception handling
    }
  }

  private String buildMessage(NotifyEvent event) {
    AtomicReference<StringBuilder> reference = new AtomicReference<>();
    reference.get().append("消費期限管理アプリケーションより");
    if (event.getNotifyTypeModel() == NotifyTypeModel.WARN) {
      reference.get().append("消費期限が間近なものがあるため警告メールを送付しています。\r");
    } else if (event.getNotifyTypeModel() == NotifyTypeModel.ALERT) {
      reference.get().append("消費期限経過の通知メールを送付します。\r");
    }
    reference.get().append("以下が対象になります。\r");

    event.getItemList().forEach(itemDto -> {
      reference.get().append(itemDto.getItemName());
      reference.get().append(" : ");
      reference.get().append(itemDto.getExpirationDate());
      reference.get().append("\r");
    });
    return reference.get().toString();
  }

  private String buildSubject(NotifyEvent event) {
    return event.getNotifyTypeModel() == NotifyTypeModel.WARN ? "【注意】消費期限間近なものがあります。"
            : "【警告】消費期限が経過したものがあります。";
  }
}
