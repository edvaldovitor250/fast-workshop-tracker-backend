package br.com.fast.workshoptracker.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.fast.workshoptracker.infrastructure.util.FlexibleLocalDateFormatter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new FlexibleLocalDateFormatter());
	}
}
