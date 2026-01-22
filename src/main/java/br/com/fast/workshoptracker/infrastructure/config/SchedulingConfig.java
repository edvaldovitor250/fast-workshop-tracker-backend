package br.com.fast.workshoptracker.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilita scheduling para tarefas agendadas como logs de cache.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
