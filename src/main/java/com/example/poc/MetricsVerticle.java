package com.example.poc;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MetricsVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        // bind Prometheus metrics handler to /metrics
        Router router = Router.router(vertx);
        router.get("/metrics").handler(new MetricsHandler());

        // start httpserver on localhost:8080
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        // send a message every second to bump up the metric counters
        vertx.setPeriodic(1_000L, e -> vertx.eventBus().send(Constants.METRICS_EVENT_BUS_ADDRESS, new JsonObject()));

        // increase the custom metric counter every second
        MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_REGISTRY_NAME);
        vertx.setPeriodic(1_000L, e -> metricRegistry.counter(Constants.METRICS_CUSTOM_COUNTER).inc());
    }
}