package co.com.bootcamp.consumer.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    private final String url;
    private final String capacidadUrl;
    private final String tecnologiaUrl;
    private final int timeout;

    public RestConsumerConfig(@Value("${adapters.restconsumer.url}") String url,
                              @Value("${adapters.restconsumer.capacidad-url}") String capacidadUrl,
                              @Value("${adapters.restconsumer.tecnologia-url}") String tecnologiaUrl,
                              @Value("${adapters.restconsumer.timeout}") int timeout) {
        this.url = url;
        this.capacidadUrl = capacidadUrl;
        this.tecnologiaUrl = tecnologiaUrl;
        this.timeout = timeout;
    }

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(getClientHttpConnector())
            .build();
    }

    @Bean("capacidadWebClient")
    public WebClient getCapacidadWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(capacidadUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(getClientHttpConnector())
            .build();
    }

    @Bean("tecnologiaWebClient")
    public WebClient getTecnologiaWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(tecnologiaUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(getClientHttpConnector())
            .build();
    }

    private ClientHttpConnector getClientHttpConnector() {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED: this should be in the default cacerts trustore
        */
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

}
