package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.models.entities.Clima;
import ar.utn.ba.ddsi.mailing.models.entities.Temperatura;
import ar.utn.ba.ddsi.mailing.models.entities.Ubicacion;
import ar.utn.ba.ddsi.mailing.models.repositories.IClimaRepository;
import ar.utn.ba.ddsi.mailing.models.dto.external.weatherapi.WeatherResponse;
import ar.utn.ba.ddsi.mailing.services.IClimaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClimaService implements IClimaService {
    private static final Logger logger = LoggerFactory.getLogger(ClimaService.class);
    private static final String[] CIUDADES_ARGENTINA = {
        "Buenos Aires", "Cordoba", "Rosario", "Mendoza", "Tucuman",
        "La Plata", "Mar del Plata", "Salta", "Santa Fe", "San Juan"
    };

    private final IClimaRepository climaRepository;
    private final WebClient webClient;
    private final String apiKey;

    public ClimaService(
            IClimaRepository climaRepository,
            @Value("${weather.api.key}") String apiKey,
            @Value("${weather.api.base-url}") String baseUrl) {
        this.climaRepository = climaRepository;
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public Mono<Void> actualizarClimaCiudades() {
        return Flux.fromArray(CIUDADES_ARGENTINA)
            .flatMap(this::obtenerClimaDeAPI)
            .flatMap(clima -> {
                climaRepository.save(clima);
                logger.info("Clima actualizado para: {}", clima.getUbicacion().getCiudad());
                return Mono.empty();
            })
            .onErrorResume(e -> {
                logger.error("Error al actualizar el clima: {}", e.getMessage());
                return Mono.empty();
            })
            .then();
    }

    private Mono<Clima> obtenerClimaDeAPI(String ciudad) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/current.json")
                .queryParam("key", apiKey)
                .queryParam("q", ciudad)
                .queryParam("aqi", "no")
                .build())
            .retrieve()
            .bodyToMono(WeatherResponse.class)
            .map(response -> {
                Clima clima = new Clima();
                clima.setUbicacion(new Ubicacion(ciudad,response.getLocation().getRegion(),response.getLocation().getCountry()));
                clima.setTemperatura(new Temperatura(response.getCurrent().getTemp_c()));
                clima.setCondicion(response.getCurrent().getCondition().getText());
                clima.setVelocidadVientoKmh(response.getCurrent().getWind_kph());
                clima.setHumedad(response.getCurrent().getHumidity());
                return clima;
            });
    }
} 