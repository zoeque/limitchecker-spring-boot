package zoeque.limitchecker.configuration.mail;

import io.vavr.control.Try;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import zoeque.limitchecker.application.service.mailer.IMailService;
import zoeque.limitchecker.domain.model.MailServiceProviderModel;

/**
 * The class to find the mail service to use
 */
@Component
public class MailServiceCollector {
  MailServiceBeanConfig beanConfig;
  BeanFactory beanFactory;

  /**
   * Constructor
   *
   * @param beanConfig  The configuration class to collect {@link IMailService}
   * @param beanFactory The factory class to create the bean
   */
  public MailServiceCollector(MailServiceBeanConfig beanConfig,
                              BeanFactory beanFactory) {
    this.beanConfig = beanConfig;
    this.beanFactory = beanFactory;
  }

  /**
   * Find the mail service to use
   *
   * @param model The model of the mail service provider
   * @return The mail service to use
   */
  public Try<IMailService> findMailService(MailServiceProviderModel model) {
    return Try.success(
            (IMailService) beanFactory.getBean(beanConfig.getServiceMap().get(model)));
  }
}
