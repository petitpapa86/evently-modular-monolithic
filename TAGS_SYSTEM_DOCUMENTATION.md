# Evently Tags System Documentation

## Overview

The Evently application implements a comprehensive **tags-based observability system** that enables business-domain-aware monitoring, logging, and operational management. Tags serve as metadata labels attached to HTTP requests, providing contextual information about which business domain (Users, Events, Attendees, Ticketing) each request belongs to.

## Core Components

### 1. TagsLoggingFilter

**Location:** `com.evently.api.logging.TagsLoggingFilter`

**Purpose:** Intercepts HTTP requests to add business domain tags for observability.

**Key Features:**
- Automatic tag inference from URL patterns
- Structured logging with tag metadata
- Request categorization for monitoring
- Performance metrics collection

**Current Implementation:**
```java
@Component
public class TagsLoggingFilter extends OncePerRequestFilter {
    // Extracts tags from request attributes or infers from URL patterns
    private String[] inferTagsFromRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/attendees")) return new String[]{"Attendees"};
        if (requestURI.startsWith("/events")) return new String[]{"Events"};
        if (requestURI.startsWith("/ticketing")) return new String[]{"Ticketing"};
        if (requestURI.startsWith("/users")) return new String[]{"Users"};
        return new String[]{"General"};
    }
}
```

### 2. Tag Categories

| Tag | Business Domain | URL Pattern | Description |
|-----|----------------|-------------|-------------|
| `Users` | User Management | `/users/*` | User registration, profiles, authentication |
| `Events` | Event Management | `/events/*` | Event creation, updates, queries |
| `Attendees` | Attendance Tracking | `/attendees/*` | Attendee management, check-ins |
| `Ticketing` | Ticket Sales | `/ticketing/*` | Ticket purchases, validation |
| `General` | Fallback | All others | Default category for unmapped requests |

## Observability Benefits

### 1. Business Intelligence
- **Domain-specific KPIs:** Track metrics per business area
- **Usage Analytics:** Monitor feature adoption by domain
- **Performance Monitoring:** Identify slow domains for optimization
- **Revenue Tracking:** Monitor ticket sales and event creation metrics

### 2. Operational Management
- **Incident Response:** Faster root cause analysis with domain context
- **Capacity Planning:** Understand scaling needs per business area
- **Alerting:** Domain-specific alert policies and thresholds
- **SLA Monitoring:** Service level objectives by business domain

### 3. Troubleshooting
- **Log Filtering:** Isolate issues to specific business domains
- **Request Tracing:** Follow user journeys across domains
- **Performance Profiling:** Domain-specific performance analysis
- **Error Correlation:** Link errors to business impact

## GCP Deployment Integration

### Cloud Monitoring (Metrics)

**Custom Metrics with Tags:**
```java
// Metrics with tag dimensions
Counter.builder("http_requests_total")
    .tags("method", request.getMethod(),
          "uri", request.getRequestURI(),
          "status", String.valueOf(response.getStatus()),
          "tags", tagsString)
    .register(meterRegistry)
    .increment();
```

**Business Metrics:**
```java
// Domain-specific business metrics
meterRegistry.gauge("evently_events_created_total",
    Tags.of("domain", "Events"), eventCount);

meterRegistry.gauge("evently_tickets_sold_total",
    Tags.of("domain", "Ticketing"), ticketCount);
```

### Cloud Logging (Structured Logs)

**Enhanced Logging:**
```java
logger.info("Request completed",
    kv("method", request.getMethod()),
    kv("uri", request.getRequestURI()),
    kv("status", response.getStatus()),
    kv("duration_ms", duration),
    kv("tags", tagsString),
    kv("user_id", extractUserId(request))
);
```

**Log-based Metrics:**
```bash
# Create log-based metrics from tags
gcloud logging metrics create request_count_by_domain \
  --filter="jsonPayload.tags:*" \
  --metric-kind=DELTA \
  --value-extractor="1" \
  --label-extractors="domain=jsonPayload.tags"
```

### Cloud Monitoring Dashboards

**Sample Dashboard Configuration:**
```json
{
  "displayName": "Evently Business Metrics",
  "widgets": [
    {
      "title": "Requests by Domain",
      "xyChart": {
        "dataSets": [{
          "timeSeriesQuery": {
            "filter": "metric.type=\"custom.googleapis.com/http_requests_total\"",
            "aggregation": {
              "groupByFields": ["metric.labels.tags"]
            }
          }
        }]
      }
    }
  ]
}
```

### Alerting Policies

**Domain-specific Alerts:**
```bash
# Alert on high error rate for Events domain
gcloud monitoring alert-policies create events-high-error-rate \
  --condition-filter="metric.type=\"custom.googleapis.com/http_requests_total\"
                     AND metric.labels.tags=\"Events\"
                     AND metric.labels.status>=\"400\"" \
  --condition-threshold-value=0.05 \
  --notification-channels="[CHANNEL_ID]"
```

## Implementation Details

### Current Architecture

1. **Tag Inference:** URL pattern matching in `TagsLoggingFilter`
2. **Logging:** SLF4J with MDC for request context
3. **Metrics:** Spring Boot Actuator (ready for Micrometer integration)
4. **Security:** JWT tokens with permission-based access control

### Future Enhancements

#### 1. Advanced Tag Sources
```java
// Multiple tag sources
public enum TagSource {
    URL_PATTERN,     // Current implementation
    REQUEST_BODY,    // Extract from JSON payload
    USER_CONTEXT,    // From authenticated user
    BUSINESS_LOGIC,  // Set by service layer
    EXTERNAL_API     // From upstream services
}
```

#### 2. Hierarchical Tags
```java
// Multi-level tag hierarchy
public class TagHierarchy {
    private String domain;      // "Events"
    private String subdomain;   // "Creation", "Search"
    private String operation;   // "Create", "Update", "Delete"
    private String priority;    // "High", "Medium", "Low"
}
```

#### 3. Dynamic Tag Configuration
```yaml
# application.yml
tags:
  domains:
    - name: "Users"
      patterns: ["/users/*", "/auth/*"]
      priority: "high"
    - name: "Events"
      patterns: ["/events/*"]
      priority: "high"
  custom-tags:
    vip-users: "user.vip == true"
    beta-features: "request.header.beta == 'true'"
```

#### 4. Tag-based Routing
```java
// Route requests based on tags
@TagBasedRoute(tags = {"Users", "HighPriority"})
public void handleUserRequest() {
    // High-priority user operations
}
```

## Configuration

### Application Properties

```yaml
# Tags configuration
evently:
  tags:
    enabled: true
    inference:
      url-patterns: true
      request-attributes: true
    logging:
      include-duration: true
      include-user-id: true
    metrics:
      enabled: true
      custom-metrics: true
```

### Environment Variables

```bash
# GCP-specific configuration
GOOGLE_CLOUD_PROJECT=evently-prod
EVENTLY_TAGS_ENABLED=true
EVENTLY_METRICS_ENABLED=true
```

## Testing Strategy

### Unit Tests
```java
@Test
public void testTagInference() {
    // Test URL pattern matching
    assertEquals(new String[]{"Users"},
        filter.inferTagsFromRequest(createRequest("/users/profile")));
}

@Test
public void testTagLogging() {
    // Test structured logging with tags
    filter.doFilter(request, response, chain);
    // Verify MDC contains tags
}
```

### Integration Tests
```java
@Test
public void testEndToEndObservability() {
    // Make request and verify:
    // 1. Tags are logged
    // 2. Metrics are recorded
    // 3. Response contains proper headers
}
```

## Monitoring Queries

### BigQuery Analytics
```sql
-- Business intelligence queries
SELECT
  JSON_EXTRACT_SCALAR(labels, '$.domain') as domain,
  COUNT(*) as request_count,
  AVG(CAST(JSON_EXTRACT_SCALAR(jsonPayload, '$.duration_ms') AS INT64)) as avg_duration
FROM `evently-prod.cloud_run_logs`
WHERE DATE(timestamp) >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)
  AND JSON_EXTRACT_SCALAR(jsonPayload, '$.tags') IS NOT NULL
GROUP BY domain
ORDER BY request_count DESC
```

### Real-time Dashboards
- **Traffic Distribution:** Requests per domain over time
- **Error Rates:** 4xx/5xx rates by domain
- **Performance:** P95 response times by domain
- **Business KPIs:** Events created, tickets sold, users registered

## Best Practices

### Tag Naming Conventions
- Use PascalCase for tag names: `Users`, `Events`, `Ticketing`
- Keep tag names descriptive but concise
- Avoid special characters in tag names
- Use consistent naming across all domains

### Performance Considerations
- Tag inference should be fast (O(1) operations)
- Avoid complex regex patterns for URL matching
- Cache frequently used tag mappings
- Limit tag cardinality to prevent metric explosion

### Security Considerations
- Don't include sensitive information in tags
- Validate tag sources to prevent injection
- Audit tag usage for compliance
- Implement tag-based access controls if needed

## Troubleshooting

### Common Issues

1. **Tags not appearing in logs:**
   - Check if `TagsLoggingFilter` is registered
   - Verify URL patterns match expectations
   - Check MDC configuration

2. **Metrics not recording:**
   - Ensure Micrometer is configured
   - Check metric registry setup
   - Verify tag format compatibility

3. **High cardinality warnings:**
   - Review tag values for uniqueness
   - Implement tag value limits
   - Use tag aggregation strategies

### Debug Commands

```bash
# Check tag inference
curl -X GET "http://localhost:8080/users/profile" \
  -H "Authorization: Bearer <token>" \
  -v

# View application logs
tail -f logs/evently.log | grep "Tags:"

# Check metrics endpoint
curl http://localhost:8080/actuator/metrics
```

## Future Roadmap

### Phase 1: Enhanced Observability
- [ ] Micrometer integration for custom metrics
- [ ] Distributed tracing with OpenTelemetry
- [ ] Health checks and probes

### Phase 2: Advanced Tagging
- [ ] Dynamic tag configuration
- [ ] Hierarchical tag system
- [ ] Tag-based routing and policies

### Phase 3: AI/ML Integration
- [ ] Anomaly detection on tag-based metrics
- [ ] Predictive scaling based on tag patterns
- [ ] Automated incident response

### Phase 4: Enterprise Features
- [ ] Multi-tenant tag isolation
- [ ] Tag-based cost allocation
- [ ] Advanced business intelligence dashboards

## Conclusion

The tags system transforms Evently from a basic web application into a **production-ready, observable, and manageable system**. By categorizing requests by business domain, we enable:

- **Operational Excellence:** Faster incident response and capacity planning
- **Business Intelligence:** Data-driven decisions with domain-specific insights
- **Performance Optimization:** Targeted improvements based on domain metrics
- **Scalability:** Horizontal scaling decisions based on usage patterns

This foundation supports the application's growth from a simple event management system to a comprehensive, enterprise-grade platform with full observability and operational management capabilities.

---

**Document Version:** 1.0
**Last Updated:** October 15, 2025
**Authors:** Evently Development Team