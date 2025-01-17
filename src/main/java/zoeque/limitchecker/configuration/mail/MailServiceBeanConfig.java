package zoeque.limitchecker.configuration.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import zoeque.limitchecker.application.service.mailer.IMailService;
import zoeque.limitchecker.domain.model.MailService;
import zoeque.limitchecker.domain.model.MailServiceProviderModel;

/**
 * The configuration class to collect {@link IMailService}.
 */
@Component
public class MailServiceBeanConfig {

  @Getter
  Map<MailServiceProviderModel, Class<?>> serviceMap;

  /**
   * The mapping method to collect {@link IMailService}.
   *
   * @param serviceSet the set of {@link IMailService}
   */
  public void serviceMap(Set<IMailService> serviceSet) {
    serviceMap = new HashMap<>();

    ClassPathScanningCandidateComponentProvider provider
            = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(MailService.class));
    Set<BeanDefinition> beanSet = provider.findCandidateComponents("zoeque.limitchecker");

    beanSet.forEach(beanDefinition -> {
      Class<?> clazz = null;
      try {
        clazz = Class.forName(beanDefinition.getBeanClassName());
        MailService annotation = clazz.getAnnotation(MailService.class);
        serviceMap.put(annotation.value(), clazz);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
