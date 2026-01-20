package br.com.fast.workshoptracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "api.security.jwt")
public class JwtProperties {

	private String secret = "change-me-in-production-change-me-in-production";
	private String issuer = "workshop-tracker";
	private Duration ttl = Duration.ofHours(2);

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public Duration getTtl() {
		return ttl;
	}

	public void setTtl(Duration ttl) {
		this.ttl = ttl;
	}
}

