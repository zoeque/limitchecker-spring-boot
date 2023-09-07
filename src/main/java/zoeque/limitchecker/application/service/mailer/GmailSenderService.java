package zoeque.limitchecker.application.service.mailer;

import org.springframework.stereotype.Service;
import zoeque.limitchecker.configuration.MailService;
import zoeque.limitchecker.configuration.MailServiceProviderModel;

@Service
@MailService(v = MailServiceProviderModel.GMAIL)
public class GmailSenderService {
}
