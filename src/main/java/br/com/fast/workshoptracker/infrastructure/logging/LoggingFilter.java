package br.com.fast.workshoptracker.infrastructure.logging;

import br.com.fast.workshoptracker.domain.exception.core.BaseExpeditiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro para adicionar contexto estruturado aos logs (MDC).
 */
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final String REQUEST_ID = "requestId";
	private static final String USER_ID = "userId";
	private static final String METHOD = "method";
	private static final String PATH = "path";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestId = UUID.randomUUID().toString();
		MDC.put(REQUEST_ID, requestId);
		MDC.put(METHOD, request.getMethod());
		MDC.put(PATH, request.getRequestURI());

		// Adiciona requestId no header da resposta para rastreamento
		response.setHeader("X-Request-ID", requestId);

		long startTime = System.currentTimeMillis();

		try {
			filterChain.doFilter(request, response);
		} finally {
			long duration = System.currentTimeMillis() - startTime;
			int status = response.getStatus();

			log.info("Request completed: method={} path={} status={} duration={}ms",
					request.getMethod(),
					request.getRequestURI(),
					status,
					duration);

			MDC.clear();
		}
	}
}
