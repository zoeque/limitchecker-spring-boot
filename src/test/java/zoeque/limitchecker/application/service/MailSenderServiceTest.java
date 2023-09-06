package zoeque.limitchecker.application.service;

import io.vavr.control.Try;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MailSenderServiceTest {
  @Mock
  JavaMailSender mockMailSender;
  @Autowired
  MailSenderService autowiredMailSenderService;

  @Test
  public void givenMockMailServiceAndMail_thenSendSuccess() {
    MailSenderService service
            = new MailSenderService("foo", "bar", mockMailSender);
    MimeMessage dummyMessage = new MimeMessage((Session) null);
    when(mockMailSender.createMimeMessage()).thenReturn(dummyMessage);
    Try<String> sendTry = service.sendMailToUser("test", "test");
    Assertions.assertTrue(sendTry.isSuccess());
  }

  /**
   * The test method to send real e-mail message
   * that defined in application.properties.
   */
  @Test
  @Disabled
  public void givenMailServiceAndMail_thenSendSuccess() {
    String subject = "【テスト】このメールは消費期限管理アプリケーションからのテスト配信メールです。";
    String message = """
                     本メールには個人情報が含まれる場合がございます。
                     本メールに心当たりがございませんでしたら、お手数をおかけしますが転送や複製は行わず、
                     本メールへの返信の上、削除していただきますようお願いいたします。
                     
                     /** 消費期限管理アプリケーション **/
    """;
    Try<String> sendTry = autowiredMailSenderService.sendMailToUser(subject, message);
    Assertions.assertTrue(sendTry.isSuccess());
  }
}
