package zoeque.limitchecker.application.service.mailer;

import org.springframework.stereotype.Service;
import zoeque.limitchecker.domain.model.MailService;
import zoeque.limitchecker.domain.model.MailServiceProviderModel;

@Service
@MailService(MailServiceProviderModel.GMAIL)
public class GmailSenderService {
}
