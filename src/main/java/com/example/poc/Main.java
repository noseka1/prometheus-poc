package com.example.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.hotspot.DefaultExports;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.Match;
import io.vertx.ext.dropwizard.MatchType;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // enable the Vert.x metrics collection using the Dropwizard metrics
        // library, see also http://vertx.io/docs/vertx-dropwizard-metrics/java/
        DropwizardMetricsOptions metricsOptions = new DropwizardMetricsOptions();
        metricsOptions.setEnabled(true);
        metricsOptions.setRegistryName(Constants.METRICS_REGISTRY_NAME);

        // don't know if the following two lines have any impact. The Vert.x
        // event bus metrics and the HTTP server metrics are collected even
        // when these two lines were commented out.
        metricsOptions.addMonitoredEventBusHandler(new Match().setValue(Constants.METRICS_EVENT_BUS_ADDRESS));
        metricsOptions.addMonitoredHttpServerUri(new Match().setValue("/.*").setType(MatchType.REGEX));

        VertxOptions options = new VertxOptions();
        options.setMetricsOptions(metricsOptions);
        Vertx vertx = Vertx.factory.vertx(options);

        // deploy a verticle that will expose Prometheus metrics at
        // localhost:8080/metrics
        LOG.info("Deploying verticle {}", MetricsVerticle.class);
        vertx.deployVerticle(new MetricsVerticle());

        // grab the Dropwizard metrics registry and attach it to the Prometheus
        // registry
        MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_REGISTRY_NAME);
        CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistry));

        // initialize JVM metrics collection using Prometheus
        DefaultExports.initialize();

    }
}
