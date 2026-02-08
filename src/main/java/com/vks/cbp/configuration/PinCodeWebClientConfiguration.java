package com.vks.cbp.configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.vks.cbp.properties.model.PinCodeProps;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class PinCodeWebClientConfiguration {

	private final PinCodeProps pinCodeProps;

	@Bean
	public WebClient webClient() {
		HttpClient httpClient = HttpClient.create()
				// Connection Timeout: time to establish the connection (5 seconds)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, pinCodeProps.timeout())
				// Response Timeout: time between sending request and receiving full response (5
				// seconds)
				.responseTimeout(Duration.ofMillis(pinCodeProps.timeout()))
				// Read/Write Timeouts: time for no data transfer during an active connection (5
				// seconds)
				.doOnConnected(conn -> conn
						.addHandlerLast(new ReadTimeoutHandler(pinCodeProps.timeout(), TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(pinCodeProps.timeout(), TimeUnit.MILLISECONDS)));
		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
		return WebClient.builder().baseUrl(pinCodeProps.baseUrl()).clientConnector(connector).build();
	}
}
