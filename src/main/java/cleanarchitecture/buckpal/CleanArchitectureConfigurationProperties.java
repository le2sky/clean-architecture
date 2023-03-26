package cleanarchitecture.buckpal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "buckpal")
public class CleanArchitectureConfigurationProperties {

  private long transferThreshold = Long.MAX_VALUE;
}
