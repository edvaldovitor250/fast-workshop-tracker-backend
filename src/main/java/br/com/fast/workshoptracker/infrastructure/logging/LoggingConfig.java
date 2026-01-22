package br.com.fast.workshoptracker.infrastructure.logging;

import org.springframework.context.annotation.Configuration;

/**
 * Configuração de logs estruturados.
 * 
 * Para ambiente de produção, considere usar:
 * - Logback com encoder JSON (logstash-logback-encoder)
 * - ELK Stack (Elasticsearch, Logstash, Kibana)
 * - CloudWatch Logs (AWS)
 * - Application Insights (Azure)
 * 
 * Exemplo de configuração adicional em logback-spring.xml:
 * 
 * <configuration>
 *   <appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
 *     <encoder class="net.logstash.logback.encoder.LogstashEncoder">
 *       <includeMdcKeyName>requestId</includeMdcKeyName>
 *       <includeMdcKeyName>userId</includeMdcKeyName>
 *       <includeMdcKeyName>method</includeMdcKeyName>
 *       <includeMdcKeyName>path</includeMdcKeyName>
 *     </encoder>
 *   </appender>
 * </configuration>
 */
@Configuration
public class LoggingConfig {
	
	// Configurações adicionais de logging podem ser adicionadas aqui
	// Por exemplo, customização de níveis de log por pacote
}
