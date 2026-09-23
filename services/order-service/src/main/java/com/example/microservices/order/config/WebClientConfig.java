package com.example.microservices.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    /**
     * HTTP client bean — name must differ from the {@code ProductWebClient} component
     * ({@code webProductClient}). Same idea as {@code productRestClient} vs {@code RestProductClient}.
     */
    @Bean(name = "productWebClient")
    public WebClient productWebClient(ProductClientProperties properties) {

        ConnectionProvider connectionProvider = ConnectionProvider.builder("product-service")
                .maxConnections(properties.maxConnections())
                .pendingAcquireTimeout(properties.connectTimeout())
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) properties.connectTimeout().toMillis())
                .responseTimeout(properties.readTimeout());

        return WebClient.builder()
                .baseUrl(properties.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
