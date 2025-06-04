package ar.utn.ba.ddsi.mailing.services.impl;

import ar.utn.ba.ddsi.mailing.config.AlertasConfig;
import ar.utn.ba.ddsi.mailing.models.dtos.EmailInputDto;
import ar.utn.ba.ddsi.mailing.models.entities.Clima;
import ar.utn.ba.ddsi.mailing.models.entities.Email;
import ar.utn.ba.ddsi.mailing.models.repositories.IClimaRepository;
import ar.utn.ba.ddsi.mailing.services.IAlertasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;

@Service
public class AlertasService implements IAlertasService {
    private AlertasConfig alertasConfig;
    private static final Logger logger = LoggerFactory.getLogger(AlertasService.class);

    private final IClimaRepository climaRepository;
    private final EmailService emailService;
    private final String remitente;
    private final List<String> destinatarios;

    public AlertasService(
            IClimaRepository climaRepository, 
            EmailService emailService,
            AlertasConfig alertasConfig,
            @Value("${alertas.clima.temperatura}") double TEMPERATURA_ALERTA,
            @Value("${alertas.clima.humedad}") int HUMEDAD_ALERTA,
            @Value("${email.alertas.remitente}") String remitente,
            @Value("${email.alertas.destinatarios}") String destinatarios) {
        this.climaRepository = climaRepository;
        this.emailService = emailService;
        this.alertasConfig = alertasConfig;
        this.remitente = remitente;
        this.destinatarios = Arrays.asList(destinatarios.split(","));
    }

    @Override
    public Mono<Void> generarAlertasYAvisar() {
        return Mono.fromCallable(() -> climaRepository.findByProcesado(false))
            .flatMap(climas -> {
                logger.info("Procesando {} registros de clima no procesados", climas.size());
                return Mono.just(climas);
            })
            .flatMap(climas -> {
                climas.stream()
                    .filter(this::cumpleCondicionesAlerta)
                    .forEach(this::generarYEnviarEmail);
                
                // Marcar todos como procesados
                climas.forEach(clima -> {
                    clima.setProcesado(true);
                    climaRepository.save(clima);
                });
                
                return Mono.empty();
            })
            .onErrorResume(e -> {
                logger.error("Error al procesar alertas: {}", e.getMessage());
                return Mono.empty();
            })
            .then();
    }

    private boolean cumpleCondicionesAlerta(Clima clima) {
        return clima.getTemperatura().getTemperaturaCelsius() > alertasConfig.getTemperaturaAlerta() &&
               clima.getHumedad() > alertasConfig.getHumedadAlerta();
    }

    private void generarYEnviarEmail(Clima clima) {
        String asunto = "Alerta de Clima - Condiciones Extremas";
        String mensaje = String.format(
            "ALERTA: Condiciones climáticas extremas detectadas en %s\n\n" +
            "Temperatura: %.1f°C\n" +
            "Humedad: %d%%\n" +
            "Condición: %s\n" +
            "Velocidad del viento: %.1f km/h\n\n" +
            "Se recomienda tomar precauciones.",
            clima.getUbicacion().getCiudad(),
            clima.getTemperatura().getTemperaturaCelsius(),
            clima.getHumedad(),
            clima.getCondicion(),
            clima.getVelocidadVientoKmh()
        );

        for (String destinatario : destinatarios) {
            EmailInputDto email = new EmailInputDto(destinatario, remitente, asunto, mensaje);
            emailService.crearEmail(email);
        }
        
        logger.info("Email de alerta generado para {} - Enviado a {} destinatarios", 
            clima.getUbicacion().getCiudad(), destinatarios.size());
    }
} 