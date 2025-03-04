package social.network.microservice_friend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import social.network.microservice_friend.configuration.factory.YamlPropertySourceFactory;

@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "application-localhost.yml", factory = YamlPropertySourceFactory.class)   //    application-social-network.yml        application-localhost.yml
public class YamlFooProperties {
}

