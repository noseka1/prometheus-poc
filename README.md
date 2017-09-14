# A Vert.x application instrumented using Prometheus

This project is a sample application instrumented using Prometheus libraries. Application exposes an endpoint to allow Prometheus to scrape the metrics. The exposed metrics are:

* Internal metrics of the Vert.x toolkit, see also [Vert.x Dropwizard Metrics](http://vertx.io/docs/vertx-dropwizard-metrics/java/)
* Java Virtual Machine metrics
* Log metrics (counts how many DEBUG, INFO, WARN and ERROR messages have been logged)
* A custom metric (mycounter)

Metrics are exposed in the [Prometheus's text format](https://prometheus.io/docs/instrumenting/exposition_formats/) at the endpoint http://localhost:8080/metrics

A sample of the exposed metrics can be found in the file [sample_output.txt](sample_output.txt)

Build this project:

```
$ mvn clean package
```

Run the application:

```
$ java -jar ./target/vertx-metrics-1.0-SNAPSHOT-fat.jar
```

Get the metrics in Prometheus format:

```
$ curl localhost:8080/metrics
```
