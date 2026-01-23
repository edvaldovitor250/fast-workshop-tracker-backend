package br.com.fast.workshoptracker.application.port.output;

import java.util.function.Supplier;

public interface MetricsPort {

	void incrementAtasCriadas();

	<T> T timeBusinessOperation(String operationName, Supplier<T> supplier);
}

