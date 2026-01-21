package br.com.fast.workshoptracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "api.security.jwt")
@Getter
@Setter
public class JwtProperties {

	private String secret = "change-me-in-production-change-me-in-production";
	private String issuer = "workshop-tracker";
	private Duration ttl = Duration.ofHours(2);
}
