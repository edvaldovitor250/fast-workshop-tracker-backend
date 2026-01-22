# 📊 Queries Úteis - Prometheus

## Métricas de Negócio

### Total de Atas Criadas
```promql
atas_criadas_total
```

### Taxa de Criação de Atas (últimos 5 minutos)
```promql
rate(atas_criadas_total[5m]) * 60
```

### Colaboradores Adicionados vs Removidos
```promql
rate(atas_colaboradores_adicionados_total[5m])
rate(atas_colaboradores_removidos_total[5m])
```

### Logins Bem-sucedidos vs Falhados
```promql
rate(auth_login_total{status="success"}[5m])
rate(auth_login_total{status="failed"}[5m])
```

## Performance

### Tempo Médio de Operações
```promql
rate(business_operation_duration_seconds_sum[5m]) / rate(business_operation_duration_seconds_count[5m])
```

### Percentil 95 de Duração
```promql
histogram_quantile(0.95, rate(business_operation_duration_seconds_bucket[5m]))
```

### Percentil 99 de Duração
```promql
histogram_quantile(0.99, rate(business_operation_duration_seconds_bucket[5m]))
```

### Operações mais lentas
```promql
topk(5, 
  rate(business_operation_duration_seconds_sum[5m]) / 
  rate(business_operation_duration_seconds_count[5m])
) by (operation)
```

## Cache

### Hit Rate Global
```promql
sum(rate(cache_hits_total[5m])) / 
(sum(rate(cache_hits_total[5m])) + sum(rate(cache_misses_total[5m])))
```

### Hit Rate por Cache
```promql
rate(cache_hits_total[5m]) / 
(rate(cache_hits_total[5m]) + rate(cache_misses_total[5m]))
```

### Cache Efficiency (%)
```promql
(rate(cache_hits_total[5m]) / 
(rate(cache_hits_total[5m]) + rate(cache_misses_total[5m]))) * 100
```

## Resiliência

### Circuit Breaker State
```promql
resilience4j_circuitbreaker_state
```

### Circuit Breaker - Taxa de Falha
```promql
rate(resilience4j_circuitbreaker_calls_seconds_count{kind="failed"}[5m]) / 
rate(resilience4j_circuitbreaker_calls_seconds_count[5m]) * 100
```

### Retry - Tentativas
```promql
rate(resilience4j_retry_calls_seconds_count[5m])
```

### Rate Limiter - Requisições Bloqueadas
```promql
rate(resilience4j_ratelimiter_calls_seconds_count{kind="failed"}[5m])
```

## HTTP

### Requests por Segundo
```promql
rate(http_server_requests_seconds_count[1m])
```

### Requests por Status Code
```promql
rate(http_server_requests_seconds_count[1m]) by (status)
```

### Tempo de Resposta P95 por Endpoint
```promql
histogram_quantile(0.95, 
  sum(rate(http_server_requests_seconds_bucket[5m])) by (uri, le)
)
```

### Endpoints mais chamados
```promql
topk(10, rate(http_server_requests_seconds_count[5m])) by (uri)
```

### Taxa de Erro (4xx e 5xx)
```promql
sum(rate(http_server_requests_seconds_count{status=~"4..|5.."}[5m])) / 
sum(rate(http_server_requests_seconds_count[5m])) * 100
```

## Database

### Conexões Ativas
```promql
hikaricp_connections_active
```

### Conexões Idle
```promql
hikaricp_connections_idle
```

### Utilização do Pool (%)
```promql
(hikaricp_connections_active / hikaricp_connections) * 100
```

### Tempo de Aquisição de Conexão
```promql
rate(hikaricp_connections_acquire_seconds_sum[5m]) / 
rate(hikaricp_connections_acquire_seconds_count[5m])
```

### Timeout de Conexão
```promql
rate(hikaricp_connections_timeout_total[5m])
```

## JVM

### Heap Memory Used (MB)
```promql
jvm_memory_used_bytes{area="heap"} / 1024 / 1024
```

### Heap Memory Usage (%)
```promql
(jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"}) * 100
```

### GC Pause Time
```promql
rate(jvm_gc_pause_seconds_sum[5m])
```

### Threads Ativos
```promql
jvm_threads_live_threads
```

### CPU Usage (%)
```promql
process_cpu_usage * 100
```

## Erros

### Erros por Minuto
```promql
rate(errors_total[1m]) * 60
```

### Top Erros
```promql
topk(10, rate(errors_total[5m])) by (type, code)
```

### Erros de Negócio
```promql
rate(errors_total{type="business"}[5m])
```

### Erros Técnicos
```promql
rate(errors_total{type="technical"}[5m])
```

## Queries Compostas

### SLA Compliance (< 1s para 95% das requisições)
```promql
histogram_quantile(0.95, 
  sum(rate(http_server_requests_seconds_bucket[5m])) by (le)
) < 1
```

### Health Score (0-100)
```promql
(
  # 40% - Taxa de sucesso
  (1 - (sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) / 
        sum(rate(http_server_requests_seconds_count[5m])))) * 40 +
  
  # 30% - Performance (p95 < 1s)
  (histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (le)) < 1) * 30 +
  
  # 30% - Cache efficiency
  (sum(rate(cache_hits_total[5m])) / 
   (sum(rate(cache_hits_total[5m])) + sum(rate(cache_misses_total[5m])))) * 30
) 
```

### System Load Score
```promql
(
  # CPU < 80%
  ((process_cpu_usage < 0.8) * 25) +
  
  # Memory < 80%
  ((jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} < 0.8) * 25) +
  
  # DB Pool < 80%
  ((hikaricp_connections_active / hikaricp_connections < 0.8) * 25) +
  
  # Circuit Breaker Closed
  ((resilience4j_circuitbreaker_state == 0) * 25)
)
```

## Alertas Recomendados

### Alta Taxa de Erro (> 5%)
```promql
sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) / 
sum(rate(http_server_requests_seconds_count[5m])) * 100 > 5
```

### P95 Alto (> 2s)
```promql
histogram_quantile(0.95, 
  sum(rate(http_server_requests_seconds_bucket[5m])) by (le)
) > 2
```

### Circuit Breaker Aberto
```promql
resilience4j_circuitbreaker_state == 1
```

### Pool de Conexões Esgotado (> 90%)
```promql
(hikaricp_connections_active / hikaricp_connections) * 100 > 90
```

### Memory Usage Alto (> 85%)
```promql
(jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"}) * 100 > 85
```

### Cache Hit Rate Baixo (< 70%)
```promql
(sum(rate(cache_hits_total[5m])) / 
(sum(rate(cache_hits_total[5m])) + sum(rate(cache_misses_total[5m])))) * 100 < 70
```

---

## 💡 Dicas

1. **Use intervalos adequados**: `[5m]` para overview, `[1m]` para alertas
2. **Rate vs Increase**: `rate()` para taxa por segundo, `increase()` para total no período
3. **Histogram_quantile**: Use para percentis (p50, p95, p99)
4. **TopK/BottomK**: Para encontrar top/bottom N valores
5. **By clause**: Para agrupar por labels
6. **Without clause**: Para excluir labels do agrupamento

## 🔗 Recursos

- [PromQL Documentation](https://prometheus.io/docs/prometheus/latest/querying/basics/)
- [PromQL Examples](https://prometheus.io/docs/prometheus/latest/querying/examples/)
- [Grafana Dashboards](https://grafana.com/grafana/dashboards/)
