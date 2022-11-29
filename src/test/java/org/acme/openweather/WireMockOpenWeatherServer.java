package org.acme.openweather;

import java.util.Collections;
import java.util.Map;

import org.openweathermap.model.Model200;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WireMockOpenWeatherServer implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    public static final String URL_KEY = "quarkus.rest-client.openweather_yaml.url";
    private static final Logger LOGGER = LoggerFactory.getLogger(WireMockOpenWeatherServer.class);

    @Override
    public Map<String, String> start() {
        final ObjectMapper mapper = new ObjectMapper();
        final Model200 response = new Model200();
        response.setWeather(Collections.emptyList());

        wireMockServer = new WireMockServer(8888);
        wireMockServer.start();

        try {
            wireMockServer.stubFor(get(urlPathEqualTo("/data/2.5/weather"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(mapper.writeValueAsString(response))));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to write object as json", e);
            throw new IllegalArgumentException(e);
        }

        return Map.of(URL_KEY, wireMockServer.baseUrl().concat("/data/2.5"));
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer, f -> f.getName().equals("openWeatherServer"));
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}