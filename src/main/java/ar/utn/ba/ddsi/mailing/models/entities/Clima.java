package ar.utn.ba.ddsi.mailing.models.entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Clima {
    private Long id;
    private Ubicacion ubicacion;
    private Temperatura temperatura;
    private String condicion;
    private Double velocidadVientoKmh;
    private Integer humedad;
    private LocalDateTime fechaActualizacion;
    private boolean procesado;

    public Clima() {
        this.fechaActualizacion = LocalDateTime.now();
        this.procesado = false;
    }
} 