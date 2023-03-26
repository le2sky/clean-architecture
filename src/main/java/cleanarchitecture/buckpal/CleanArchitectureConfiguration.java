package cleanarchitecture.buckpal;

import cleanarchitecture.buckpal.account.application.service.MoneyTransferProperties;
import cleanarchitecture.buckpal.account.domain.Money;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CleanArchitectureConfigurationProperties.class)
public class CleanArchitectureConfiguration {

  @Bean
  public MoneyTransferProperties moneyTransferProperties(
      CleanArchitectureConfigurationProperties cleanArchitectureConfigurationProperties) {
    return new MoneyTransferProperties(
        Money.of(cleanArchitectureConfigurationProperties.getTransferThreshold()));
  }
}
