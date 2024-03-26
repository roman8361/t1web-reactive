package ru.kravchenko.payment.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestConfig {
    @Value("${integrations.product.base-url}")
    private String productUrl;
    @Value("${integrations.limit.base-url}")
    private String limitUrl;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
    }

    @Bean
    public WebClient baseClient(WebClient.Builder webClientBuilder, HttpClient httpClient) {
        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient productWebClient(WebClient baseClient) {
        return baseClient.mutate()
                .baseUrl(productUrl)
                .build();
    }

    @Bean
    public WebClient limitWebClient(WebClient baseClient) {
        return baseClient.mutate()
                .baseUrl(limitUrl)
                .build();
    }
}
