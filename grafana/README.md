# Microservices Overview (All Services) Dashboard Documentation

This Grafana dashboard provides a comprehensive overview of the health and performance of your microservices, leveraging Prometheus metrics. It is designed to offer quick insights into key performance indicators, request and latency trends, and JVM/system health across all monitored services.

---

## Sections and Panels

Used dashboard is stored under provisioning/dashboards/sample-dashboard.json
The dashboard with the data is available on our rancher ingress.

Beyond visual monitoring, this dashboard has a configured alert.

Service Down Alert (up == 0): An essential alert to set up is for the up metric. Prometheus automatically generates an up metric for each configured scrape target, which has a value of 1 if the target is healthy (successfully scraped) and 0 if it's down or unreachable. An alert rule like up == 0 (or up == 0 for: 5m to reduce flapping) will trigger when a service becomes unavailable. This alert can be configured to send notifications via a Discord webhook, ensuring immediate visibility of service outages within your team's chat channels. This allows for rapid response and minimizes downtime.

The dashboard itself is logically organized into three main sections: **Key Performance Indicators (KPIs)**, **Request & Latency Analysis**, and **JVM & System Health**.

### Key Performance Indicators (All Services)

This section provides a high-level summary of the overall health of your microservices cluster.

| Panel Title | Type | Description | Prometheus Query |
| :----- | :----- | :----- | :----- |
| **Total Requests per Second** | Stat | Displays the **total number of HTTP requests processed per second** across all microservices. This is a crucial metric for understanding overall system load and throughput. | `sum(rate(http_server_requests_seconds_count[$__rate_interval]))` |
| **Overall Error Rate, outcome not success** | Stat | Shows the **total count of requests that did not have a successful outcome** (i.e., `outcome` is not "SUCCESS"). This includes both client errors (4xx) and server errors (5xx), excluding metrics from Actuator endpoints. This provides a holistic view of problematic requests. | `sum(http_server_requests_seconds_count{outcome!="SUCCESS", uri!~"^/api/actuator/.*"})` |
| **Average Latency** | Stat | Calculates the **average response time (latency) for all HTTP requests** across all microservices, presented in milliseconds. This is a critical indicator of user experience and system responsiveness. | `sum(rate(http_server_requests_seconds_sum[$__rate_interval])) / sum(rate(http_server_requests_seconds_count[$__rate_interval])) * 1000` |
| **Total Live Threads** | Stat | Displays the **total number of live threads** currently active across all JVMs running your microservices. A sudden increase could indicate thread contention or resource exhaustion, while a consistent high number might suggest inefficient thread management. | `sum(jvm_threads_live_threads)` |

---

### Request & Latency Analysis

This section dives deeper into the distribution of requests and latency by individual services and their specific URIs.

| Panel Title | Type | Description | Prometheus Queries |
| :----- | :----- | :----- | :----- |
| **Request Rate by Service & URI** | Time Series | Visualizes the **requests per second (RPS) for each microservice broken down by specific URI paths**. This helps identify traffic patterns, highly utilized endpoints, or unexpected spikes in request volume for individual API calls. | `sum(rate(http_server_requests_seconds_count[$__rate_interval])) by (job, uri)` |
| **Request Latency by Service & URI** | Time Series | Shows detailed **latency metrics (P99, P95, and Average) for each microservice's URI endpoints**. This panel is crucial for pinpointing slow endpoints and understanding the user experience impact of different API calls. Latency is measured in milliseconds. | **P99 Latency:** `histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket[$__rate_interval])) by (le, job, uri)) * 1000`<br>**P95 Latency:** `histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[$__rate_interval])) by (le, job, uri)) * 1000`<br>**Average Latency:** `sum(rate(http_server_requests_seconds_sum[$__rate_interval])) by (job, uri) / sum(rate(http_server_requests_seconds_count[$__rate_interval])) by (job, uri) * 1000` |
| **Request Count by Service, Status & Method** | Pie Chart | Presents a **breakdown of total request counts by service, HTTP status code (e.g., 200 OK, 404 Not Found, 500 Internal Server Error), and HTTP method (GET, POST, PUT, etc.)** over the dashboard's time range. This offers a quick visual of which services, statuses, and methods are most prevalent or problematic. Hidden series are excluded for clarity. | `sum(increase(http_server_requests_seconds_count[$__range])) by (job, status, method)` |

---

### JVM & System Health

This section provides insights into the underlying Java Virtual Machine (JVM) and basic system resource usage for your microservices.

| Panel Title | Type | Description | Prometheus Queries |
| :----- | :----- | :----- | :----- |
| **JVM Heap Memory** | Time Series | Monitors the **heap memory usage of each JVM**. It tracks Used, Committed, and Max heap memory, helping to identify potential memory leaks or misconfigurations. Memory is measured in bytes. | **Used:** `jvm_memory_used_bytes{area="heap"}`<br>**Committed:** `jvm_memory_committed_bytes{area="heap"}`<br>**Max:** `jvm_memory_max_bytes{area="heap"}` |
| **JVM Threads** | Time Series | Displays the **number of live and daemon threads for each JVM**. High numbers or unusual spikes in live threads can indicate contention or resource issues, while tracking daemon threads helps understand background processes. | **Live Threads:** `jvm_threads_live_threads`<br>**Daemon Threads:** `jvm_threads_daemon_threads` |
| **Process CPU Usage** | Time Series | Shows the **CPU utilization of each microservice process** as a percentage. This helps in identifying services that are consuming excessive CPU resources, which might indicate performance bottlenecks or inefficiencies in their code. | `process_cpu_usage * 100` |

---