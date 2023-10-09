package zoeque.limitchecker.application.service.mailer;

import io.micrometer.common.util.StringUtils;
import io.vavr.control.Try;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import zoeque.limitchecker.application.dto.record.StoredItemDto;
import zoeque.limitchecker.configuration.mail.MailServiceCollector;
import zoeque.limitchecker.domain.model.MailServiceProviderModel;
import zoeque.limitchecker.domain.model.NotifyTypeModel;
import zoeque.limitchecker.application.event.MailNotificationEvent;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Class to send e-mail
 */
@Slf4j
public abstract class AbstractMailSenderService implements IMailService {
  protected String toMailAddress;
  protected String fromMailAddress;
  protected MailServiceProviderModel model;
  MailServiceCollector collector;

  public AbstractMailSenderService(@Value("${zoeque.limitchecker.mail.address.to:null}")
                                   String toMailAddress,
                                   @Value("${zoeque.limitchecker.mail.address.from:null}")
                                   String fromMailAddress,
                                   @Value("${zoeque.limitchecker.mail.provider:GMAIL}")
                                   MailServiceProviderModel model,
                                   MailServiceCollector collector) {
    this.toMailAddress = toMailAddress;
    this.fromMailAddress = fromMailAddress;
    this.model = model;
    this.collector = collector;
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
    collector.findMailService(model)
            .get()
            .sendMailToUser(buildSubject(event), buildMessage(event));
  }

  protected String buildMessage(MailNotificationEvent event) {
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

  protected String buildSubject(MailNotificationEvent event) {
    return event.getNotifyTypeModel() == NotifyTypeModel.WARN ? "【注意】消費期限間近なものがあります。"
            : "【警告】消費期限が経過したものがあります。";
  }
}
